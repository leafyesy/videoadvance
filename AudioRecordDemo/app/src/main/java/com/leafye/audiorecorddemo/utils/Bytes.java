package com.leafye.audiorecorddemo.utils;

import java.util.Arrays;

public class Bytes{
    /**
     * 字节数组的容量
     */
    int mCap = 0;
    /**
     * 实际字节数组容器
     */
    byte[] mBytes;

    /**
     * 通过设定字节数组容量来构建对象
     * @param cap   int 字节数组容量
     */
    public Bytes(int cap) {
        mBytes = new byte[cap];
        mCap = cap;
    }

    /**
     * 默认构建字节数组为容量为4
     */
    public Bytes() {
        mBytes = new byte[4];
        mCap = 4;
    }

    /**
     * 通过已有byte[]进行构建对象，同时设定容器容量
     * @param mBytes    byte[]
     */
    public Bytes(byte[] mBytes) {
        this.mBytes = mBytes;
        mCap = mBytes.length;
    }

    /**
     * 通过已有int[]进行构建对象，同时设定容器容量，会把int型强制转换为byte型，超过范围都按0xff设定
     * @param mInts int[]
     */
    public Bytes(int[] mInts) {
        mCap = mInts.length;
        mBytes = new byte[mCap];
        for (int i = 0; i < mCap; i++) {
            if (mInts[i] > 255) {
                this.mBytes[i] = (byte) 255;
            } else {
                this.mBytes[i] = (byte) (mInts[i] & 0xff);
            }
        }
    }

    /**
     * 从容器中指定偏移位置读取16位的无符号整形，大端模式
     * @param offset    int 容器偏移量
     * @return          int 返回16位无符号整形，大端模式
     */
    public int readUInt16BE(int offset) {
        int res = 0;
        if (mBytes != null && (mCap-offset) >= 2) {
            res = ((mBytes[offset] & 0xff) << 8) + (mBytes[offset + 1] & 0xff);
        }
        return res;
    }

    /**
     * 从容器起始位置读取16位无符号整形，大端模式
     * @return int  返回16位无符号整形，大端模式
     */
    public int readUInt16BE() {
        return readUInt16BE(0);
    }

    /**
     * 从容器中指定偏移位置读取16位的无符号整形，小端模式
     * @param offset    int 容器偏移量
     * @return          int 返回16位无符号整形，小端模式
     */
    public int readUInt16LE(int offset) {
        int res = 0;
        if (mBytes != null && (mCap-offset) >= 2) {
            res = ((mBytes[offset + 1] & 0xff) << 8) + (mBytes[offset] & 0xff);
        }
        return res;
    }

    /**
     * 从容器中指定偏移位置读取16位的无符号整形，小端模式
     * @return  int 返回16位无符号整形，小端模式
     */
    public int readUInt16LE() {
        return readUInt16LE(0);
    }

    /**
     * 从容器中指定偏移位置读取32位的无符号整形，大端模式
     * @param offset    int     容器偏移量
     * @return          long    返回32位无符号整形，大端模式
     */
    public long readUInt32BE(int offset) {
        long res = 0;
        if (mBytes != null && (mCap-offset) >= 4) {
            res = ((mBytes[offset] & 255L) << 24) + ((mBytes[offset + 1] & 255L) << 16) + ((mBytes[offset + 2] & 0x255L) << 8) + (mBytes[offset + 3] & 0x255L);
        }
        return res;
    }

    /**
     * 从容器中指定偏移位置读取32位的无符号整形，大端模式
     * @return  long    返回32位无符号整形，大端模式
     */
    public long readUInt32BE() {
        return readUInt32BE(0);
    }

    /**
     * 从容器中指定偏移位置读取32位的无符号整形，小端模式
     * @param offset    int     容器偏移量
     * @return          long    返回32位无符号整形，小端模式
     */
    public long readUInt32LE(int offset) {
        long res = 0;
        if (mBytes != null && (mCap-offset) >= 4) {
            res = ((mBytes[offset + 3] & 255L) << 24) + ((mBytes[offset + 2] & 255L) << 16) + ((mBytes[offset + 1] & 0x255L) << 8) + (mBytes[offset] & 0x255L);
        }
        return res;
    }

    /**
     * 从容器中指定偏移位置读取32位的无符号整形，小端模式
     * @return  long    返回32位无符号整形，小端模式
     */
    public long readUInt32LE() {
        return readUInt32LE(0);
    }

    /**
     * 从容器中指定偏移位置读取16位的有符号整形，大端模式
     * @param offset    int 容器偏移量
     * @return          int 返回16位有符号整形，大端模式
     */
    public int readInt16BE(int offset) {
        int res = 0;
        if (mBytes != null && (mCap-offset) >= 2) {
            res = (mBytes[offset] << 8) + mBytes[offset+1];
        }
        return res;
    }

    /**
     * 从容器中指定偏移位置读取16位的有符号整形，大端模式
     * @return  int 返回16位有符号整形，大端模式
     */
    public int readInt16BE(){
        return readInt16BE(0);
    }

    /**
     * 从容器中指定偏移位置读取16位的有符号整形，小端模式
     * @param offset    int 容器偏移量
     * @return          int 返回16位有符号整形，小端模式
     */
    public int readInt16LE(int offset) {
        int res = 0;
        if (mBytes != null && (mCap-offset) >= 2) {
            res = (mBytes[offset+1] << 8) + mBytes[offset];
        }
        return res;
    }

    /**
     * 从容器中指定偏移位置读取16位的有符号整形，小端模式
     * @return  int 返回16位有符号整形，小端模式
     */
    public int readInt16LE(){
        return readInt16LE(0);
    }

    /**
     * 从容器中指定偏移位置读取32位的有符号整形，大端模式
     * @param offset    int     容器偏移量
     * @return          long    返回32位有符号整形，大端模式
     */
    public long readInt32BE(int offset) {
        long res =0;
        if (mBytes != null && (mCap-offset) >= 4) {
            res = (mBytes[offset] << 24) + (mBytes[offset+1] << 16) + (mBytes[offset+2] << 8) + mBytes[offset+3];
        }
        return res;
    }

    /**
     * 从容器中指定偏移位置读取32位的有符号整形，大端模式
     * @return  long    返回32位有符号整形，大端模式
     */
    public long readInt32BE(){
        return readInt32BE(0);
    }

    /**
     * 从容器中指定偏移位置读取32位的有符号整形，小端模式
     * @param offset    int     容器偏移量
     * @return          long    返回32位有符号整形，小端模式
     */
    public long readInt32LE(int offset) {
        long res =0;
        if (mBytes != null && (mCap-offset) >= 4) {
            res = (mBytes[offset+3] << 24) + (mBytes[offset+2] << 16) + (mBytes[offset+1] << 8) + mBytes[offset];
        }
        return res;
    }

    /**
     * 从容器中指定偏移位置读取32位的有符号整形，小端模式
     * @return  long 返回32位有符号整形，小端模式
     */
    public long readInt32LE(){
        return readInt32LE(0);
    }

    /**
     * 获得容器容量
     * @return  int 容器容量
     */
    public int length() {
        return mCap;
    }

    /**
     * 连接另一个byte[],可设定来源数据的起始位置，以及长度，也可设定目标数据的连接起始位置
     * @param offsetT   int     目标数据的连接起始位置
     * @param b         byte[]  来源数据
     * @param offset    int     来源数据起始位置
     * @param length    int     来与数据长度
     */
    public void cat(int offsetT, byte[] b, int offset, int length) {
        byte[] tmp = mBytes;
        mCap = offsetT + length;
        mBytes = new byte[mCap];
        for (int i = 0; i < offsetT; i++) {
            mBytes[i] = tmp[i];
        }
        for (int i = 0; i < length; i++) {
            mBytes[i + offsetT] = b[i + offset];
        }
    }

    /**
     * 连接另一个byte[],可设定来源数据的起始位置，以及长度,连接到目标数据的尾部
     * @param b         byte[]  来源数据
     * @param offset    int     来源数据起始位置
     * @param length    int     来与数据长度
     */
    public void cat(byte[] b, int offset, int length) {
        cat(mCap, b, offset, length);
    }

    /**
     * 连接另一个byte[],可设定来源数据的起始位置到结尾，连接到目标数据的尾部
     * @param b         byte[]  来源数据
     * @param offset    int     来源数据起始位置
     */
    public void cat(byte[] b, int offset) {
        cat(b, offset, b.length - offset);
    }

    /**
     * 连接另一个byte[],来源数据全部连接到目标数据的尾部
     * @param b byte[]  来源数据
     */
    public void cat(byte[] b) {
        cat(b, 0);
    }

    /**
     * 获得实际容器的byte[]数组
     * @return  byte[]
     */
    public byte[] toBytes() {
        return mBytes;
    }

    /**
     * 输出对象全部属性
     * @return  String
     */
    @Override
    public String toString() {
        return "Bytes{" +
                "length=" + mCap +
                ", data=" + Arrays.toString(mBytes) +
                '}';
    }

    /**
     * 输出16进制的数据，每个数据前可增加前缀header，数据之间可增加间隔space
     * @param header    String  前缀
     * @param space     String  间隔
     * @return          String
     */
    public String toHexString(String header,String space){
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<mCap;i++){
            buffer.append(String.format("%s%02x%s",header,mBytes[i],space));
        }
        return buffer.substring(0,buffer.length()-space.length());
    }

    /**
     * 输出16进制的数据，数据之间可增加间隔space
     * @param space     String  间隔
     * @return          String
     */
    public String toHexString(String space){
        return toHexString("",space);
    }

    /**
     * 输出16进制的数据
     * @return  String
     */
    public String toHexString(){
        return toHexString("");
    }
}

