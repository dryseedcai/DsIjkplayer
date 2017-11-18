package com.dryseed.ijkplayerdemo;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import com.dryseed.ijkplayerdemo.application.Settings;
import com.dryseed.ijkplayerdemo.widget.media.AndroidMediaController;
import com.dryseed.ijkplayerdemo.widget.media.IPlayerControl;
import com.dryseed.ijkplayerdemo.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {

    private Button mStartBtn;
    private IjkVideoView mVideoView;
    private Settings mSettings;
    private AndroidMediaController mMediaController;
    private boolean mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, "MMM", Toast.LENGTH_SHORT).show();

        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mStartBtn = (Button) findViewById(R.id.start_btn);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
    }

    private void start() {
//        IjkMediaPlayer.loadLibrariesOnce(null);
//        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        IPlayerControl.PlayerOptions playerOptions = new IPlayerControl.PlayerOptions(false);
        playerOptions.setShowHubView(true);
        mVideoView.setPlayerOptions(playerOptions);

        onResumeVideo();

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/11.mp4";
        Log.d("MMM", "path:" + path);
        mVideoView.setVideoURI(Uri.parse(path));

        //mVideoView.setVideoURI(Uri.parse("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mVideoView.suspend();
        mVideoView.release();
    }

    private void onResumeVideo() {
        mVideoView.suspend();
        initRenders();
    }

    public void initRenders() {
        mVideoView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mVideoView.initRenders();
            }
        }, 50);
    }
}
