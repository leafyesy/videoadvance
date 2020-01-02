package com.leafye.audiorecorddemo.linxi.handler

/**
 * 传入处理的数据 I
 * 返回处理结束的数 T
 * 如果需要处理拦截就表示返回值不为null
 */
abstract class AbstractHandler<I, T> {

    abstract fun handler(input: I): T?

}