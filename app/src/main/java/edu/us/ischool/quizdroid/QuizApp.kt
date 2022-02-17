package edu.us.ischool.quizdroid

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log

class QuizApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val prefs : SharedPreferences = getSharedPreferences("QuizApp", MODE_PRIVATE)

        Log.i("QuizApp", "onCreate loaded and running")
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Environment","Permission is granted");
        }
    }

    fun getTopicRepository(c: Context): TopicRepository {
        return MemoryTopicRepository(c)
    }
}