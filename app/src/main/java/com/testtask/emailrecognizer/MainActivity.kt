package com.testtask.emailrecognizer

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.text.TextRecognizer
import com.testtask.emailrecognizer.dagger.DaggerMainComponent
import com.testtask.emailrecognizer.dagger.MainComponent
import com.testtask.emailrecognizer.dagger.MainModule
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainModel.View {

    @Inject
    lateinit var presenter: Lazy<MainPresenter>
    var mCameraSource: CameraSource ?= null
    lateinit var mp:MediaPlayer

    val component: MainComponent by lazy {
        DaggerMainComponent.builder()
                .mainModule(MainModule(this))
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)
        presenter.get().initializeCameraSource(this@MainActivity)
        mp = MediaPlayer.create(this@MainActivity, R.raw.tada)
    }

    override fun initializeCameraSource(textRecognizer:TextRecognizer) {

        //Initialize camerasource to use high resolution and set Autofocus on.
        mCameraSource = CameraSource.Builder(this@MainActivity, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setAutoFocusEnabled(true)
                .setRequestedFps(2.0f)
                .build()

        /**
         * Add call back to SurfaceView and check if camera permission is granted.
         * If permission is granted we can start our cameraSource and pass it to surfaceView
         */
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {

                    if (ActivityCompat.checkSelfPermission(this@MainActivity,
                                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(this@MainActivity,
                                arrayOf(Manifest.permission.CAMERA), 1)
                        return
                    }

                    mCameraSource?.start(surfaceView.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            /**
             * Release resources for cameraSource
             */
            override fun surfaceDestroyed(holder: SurfaceHolder) {
                mCameraSource?.stop()
            }
        })

    }

    override fun showFoundedEmails(emails:String) {
        text_view.post {
            if (emails.isEmpty())
                text_view.text = getString(R.string.empty_text_placeholder)
            else
                text_view.text = emails
        }
    }

    override fun playSound() {
        val random = Random()

        if (!mp.isPlaying) {
            if (random.nextInt((1 - 0) + 1) + 0 == 0) {
                mp = MediaPlayer.create(this, R.raw.tada)
                mp.start()
            } else {
                mp = MediaPlayer.create(this, R.raw.toasty)
                mp.start()
            }
        }
    }
}
