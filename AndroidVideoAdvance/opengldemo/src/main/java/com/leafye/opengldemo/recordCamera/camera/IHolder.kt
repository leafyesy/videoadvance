package com.leafye.opengldemo.recordCamera.camera

/**
 * Created by leafye on 2020-01-27.
 */
interface IHolder {

    fun create(holder: IHolder)

    fun change(holder: IHolder)

    fun destroy(holder: IHolder)


//    class Holder : SurfaceHolder.Callback2 {
//        override fun surfaceRedrawNeeded(p0: SurfaceHolder?) {
//
//        }
//
//        override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun surfaceDestroyed(p0: SurfaceHolder?) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun surfaceCreated(p0: SurfaceHolder?) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//    }
//
//    class Holder1 : TextureView.SurfaceTextureListener {
//        override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, p1: Int, p2: Int) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//
//    }

}