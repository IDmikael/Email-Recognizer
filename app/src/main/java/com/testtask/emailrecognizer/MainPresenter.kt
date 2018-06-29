package com.testtask.emailrecognizer

import android.content.Context
import android.util.Log
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import java.util.regex.Pattern
import javax.inject.Inject

class MainPresenter
@Inject
constructor(val view:MainModel.View?) : MainModel.Presenter {

    companion object {
        private val TAG = MainPresenter::class.java.simpleName
    }

    override fun initializeCameraSource(context: Context) {
        val textRecognizer = TextRecognizer.Builder(context).build()

        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {
            view?.initializeCameraSource(textRecognizer)
            Log.i(TAG, "Initializing camera source")
        }

        //Set the TextRecognizer's Processor.
        textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
            override fun release() {}

            /**
             * Detect all the text from camera using TextBlock and the values into a stringBuilder
             * which will then be set to the textView.
             */
            override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
                val items = detections.detectedItems
                if (items.size() != 0) {

                    val stringBuilder = StringBuilder()
                    for (i in 0 until items.size()) {
                        val item = items.valueAt(i)
                        stringBuilder.append(item.value)
                        stringBuilder.append("\n")
                    }

                    Log.i(TAG, "Founded text: $stringBuilder")

                    view?.showFoundedEmails(findEmails(stringBuilder.toString()))
                }
            }
        })

    }

    // Method to find emails in gotten string
    private fun findEmails(text:String) : String{
        val stringBuilder = StringBuilder()
        val m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(text)
        while (m.find()) {
            stringBuilder.append(m.group())
            stringBuilder.append(", ")
        }

        if (stringBuilder.isNotEmpty())
            view?.playSound()

        return stringBuilder.toString()
    }

}