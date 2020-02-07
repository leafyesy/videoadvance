package com.leafye.opengldemo.recordCamera.camera

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.util.Log
import com.leafye.opengldemo.recordCamera.camera.cameraImp.CameraV1GLSurfaceView
import com.leafye.opengldemo.utils.OpenGLUtils
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 * Created by leafye on 2020-02-05.
 */
class CameraV1GLRenderer(
    private val context: Context
) : GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "CameraV1GLRenderer"
    }

    private val mOESTextureId by lazy {
        OpenGLUtils.createOESTextureObject()
    }
    private var mSurfaceTexture: SurfaceTexture? = null
    private val transformMatrix = FloatArray(16)
    private var mGLSurfaceView: CameraV1GLSurfaceView? = null
    private var mCamera: ICamera? = null
    private var bIsPreviewStarted: Boolean = false
    private val mFilterEngine: FilterEngine by lazy {
        FilterEngine(mOESTextureId, context)
    }
    private val mDataBuffer: FloatBuffer by lazy {
        mFilterEngine.buffer
    }
    private val mShaderProgram by lazy {
        mFilterEngine.shaderProgram
    }
    private var aPositionLocation = -1
    private var aTextureCoordLocation = -1
    private var uTextureMatrixLocation = -1
    private var uTextureSamplerLocation = -1
    private val mFBOIds = IntArray(1)

    fun init(
        glSurfaceView: CameraV1GLSurfaceView,
        camera: ICamera,
        isPreviewStarted: Boolean
    ) {
        mGLSurfaceView = glSurfaceView
        mCamera = camera
        bIsPreviewStarted = isPreviewStarted
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        glGenFramebuffers(1, mFBOIds, 0)
        glBindFramebuffer(GL_FRAMEBUFFER, mFBOIds[0])
        Log.i(TAG, "onSurfaceCreated: mFBOId: " + mFBOIds[0])
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f)
        initSurfaceTexture()
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        val t1 = System.currentTimeMillis()
        if (mSurfaceTexture == null) {
            initSurfaceTexture()
        }
        mSurfaceTexture?.let {
            it.updateTexImage()
            it.getTransformMatrix(transformMatrix)
        }
        //glClear(GL_COLOR_BUFFER_BIT)
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        aPositionLocation = glGetAttribLocation(mShaderProgram, FilterEngine.POSITION_ATTRIBUTE)
        aTextureCoordLocation =
            glGetAttribLocation(mShaderProgram, FilterEngine.TEXTURE_COORD_ATTRIBUTE)
        uTextureMatrixLocation =
            glGetUniformLocation(mShaderProgram, FilterEngine.TEXTURE_MATRIX_UNIFORM)
        uTextureSamplerLocation =
            glGetUniformLocation(mShaderProgram, FilterEngine.TEXTURE_SAMPLER_UNIFORM)

        glActiveTexture(GLES20.GL_TEXTURE0)
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mOESTextureId)
        glUniform1i(uTextureSamplerLocation, 0)
        glUniformMatrix4fv(uTextureMatrixLocation, 1, false, transformMatrix, 0)

        mDataBuffer.position(0)
        glEnableVertexAttribArray(aPositionLocation)
        glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 16, mDataBuffer)

        mDataBuffer.position(2)
        glEnableVertexAttribArray(aTextureCoordLocation)
        glVertexAttribPointer(aTextureCoordLocation, 2, GL_FLOAT, false, 16, mDataBuffer)

        //glDrawElements(GL_TRIANGLE_FAN, 6,GL_UNSIGNED_INT, 0);
        //glDrawArrays(GL_TRIANGLE_FAN, 0 , 6);
        glDrawArrays(GL_TRIANGLES, 0, 6)
        //glDrawArrays(GL_TRIANGLES, 3, 3);
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        val t2 = System.currentTimeMillis()
        val t = t2 - t1
        Log.i(TAG, "onDrawFrame: time: $t")
    }

    fun initSurfaceTexture(): Boolean {
        if (mCamera == null || mGLSurfaceView == null) {
            Log.w(TAG, "mCamera:$mCamera  or mGLSurfaceView:$mGLSurfaceView!")
            return false
        }
        //处理结果
        val iCamera = mCamera!!
        val glSurfaceView = mGLSurfaceView!!
        Log.w(TAG, "init surface texture!!!")
        mSurfaceTexture = SurfaceTexture(mOESTextureId)
            .apply {
                setOnFrameAvailableListener {
                    Log.d(TAG, "setOnFrameAvailableListener->requestRender")
                    glSurfaceView.requestRender()
                }
                iCamera.setPreviewTexture(this)
            }
        return true
    }

    fun deinit() {
        mSurfaceTexture?.release()
        mSurfaceTexture = null
        mCamera = null
        bIsPreviewStarted = false
    }


}