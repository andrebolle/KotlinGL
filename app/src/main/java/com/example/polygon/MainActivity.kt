package com.example.polygon

import android.app.Activity
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}

class MainActivity : Activity() {

    private lateinit var gLView: GLSurfaceView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = MyGLSurfaceView(this)
        setContentView(gLView)
    }
}