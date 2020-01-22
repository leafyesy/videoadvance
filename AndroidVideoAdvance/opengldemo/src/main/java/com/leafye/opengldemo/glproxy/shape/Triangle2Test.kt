package com.leafye.opengldemo.glproxy.shape

import android.opengl.GLES20
import android.opengl.Matrix
import android.os.SystemClock
import com.leafye.opengldemo.glproxy.GLTest
import com.leafye.opengldemo.shape.Triangle2
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Triangle2Test : GLTest {
    private var triangle: Triangle2? = null

    private val mvpMatrix = FloatArray(16)
    private val projectMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)

    override fun onDrawFrameTest(gl: GL10?) {
        val scratch = FloatArray(16)
        val time = SystemClock.uptimeMillis() % 4000L
        val angle = 0.090f * time.toInt()
        Matrix.setRotateM(rotationMatrix, 0, angle, 0F, 0F, -1F)

        gl?.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        //观察角度的矩形
        //eye->代表相机在坐标轴的位置
        //center->代表物体的中心坐标
        //up->代表相机往哪边看
        Matrix.setLookAtM(
            viewMatrix, 0,
            0F, 0F, -3F,
            0f, 0f, 0f,
            -0f, 1.0f, 1.0f
        )
        //Matrix.setLookAtM(viewMatrix, 0, 0F, 0F, 3F, 0F, 0F, 0F, 0F, 0F, -1.0F)
        Matrix.multiplyMM(mvpMatrix, 0, projectMatrix, 0, viewMatrix, 0)
        Matrix.multiplyMM(scratch, 0, mvpMatrix, 0, rotationMatrix, 0)
        triangle?.draw(scratch)
    }

    override fun onSurfaceChangedTest(gl: GL10?, width: Int, height: Int) {
        gl?.glViewport(0, 0, width, height)
        val ratio = (width.toFloat() / height)
        //left right bottom top 影响图像左右和上下缩放比
        /*
        near和far需要结合拍摄相机即观察者眼睛的位置来设置，
        例如setLookAtM中设置cx = 0, cy = 0, cz = 10，near设置的范围需要是小于10才可以看得到绘制的图像，
        如果大于10，图像就会处于了观察这眼睛的后面，这样绘制的图像就会消失在镜头前，
        far参数，far参数影响的是立体图形的背面，far一定比near大，一般会设置得比较大，如果设置的比较小，
        一旦3D图形尺寸很大，这时候由于far太小，这个投影矩阵没法容纳图形全部的背面，这样3D图形的背面会有部分隐藏掉的。
         */
        Matrix.frustumM(
            projectMatrix,
            0,
            -ratio,
            ratio,
            -1F,
            1F,
            1F,
            3F
        )
    }

    override fun onSurfaceCreatedTest(gl: GL10?, config: EGLConfig?) {
        gl?.glClearColor(.0F, 1.0F, .0F, .0F)
        triangle = Triangle2()
    }
}