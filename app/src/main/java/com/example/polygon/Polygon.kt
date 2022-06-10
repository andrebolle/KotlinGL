package com.example.polygon

import android.opengl.GLES20
import android.os.SystemClock
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random



//var triangleCoordinates = floatArrayOf(     // in counterclockwise order:
//    0.0f, 0.622008459f, 0.0f,      // top
//    -0.5f, -0.311004243f, 0.0f,    // bottom left
//    0.5f, -0.311004243f, 0.0f      // bottom right
//)


// class Polygon (val x:Float, val y:Float, val radius:Float, val sides:Int ){
class Polygon {
    private val dim = 3
    // private val TAG = "Polygon"
    // Set color with red, green, blue and alpha (opacity) values
    // val vertices = Array(9) { Random.nextFloat()}
//    private val vertices = FloatArray(9*3)

    private var mProgram: Int

    private val vertexShaderCode =
//        "in vec4 vPosition;" +
        "attribute vec4 vPosition;" +
//        "layout (location = 0) in vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = vPosition;" +
                "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"


    init {



        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)
        }
    }



    // attribute and varying are deprecated since glsl 1.3 and were removed in glsl 1.4:
    // Use of the keywords attribute and varying. Use in and out instead.


    private fun loadShader(type: Int, shaderCode: String): Int {

        // create a vertex shader type (GL_VERTEX_SHADER)
        // or a fragment shader type (GL_FRAGMENT_SHADER)
        return GLES20.glCreateShader(type).also { shader ->

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }
    
    fun drawTriangle() {
        val vertices = FloatArray(9*1)

        for (index in vertices.indices) {
            //code
            vertices[index] = Random.nextFloat() * 2.0f - 1.0f
        }

        val vertexBuffer: FloatBuffer =
            // (number of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(vertices.size * 4).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(vertices)
                    // set the buffer to read the first coordinate
                    position(0)
                }
            }

        val vertexCount: Int = vertices.size / dim
        val vertexStride: Int = dim * 4 // 4 bytes per vertex
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        // ------------ Position Attribute ------------------
        val positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(
            positionHandle,
            dim,
            GLES20.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )


        // get handle to fragment shader's vColor member
        GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->
            //val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)
            val color = floatArrayOf(Random.nextFloat(),Random.nextFloat(),Random.nextFloat(), 1.0f)

            // Set color for drawing the triangle
            GLES20.glUniform4fv(colorHandle, 1, color, 0)
        }

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        GLES20.glDisableVertexAttribArray(positionHandle)

        SystemClock.sleep(30)

    }

    fun draw() {
        val vertices = polygon((Random.nextFloat() - 0.5f) * 2f,(Random.nextFloat() - 0.5f) * 2f, Random.nextFloat() / 4f, Random.nextInt(3,7))

        val vertexBuffer: FloatBuffer =
            // (number of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(vertices.size * 4).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(vertices)
                    // set the buffer to read the first coordinate
                    position(0)
                }
            }

        val vertexCount: Int = vertices.size / dim
        val vertexStride: Int = dim * 4 // 4 bytes per vertex
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        // ------------ Position Attribute ------------------
        val positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(
            positionHandle,
            dim,
            GLES20.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )


        // get handle to fragment shader's vColor member
        GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->
            //val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)
            val color = floatArrayOf(Random.nextFloat(),Random.nextFloat(),Random.nextFloat(), 1.0f)

            // Set color for drawing the triangle
            GLES20.glUniform4fv(colorHandle, 1, color, 0)
        }

        // Draw the triangle
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount)
        GLES20.glDisableVertexAttribArray(positionHandle)

        SystemClock.sleep(30)

    }

    private fun polygon(x: Float, y: Float, radius: Float, sides: Int): FloatArray {
//        N = 5 'No. of sides, try to increase its value to 6,7,10,etc
//        _glBegin _GL_TRIANGLE_FAN
//                FOR i = 0 TO _PI(2) STEP _PI(2) / N
//        _glVertex2f COS(i) * .5, SIN(i) * .5
//        NEXT
//        _glEnd
        // Use a _GL_TRIANGLE_FAN to do polygons
        val vertices = FloatArray(sides * 3)

        for (i in 1..sides) {
            vertices[(i-1)*3] = (cos((i-1) * 2.0f * PI / sides) * radius).toFloat() + x
            vertices[(i-1)*3+1] = (sin((i-1) * 2.0f * PI / sides) * radius).toFloat() + y
            vertices[(i-1)*3+2] = 0f
        }

        return vertices
    }
}
