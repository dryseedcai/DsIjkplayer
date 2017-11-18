package com.dryseed.ijkplayerdemo.jjdxm;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dryseed.ijkplayerdemo.R;
import com.dryseed.ijkplayerdemo.jjdxm.bean.VideoijkBean;
import com.dryseed.ijkplayerdemo.jjdxm.listener.OnShowThumbnailListener;
import com.dryseed.ijkplayerdemo.jjdxm.widget.PlayStateParams;
import com.dryseed.ijkplayerdemo.jjdxm.widget.PlayerView;
import com.dryseed.ijkplayerdemo.simple.widget.media.IjkVideoView;

import java.util.List;

/**
 * Created by User on 2017/11/18.
 */
public class MainActivity extends AppCompatActivity {
    private PlayerView player;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = getLayoutInflater().from(this).inflate(R.layout.activity_jjdxm_main, null);
        setContentView(rootView);

        Toast.makeText(MainActivity.this, "MMM", Toast.LENGTH_SHORT).show();


        player = new PlayerView(this, rootView) {
            @Override
            public PlayerView toggleProcessDurationOrientation() {
                hideSteam(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return setProcessDurationOrientation(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ? PlayStateParams.PROCESS_PORTRAIT : PlayStateParams.PROCESS_LANDSCAPE);
            }

            @Override
            public PlayerView setPlaySource(List<VideoijkBean> list) {
                return super.setPlaySource(list);
            }
        }
                .setPlaySource(Environment.getExternalStorageDirectory().getAbsolutePath() + "/11.mp4")
                .setTitle("Dryseed")
                .setProcessDurationOrientation(PlayStateParams.PROCESS_PORTRAIT)
                .setScaleType(PlayStateParams.wrapcontent)
                .forbidTouch(false)
                .hideSteam(true)
                .hideCenterPlayer(true)
                .showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView ivThumbnail) {
                        ivThumbnail.setImageResource(R.drawable.ic_launcher);
                    }
                })
                //.setPlaySource(list)
                //.setChargeTie(true,60)
                .startPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}