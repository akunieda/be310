package com.example.be310birdtracker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	int PICTURE_REQ_CODE = 1;
	private static String LOG_TAG = "log tag";

	ImageView mImageView;
	Bitmap mImageBitmap;

    private static String mFileName = null;
	private ExtAudioRecorder mExtAudioRecorder = null;
	protected boolean mStartRecording, mStartPlaying;
	private MediaPlayer mPlayer = null;

	private Button mRecordButton, mPlayButton;
	private Button mSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecordButton = (Button) findViewById(R.id.button1);
        mRecordButton.setText("Start recording");
        mStartRecording = true;
        mRecordButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    ((Button) v).setText("Stop recording");
                    mPlayButton.setActivated(true);
                } else {
                	((Button) v).setText("Start recording");
                }
                mStartRecording = !mStartRecording;
			}
        });

        mPlayButton = (Button) findViewById(R.id.button2);
        mPlayButton.setText("Start playing");
        mPlayButton.setActivated(false);
        mStartPlaying = true;
        mPlayButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                	((Button) v).setText("Stop playing");
                } else {
                	((Button) v).setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        });

        mSendButton = (Button) findViewById(R.id.button3);
        mSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                postData();
            }
        });

        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dispatchTakePictureIntent(PICTURE_REQ_CODE);
			}
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void dispatchTakePictureIntent(int actionCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, actionCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == PICTURE_REQ_CODE) {
            Bundle extras = data.getExtras();
            mImageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(mImageBitmap);
    	}
    }

    private void startRecording() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.wav";

    	mExtAudioRecorder = ExtAudioRecorder.getInstanse(false);
        try {
	    	mExtAudioRecorder.setOutputFile(mFileName);
	    	mExtAudioRecorder.prepare();
	    	mExtAudioRecorder.start();
        } catch (Exception e) {
            Log.e(LOG_TAG , "recording failed");
        }
    }

    private void stopRecording() {
    	mExtAudioRecorder.stop();
    	mExtAudioRecorder.release();
    	mExtAudioRecorder = null;
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG , "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    public void postData() {
    	new PostDataTask().execute();
    }

    class PostDataTask extends AsyncTask <Void, Void, String>{
        @Override
        protected String doInBackground(Void... unsued) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://10.0.2.2:5000/form/");

            try {
                MultipartEntity reqEntity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                mImageBitmap.compress(CompressFormat.JPEG, 75, bos);
                byte[] imageData = bos.toByteArray();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFilename = "IMG_"+ timeStamp + ".jpg";

                reqEntity.addPart("image", new ByteArrayBody(imageData, imageFilename));
                reqEntity.addPart("name", new StringBody("Furnarius rufus"));
                reqEntity.addPart("date", new StringBody("2013-06-30"));
                reqEntity.addPart("time", new StringBody("00:50"));
                reqEntity.addPart("coord_lat", new StringBody("-22.817708"));
                reqEntity.addPart("coord_lng", new StringBody("-47.068316"));

                httppost.setEntity(reqEntity);

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                return "cool";
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
            	e.printStackTrace();
            }

            return "no cool";
        }
    }
}
