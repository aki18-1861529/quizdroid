package edu.us.ischool.quizdroid

import android.app.Application
import android.util.Log

class QuizApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i("QuizApp", "onCreate loaded and running")
    }

    fun getTopicRepository(): TopicRepository {
        return MemoryTopicRepository()
    }
}