package com.dryseed.ijkplayerdemo;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toast;

import com.dryseed.ijkplayerdemo.application.Settings;
import com.dryseed.ijkplayerdemo.widget.media.AndroidMediaController;
import com.dryseed.ijkplayerdemo.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {

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

        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mMediaController = new AndroidMediaController(this, false);

        mVideoView.setHudView(new TableLayout(this));
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoURI(Uri.parse("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8"));

        mVideoView.start();

    }

    @Override
    public void onBackPressed() {
        mBackPressed = true;
        super.onBackPressed();
    }
    @Override
    protected void onStop() {
        super.onStop();
        //点击返回或不允许后台播放时 释放资源
        if (mBackPressed || !mVideoView.isBackgroundPlayEnabled()) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        } else {
            mVideoView.enterBackground();
        }
        IjkMediaPlayer.native_profileEnd();
    }
}
