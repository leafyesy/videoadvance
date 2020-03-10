package com.leafye.ffmpegapp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * @ProjectName: AndroidVideoAdvance
 * @Package: com.royole.ffmpegapp
 * @ClassName: YeFFmpeg
 * @Description: java类作用描述
 * @Author: leafye
 * @CreateDate: 2020/2/21 15:14
 * @UpdateUser:
 * @UpdateDate: 2020/2/21 15:14
 * @UpdateRemark:
 */
public class YeFFmpeg {

    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("yeffmpeg");
    }

    public static final int RESULT_OK = 0;

    private volatile static YeFFmpeg yeFFmpeg;

    public static YeFFmpeg instance() {
        if (yeFFmpeg == null) {
            synchronized (YeFFmpeg.class) {
                if (yeFFmpeg == null) {
                    yeFFmpeg = new YeFFmpeg();
                }
            }
        }
        return yeFFmpeg;
    }

    public native void decode(String input, String output);

    public native String avfilterinfo();

    public native String avcodecinfo();

    public native String avformatinfo();

    public native String urlprotocolinfo();

    public native static int handle(String[] cmd);

    public native int ffmpegPlay(String input);

    /**
     * 创建一个AudioTrack对象
     * @param sampleRate 采样率
     * @param channels 声道布局
     * @return AudioTrack
     */
    public AudioTrack createAudioTrack(int sampleRate, int channels){
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int channelConfig;
        if(channels == 1){
            channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        }else if(channels == 2){
            channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
        }else{
            channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
        }
        int bufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        return new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelConfig, audioFormat,
                bufferSizeInBytes, AudioTrack.MODE_STREAM);
    }


}
