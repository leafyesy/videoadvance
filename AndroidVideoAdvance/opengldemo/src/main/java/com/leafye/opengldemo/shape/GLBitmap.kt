package com.leafye.opengldemo.shape

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import com.leafye.opengldemo.R
import com.leafye.opengldemo.utils.OpenGLUtils
import java.nio.FloatBuffer

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.opengldemo.shape
 * @ClassName:      GLBitmap
 * @Description:     java类作用描述
 * @Author:         leafye
 * @CreateDate:     2020/1/21 16:29
 * @UpdateUser:
 * @UpdateDate:     2020/1/21 16:29
 * @UpdateRemark:
 */
class GLBitmap : BaseShape() {

    private val mVertexBuffer: FloatBuffer by lazy {
        OpenGLUtils.floatBufferUtil(VERTEX)
    }

    private val mTextureBuffer: FloatBuffer by lazy {
        OpenGLUtils.floatBufferUtil(TEXTURE)
    }
    private var mMvpMatrix = FloatArray(16)
    private var mMvpMatrixHandle: Int = 0
    private var mPositionHandle: Int = 0
    private var texUnitHandle: Int = 0
    private var mTexCoordHandle: Int = 0
    private var mBitmapWidth: Int = 0
    private var mBitmapHeight: Int = 0

    companion object {

        private const val COORDS_PER_TEXTURE = 2
        private const val COORDS_PER_VERTEX = 2

        private val vertexMatrixShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 aPosition;" +
                    "attribute vec2 aTexCoord;" +
                    "varying vec2 vTexCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * aPosition;" +
                    "  vTexCoord = aTexCoord;" +
                    "}"

        private val fragmentShaderCode = (
                "precision mediump float;" +
                        "uniform sampler2D uTextureUnit;" +
                        "varying vec2 vTexCoord;" +
                        "void main() {" +
                        "  gl_FragColor = texture2D(uTextureUnit, vTexCoord);" +
                        "}")

        private val VERTEX = floatArrayOf(
            1f, 1f, // top right
            -1f, 1f, // top left
            1f, -1f, // bottom right
            -1f, -1f// bottom left
        )
        private val TEXTURE = floatArrayOf(
            1f, 0f, // top right
            0f, 0f, // top left
            1f, 1f, // bottom right
            0f, 1f// bottom left
        )
    }

    override fun initOpenGLParam(): MutableList<Int> {
        return mutableListOf(
            OpenGLUtils.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexMatrixShaderCode
            ),
            OpenGLUtils.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode
            )
        )
    }

    override fun initData() {
        super.initData()
        mPositionHandle = GLES20.glGetAttribLocation(program, "aPosition")
        mMvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")
        mTexCoordHandle = GLES20.glGetAttribLocation(program, "aTexCoord")
        texUnitHandle = GLES20.glGetAttribLocation(program, "uTextureUnit")
    }

    fun loadGLTexture(context: Context): Int {
        // 加载图片并且保存在 OpenGL 纹理系统中
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher)
        mBitmapWidth = bitmap.width
        mBitmapHeight = bitmap.height
        val imageTexture = OpenGLUtils.createImageTexture(bitmap)
        // 激活纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        // 绑定纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, imageTexture)
        // 把选定的纹理单元传给片段着色器
        GLES20.glUniform1i(texUnitHandle, 0)
        return 0
    }

    fun changeMvpMatrixInside(width: Int, height: Int) {
        mMvpMatrix = OpenGLUtils.changeMvpMatrixInside(
            width.toFloat(),
            height.toFloat(),
            mBitmapWidth.toFloat(),
            mBitmapHeight.toFloat()
        )
    }

    fun draw() {
        GLES20.glUseProgram(program)
        GLES20.glUniformMatrix4fv(mMvpMatrixHandle, 1, false, mMvpMatrix, 0)
        // 传入顶点坐标
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        GLES20.glVertexAttribPointer(
            mPositionHandle,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            0,
            mVertexBuffer
        );
        // 传入纹理坐标
        GLES20.glEnableVertexAttribArray(mTexCoordHandle)
        GLES20.glVertexAttribPointer(
            mTexCoordHandle,
            COORDS_PER_TEXTURE,
            GLES20.GL_FLOAT,
            false,
            0,
            mTextureBuffer
        )
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, VERTEX.size / COORDS_PER_VERTEX)
        GLES20.glDisableVertexAttribArray(mPositionHandle)
        GLES20.glDisableVertexAttribArray(mTexCoordHandle)
        GLES20.glUseProgram(0)
    }


}