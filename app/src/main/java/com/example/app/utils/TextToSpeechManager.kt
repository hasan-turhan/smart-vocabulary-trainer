package com.example.app.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TextToSpeechManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private var isReady = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isReady = true
        }
    }

    fun speakGerman(text: String) {
        if (!isReady) return
        tts?.language = Locale.GERMANY
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "DE")
    }

    fun speakEnglishUS(text: String) {
        if (!isReady) return
        tts?.language = Locale.US
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "EN_US")
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
