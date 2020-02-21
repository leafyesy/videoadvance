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

    public native void decode(String input, String output);

    public native String avfilterinfo();

    public native String avcodecinfo();

    public native String avformatinfo();

    public native String urlprotocolinfo();


}
