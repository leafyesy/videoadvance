package com.leafye.androidvideoadvance.jni;

/**
 * Created by leafye on 2019-12-23.
 */
public class Mp3Encoder {

    public native int init(String pcmPath,int audioChannels,int bitRate,int sampleRate,String mp3Path);

    public native void encode();

    public native void destroy();

}
