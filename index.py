import os
from flask import Flask, render_template, request
from werkzeug import secure_filename
import bird

UPLOAD_IMAGE_FOLDER = 'static/images'
UPLOAD_SOUND_FOLDER = 'static/sounds'
IMAGE_EXTENSIONS = set(['png', 'jpg', 'jpeg', 'gif'])
SOUND_EXTENSIONS = set(['mp3', 'wav'])

app = Flask(__name__)
app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024


def media_type_f(filename):
    if filename.endswith(".wav"):
        return u'wav'
    elif filename.endswith(".mp3"):
        return u'mpeg'
    else:
        return None

app.jinja_env.globals.update(media_type_f=media_type_f)


@app.route('/')
def main():
    registros, locais = bird.get_last_records()
    return render_template('index.html', passaros=bird.get_birds(), registros=registros, locais=locais)


def allowed_file(filename, filetype=None):
    if filetype == "image":
        extensions = IMAGE_EXTENSIONS
    elif filetype == "sound":
        extensions = SOUND_EXTENSIONS
    else:
        extensions = IMAGE_EXTENSIONS
        extensions.union(SOUND_EXTENSIONS)

    return '.' in filename and \
           filename.rsplit('.', 1)[1] in extensions


@app.route('/form/', methods=['GET', 'POST'])
def form():
    if request.method == 'POST':
        print request.files
        print request.form

        image = request.files.get('image')
        sound = request.files.get('sound')
        name = request.form.get('name')
        common_name = request.form.get('common_name')
        date = request.form.get('date')
        time = request.form.get('time')
        coord_lat = request.form.get('coord_lat')
        coord_lng = request.form.get('coord_lng')

        if image and allowed_file(image.filename, "image"):
            image_filename = secure_filename(image.filename)
            image.save(os.path.join(UPLOAD_IMAGE_FOLDER, image_filename))
        else:
            image_filename = None

        if sound and allowed_file(sound.filename, "sound"):
            sound_filename = secure_filename(sound.filename)
            sound.save(os.path.join(UPLOAD_SOUND_FOLDER, sound_filename))
        else:
            sound_filename = None

        bird.store(name, common_name, date, time, image_filename, sound_filename, coord_lat, coord_lng)

        return render_template('receive_form.html',
                               name=name,
                               common_name=common_name,
                               date=date,
                               time=time,
                               image_filename=image_filename,
                               sound_filename=sound_filename,
                               coord_lat=coord_lat,
                               coord_lng=coord_lng)

    return render_template('form.html')


@app.route('/bird/<name>/')
def bird_page(name=None):
    if name is None:
        return main()
    else:
        passaro, registros = bird.get_bird(name)
        return render_template('bird.html', passaro=passaro, registros=registros)


if __name__ == '__main__':
    app.run(debug=True)
