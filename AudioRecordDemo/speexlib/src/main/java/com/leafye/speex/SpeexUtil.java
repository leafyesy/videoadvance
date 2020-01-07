package com.leafye.speex;

public class SpeexUtil {
    //压缩比为4
    private static final int DEFAULT_COMPRESSION = 4;

    static {
        try {
            System.loadLibrary("speex");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public SpeexUtil() {
        open(DEFAULT_COMPRESSION);
    }

    public native int open(int compression);

    public native int getFrameSize();

    public native int decode(byte[] encoded, short[] lin, int size);

    public native int encode(short[] lin, int offset, byte[] encode, int size);

    public native void close();
}
