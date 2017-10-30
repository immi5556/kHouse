package com.khouse.immi.chuck24hoursbible;

/**
 * Created by Immanuel on 6/18/2017.
 */

        import android.content.Intent;
        import android.media.MediaPlayer;
        import android.media.MediaPlayer.OnPreparedListener;
        import android.net.Uri;
        import android.os.Bundle;
        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.util.Log;
        import android.widget.MediaController;
        import android.widget.VideoView;

public class VideoViewActivity extends Activity {

    // Declare variables
    ProgressDialog pDialog;
    VideoView videoview;
    VideoViewActivity self = this;
    // Insert your Video URL
    //String VideoURL = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";
    String VideoURL = "http://immanuel.co/chuck/video/";
    String pon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the layout from video_main.xml
        setContentView(R.layout.videoview_main);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                pon= null;
            } else {
                pon= extras.getString("path");
            }
        } else {
            pon= (String) savedInstanceState.getSerializable("path");
        }
        //VideoURL = VideoURL + pon + ".avi";
        // Find your VideoView in your video_main.xml layout
        videoview = (VideoView) findViewById(R.id.VideoView);
        // Execute StreamVideo AsyncTask

        // Create a progressbar
        pDialog = new ProgressDialog(VideoViewActivity.this);
        // Set progressbar title
        pDialog.setTitle("Chuck - 24 Hrs Bible");
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    VideoViewActivity.this);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            //Uri video = Uri.parse(VideoURL);
            Uri video = Uri.parse(pon);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            pDialog.dismiss();
            this.finish();
        }

        videoview.requestFocus();
        videoview.setOnPreparedListener(new OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoview.start();
            }
        });

        videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("video", "setOnErrorListener ");
                pDialog.dismiss();
                Intent i = new Intent();
                i.putExtra("vidRest", "Error");
                self.setResult(1);
                self.finish();
                return true;
            }
        });
    }

}