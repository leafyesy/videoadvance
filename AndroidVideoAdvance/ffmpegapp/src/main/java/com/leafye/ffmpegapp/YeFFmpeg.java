package com.leafye.ffmpegapp;

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


}
