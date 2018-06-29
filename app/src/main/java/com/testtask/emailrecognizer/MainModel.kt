package com.testtask.emailrecognizer

import android.content.Context
import com.google.android.gms.vision.text.TextRecognizer

interface MainModel {

    interface View{
        fun initializeCameraSource(textRecognizer: TextRecognizer)
        fun showFoundedEmails(emails:String)
        fun playSound()
    }

    interface Presenter{
        fun initializeCameraSource(context: Context)
    }
}