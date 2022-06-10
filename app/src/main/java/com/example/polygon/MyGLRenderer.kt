package com.example.polygon

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer : GLSurfaceView.Renderer {

    private lateinit var mShape: Polygon

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    private fun glInfo() {

//        OpenGL Version	GLSL Version
//        2.0	1.10
//        2.1	1.20
//        3.0	1.30
//        3.1	1.40
//        3.2	1.50
        Log.i("MyGLRenderer", "*********************************************")
        Log.i("MyGLRenderer", "GL_VENDOR:" + GLES20.glGetString(GLES20.GL_VENDOR))
        Log.i("MyGLRenderer", "GL_RENDERER:" + GLES20.glGetString(GLES20.GL_RENDERER))
        Log.i("MyGLRenderer", "GL_VERSION:" + GLES20.glGetString(GLES20.GL_VERSION))
        Log.i("MyGLRenderer", "GL_SHADING_LANGUAGE_VERSION:" + GLES20.glGetString(GLES20.GL_SHADING_LANGUAGE_VERSION))
        val count = IntArray(1)
        GLES20.glGetIntegerv(GLES20.GL_MAX_VERTEX_ATTRIBS, count, 0)
        Log.i("MyGLRenderer", "${count[0]}")
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {

        glInfo()

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f)

        // initialize a triangle
        // mShape = Polygon(0.0f,0.0f,1.0f,3)
        mShape = Polygon()
    }

    // Draw every frame
    override fun onDrawFrame(unused: GL10) {
        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Draw shape
        //mTriangle.draw(vPMatrix)
        mShape.draw()
    }


    // Reset the viewport if the screen size changes, e.g. if it rotates.
    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }
}
