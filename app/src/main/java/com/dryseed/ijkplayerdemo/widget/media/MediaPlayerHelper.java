package com.dryseed.ijkplayerdemo.widget.media;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by zhanggao1 on 2017/2/28.
 */

public class MediaPlayerHelper {

    protected static IMediaPlayer createPlayer(IPlayerControl.PlayerOptions options) {
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        setPlayerAVOptions(ijkMediaPlayer, options);
        return ijkMediaPlayer;
    }

    protected static  void setPlayerAVOptions(IjkMediaPlayer ijkMediaPlayer, IPlayerControl.PlayerOptions options) {
        ijkMediaPlayer.native_setLogLevel(options.isDebugLog ? IjkMediaPlayer.IJK_LOG_DEBUG : IjkMediaPlayer.IJK_LOG_ERROR);

        // 1 -> hw codec enable, 0 -> disable [recommended]
        // 解码方式，codec＝1，硬解; codec=0, 软解
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", options.isCouldMediaCodec ? 1 : 0);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", options.isCouldMediaCodec ? 1 : 0);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 12L);
        // 是否自动启动播放，如果设置为 1，则在调用 `prepareAsync` 或者 `setVideoPath` 之后自动启动播放，无需调用 `start()`
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1L);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0L);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48L);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", options.isStartOnPrepared ? 1 : 0);
        if (options.isLive) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtmp_live", 1L);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtmp_buffer", 1000L);
            // the unit of timeout is ms
            // 准备超时时间，包括创建资源、建立连接、请求码流等，单位是 ms
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "timeout", 10 * 1000 * 1000);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "threads", "1");


            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", 0L);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 1L);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "first-high-water-mark-ms", 100L);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "next-high-water-mark-ms", 100L);
            // 缓存多少毫秒视频（视频可播放的时长）后开始播放
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "last-high-water-mark-ms", 500);

            // 超过多长延时就丢包 毫秒
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-live-delay", options.maxLiveDelay);
            // 丢帧保留时长 毫秒
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-live-duration", options.minLiveDuration);

            // 轮询方式，0 GOP，1固定时长
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "drop-format-mode", 0);
            // 开始播多长时间内不丢帧 毫秒
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "drop-format-open-start-time", 1000);
            // 丢帧间隔 GOP模式上次丢帧后该时长内不再触发丢帧、固定时长模式间隔 毫秒
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "drop-format-space-time", 1500);
        } else {
            // infinite_buffer 无限缓冲区 1 表示不限制缓冲区大小， 0 表示限制 默认15M
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", 0L);
            // 0 表示有数据就播放
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 1L);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "first-high-water-mark-ms", 100L);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "next-high-water-mark-ms", 100L);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "last-high-water-mark-ms", 500L);

            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-live-delay", 0);
        }

        // 检测视频信息相关
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", 200000L);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 16384L);
    }
/*
mediaInfo 增加丢帧回调 type = 10303
long audio = player._getPropertyLong(21001, 0);
long video = player._getPropertyLong(21002, 0);
低20位表示实际延时，高位表示丢弃的延时
丢弃延时 audio >> 20, video >> 20
延时 audio & 0xfffff  video & 0xfffff
*/
}
