package com.dryseed.ijkplayerdemo.simple.widget.media;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.MediaPlayerProxy;

public class InfoHudViewHolder {

    private IjkMediaPlayer mMediaPlayer;
    private HashMap<String, TextView> mRowMap = new HashMap<>();
    private LinearLayout leftInfoLayout;
    private LinearLayout rightInfoLayout;
    private Context context;

    public InfoHudViewHolder(Context context, ViewGroup parentViewGroup) {
        this.context = context;

        LinearLayout infoParentLayout = new LinearLayout(context);
        infoParentLayout.setOrientation(LinearLayout.HORIZONTAL);
        FrameLayout frameLayout = (FrameLayout)parentViewGroup.getParent();
        frameLayout.addView(infoParentLayout,
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));

        leftInfoLayout = new LinearLayout(context);
        leftInfoLayout.setOrientation(LinearLayout.VERTICAL);
        rightInfoLayout = new LinearLayout(context);
        rightInfoLayout.setOrientation(LinearLayout.VERTICAL);
        infoParentLayout.addView(leftInfoLayout);
        infoParentLayout.addView(rightInfoLayout);
    }

    private void setRowValue(String key, String value) {
        TextView textView = mRowMap.get(key);
        if (textView == null) {
            textView = createValueView(key);
            mRowMap.put(key, textView);
        }
        textView.setText(value);
    }

    private TextView createValueView(String key) {
        TextView keyView = new TextView(context);
        keyView.setTextColor(0xffff0000);
        keyView.setText(key + ":  ");
        leftInfoLayout.addView(keyView);

        TextView valueView = new TextView(context);
        valueView.setTextColor(0xffff0000);
        rightInfoLayout.addView(valueView);
        return valueView;
    }

    public void setMediaPlayer(IMediaPlayer mp) {
        if (null == mp) {
            return;
        }
        if (mp instanceof IjkMediaPlayer) {
            mMediaPlayer = (IjkMediaPlayer) mp;
        } else if (mp instanceof MediaPlayerProxy) {
            MediaPlayerProxy proxy = (MediaPlayerProxy) mp;
            IMediaPlayer internal = proxy.getInternalMediaPlayer();
            if (internal != null && internal instanceof IjkMediaPlayer) {
                mMediaPlayer = (IjkMediaPlayer) internal;
            }
        }

        if (mMediaPlayer != null) {
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_HUD, 500);
        } else {
            mHandler.removeMessages(MSG_UPDATE_HUD);
        }
    }

    public void updateLoadCost(long loadCost) {
        setRowValue("load_cost", String.format(Locale.US, "%d ms", loadCost));

        if (null == mMediaPlayer) {
            return;
        }
        setRowValue("resolution", "w:" + mMediaPlayer.getVideoWidth() + " h:" + mMediaPlayer.getVideoHeight() +
                " Num:" + mMediaPlayer.getVideoSarNum() + " Den:" + mMediaPlayer.getVideoSarDen());
    }

    public void updateSeekCost(long seekCost) {
        setRowValue("seek_cost", String.format(Locale.US, "%d ms", seekCost));
    }

    private static final int MSG_UPDATE_HUD = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_HUD: {
                    if (null == mMediaPlayer) {
                        break;
                    }

                    int vdec = mMediaPlayer.getVideoDecoder();
                    switch (vdec) {
                        case IjkMediaPlayer.FFP_PROPV_DECODER_AVCODEC:
                            setRowValue("vdec", "avcodec");
                            break;
                        case IjkMediaPlayer.FFP_PROPV_DECODER_MEDIACODEC:
                            setRowValue("vdec", "MediaCodec");
                            break;
                        default:
                            setRowValue("vdec", "unknown");
                            break;
                    }

                    float fpsOutput = mMediaPlayer.getVideoOutputFramesPerSecond();
                    float fpsDecode = mMediaPlayer.getVideoDecodeFramesPerSecond();
                    setRowValue("fps", String.format(Locale.US, "%.2f / %.2f", fpsDecode, fpsOutput));

                    long videoCachedDuration = mMediaPlayer.getVideoCachedDuration();
                    long audioCachedDuration = mMediaPlayer.getAudioCachedDuration();
                    long videoCachedBytes = mMediaPlayer.getVideoCachedBytes();
                    long audioCachedBytes = mMediaPlayer.getAudioCachedBytes();
                    long tcpSpeed = mMediaPlayer.getTcpSpeed();
                    long bitRate = mMediaPlayer.getBitRate();
                    long seekLoadDuration = mMediaPlayer.getSeekLoadDuration();

                    setRowValue("v_cache", String.format(Locale.US, "%s, %s", formattedDurationMilli(videoCachedDuration), formattedSize(videoCachedBytes)));
                    setRowValue("a_cache", String.format(Locale.US, "%s, %s", formattedDurationMilli(audioCachedDuration), formattedSize(audioCachedBytes)));
                    setRowValue("seek_cost", String.format(Locale.US, "%d ms", seekLoadDuration));
                    setRowValue("tcp_speed", String.format(Locale.US, "%s", formattedSpeed(tcpSpeed, 1000)));
                    setRowValue("bit_rate", String.format(Locale.US, "%.2f kbs", bitRate / 1000f));

                    mHandler.removeMessages(MSG_UPDATE_HUD);
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_HUD, 500);
                }
            }
        }
    };

    private static String formattedDurationMilli(long duration) {
        if (duration >= 1000) {
            return String.format(Locale.US, "%.2f sec", ((float) duration) / 1000);
        } else {
            return String.format(Locale.US, "%d msec", duration);
        }
    }

    private static String formattedSpeed(long bytes, long elapsed_milli) {
        if (elapsed_milli <= 0) {
            return "0 B/s";
        }

        if (bytes <= 0) {
            return "0 B/s";
        }

        float bytes_per_sec = ((float) bytes) * 1000.f / elapsed_milli;
        if (bytes_per_sec >= 1000 * 1000) {
            return String.format(Locale.US, "%.2f MB/s", ((float) bytes_per_sec) / 1000 / 1000);
        } else if (bytes_per_sec >= 1000) {
            return String.format(Locale.US, "%.1f KB/s", ((float) bytes_per_sec) / 1000);
        } else {
            return String.format(Locale.US, "%d B/s", (long) bytes_per_sec);
        }
    }

    private String formattedTime(long duration) {
        long total_seconds = duration / 1000;
        long hours = total_seconds / 3600;
        long minutes = (total_seconds % 3600) / 60;
        long seconds = total_seconds % 60;
        if (duration <= 0) {
            return "--:--";
        }
        if (hours >= 100) {
            return String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds);
        }
    }

    private static String formattedSize(long bytes) {
        if (bytes >= 100 * 1000) {
            return String.format(Locale.US, "%.2f MB", ((float) bytes) / 1000 / 1000);
        } else if (bytes >= 100) {
            return String.format(Locale.US, "%.1f KB", ((float) bytes) / 1000);
        } else {
            return String.format(Locale.US, "%d B", bytes);
        }
    }
}
