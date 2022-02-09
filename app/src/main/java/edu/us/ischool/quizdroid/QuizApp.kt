package edu.us.ischool.quizdroid

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log

class QuizApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i("QuizApp", "onCreate loaded and running")
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Environment","Permission is granted");
        }
    }

    fun getTopicRepository(): TopicRepository {
        return MemoryTopicRepository()
    }
}