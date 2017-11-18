package com.dryseed.ijkplayerdemo.simple.widget.media;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.view.SurfaceHolder;

/**
 * Created by zhanggao1 on 2017/2/28.
 */

public interface IPlayerControl {

    void setPlayerOptions(PlayerOptions options);

    void setVideoPath(String path);

    void setVideoURI(Uri uri);

    void setOnPlayerStateListener(OnPlayerStateListener listener);

    void setMediaController(IMediaController controller);

    void addSurfaceHolderCallback(SurfaceHolder.Callback surfaceHolderCallback);


    /**
     * playback or live 开始播放,
     */
    void start();

    /**
     * playback 暂停，直播调用暂停无意义;回放调用后不会停止拉数据
     */
    void pause();

    /**
     * playback or live 重新开始播放
     */
    void resume();

    /**
     * playback or live 停止。回放调用后会停止获取数据，下次需要调用 resume 重新开始播放
     */
    void suspend();

    void release();

    int getDuration();

    int getCurrentPosition();

    void seekTo(int position);

    boolean isPlaying();

    int getBufferPercentage();

    long getTcpSpeed();

    class PlayerOptions {

        public static final int AR_ASPECT_FIT_PARENT = IRenderView.AR_ASPECT_FIT_PARENT; // without clip
        public static final int AR_ASPECT_FILL_PARENT = IRenderView.AR_ASPECT_FILL_PARENT; // may clip
        public static final int AR_ASPECT_WRAP_CONTENT = IRenderView.AR_ASPECT_WRAP_CONTENT;
        public static final int AR_MATCH_PARENT = IRenderView.AR_MATCH_PARENT;
        public static final int AR_16_9_FIT_PARENT = IRenderView.AR_16_9_FIT_PARENT;
        public static final int AR_4_3_FIT_PARENT = IRenderView.AR_4_3_FIT_PARENT;

        protected boolean isLive;                             // 是否是直播，必须设置
        protected boolean isCouldMediaCodec;                  // 是否使用硬解，默认直播用硬解，回放软解
        protected boolean isStartOnPrepared = true;           // 是否加载好后自动开始播，默认自动开始
        protected int maxLiveDelay = 3000;                    // 直播最大延迟，超过则丢帧，回放设置无用
        protected int minLiveDuration = 500;                  // 直播丢帧保留时长
        protected boolean isUseTextureView = true;            // 默认使用 TextureView， false 则使用 SurfaceView
        protected int aspectRatio = AR_ASPECT_FILL_PARENT;    // 默认等比缩放到父布局一样大，多的裁剪
        protected boolean isDebugLog = false;                 // 是否需要 ijkPlayer log
        protected boolean isShowHubView = false;

        public PlayerOptions(boolean isLiveStream) {
            isLive = isLiveStream;
            isCouldMediaCodec = isLive;
        }

        public PlayerOptions setCouldMediaCodec(boolean couldMediaCodec) {
            isCouldMediaCodec = couldMediaCodec;
            return this;
        }

        public PlayerOptions setStartOnPrepared(boolean startOnPrepared) {
            isStartOnPrepared = startOnPrepared;
            return this;
        }

        public PlayerOptions setMaxLiveDelay(int maxLiveDelay) {
            this.maxLiveDelay = maxLiveDelay;
            return this;
        }

        public PlayerOptions setMinLiveDuration(int minLiveDuration) {
            this.minLiveDuration = minLiveDuration;
            return this;
        }

        public PlayerOptions setDebugLog(boolean debugLog) {
            isDebugLog = debugLog;
            return this;
        }

        public PlayerOptions setUseTextureView(boolean useTextureView) {
            isUseTextureView = useTextureView;
            return this;
        }

        public PlayerOptions setAspectRatio(@AspectRatioType int aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this;
        }

        public PlayerOptions setShowHubView(boolean showHubView) {
            isShowHubView = showHubView;
            return this;
        }
    }

    interface OnPlayerStateListener {
        void onReload();

        void onPrepared();

        void onCompletion();

        boolean onError(int frameworkErr, int implErr);

        boolean onInfo(int mediaInfo, int degree);

        void onSeekComplete();
    }

    @IntDef({PlayerOptions.AR_ASPECT_FIT_PARENT, PlayerOptions.AR_ASPECT_FILL_PARENT,
            PlayerOptions.AR_ASPECT_WRAP_CONTENT, PlayerOptions.AR_MATCH_PARENT,
            PlayerOptions.AR_16_9_FIT_PARENT, PlayerOptions.AR_4_3_FIT_PARENT})
    @interface AspectRatioType {

    }
}
