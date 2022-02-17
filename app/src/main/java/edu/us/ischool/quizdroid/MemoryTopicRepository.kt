package edu.us.ischool.quizdroid

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.lang.reflect.Type
import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import kotlin.concurrent.thread


class MemoryTopicRepository(c : Context) : TopicRepository {
    lateinit var topics : Array<Topic>
    private val sharedPreference: SharedPreferences =  c.applicationContext.getSharedPreferences("DOWNLOAD_PREFERENCE", MODE_PRIVATE)
    private var url = "https://tednewardsandbox.site44.com/questions.json"
    var resultString = ""
    lateinit var data : Array<Topic>

    init {
        if (sharedPreference.getString("URL", "") != "") {
            url = sharedPreference.getString("URL", "") as String
        }
//        GlobalScope.launch {
            try {
                resultString = URL(url).readText()
            } catch (e: Exception) {
                Log.e("QuizApp", e.toString())
            }
            var editor = sharedPreference.edit()
            editor.putString("URL", url)
//        }
        data = Gson().fromJson(resultString, topicType)
    }

    override val topicType: Type
        get() = object : TypeToken<Array<Topic>>() {}.type

//    override val data: Array<Topic>
//        get() = Gson().fromJson(resultString, topicType)

    override fun getAllTopics() : Array<Topic> {
        // data.forEachIndexed { idx, data -> Log.i("Topic", "> Item $idx:\n${data.title}") }
        val topicArr : MutableList<Topic> = mutableListOf()
        for (i in data.indices) {
            val questions = data[i].questions.map { Quiz(it.text, it.answer, it.answers) }
            topicArr.add(Topic(data[i].title, data[i].desc, questions.toTypedArray()))
        }
        topics = topicArr.toTypedArray()
        Log.i("Topics", topicArr.toTypedArray().toString())
        return topics
    }

    override fun getTopic(i : Int) : Topic {
        return data[i]
    }

    override fun getAllQuizzes(i: Int): Array<Quiz> {
        val questions = data[i].questions.map { Quiz(it.text, it.answer, it.answers) }
        return questions.toTypedArray()
    }

    override fun getQuiz(q: Int, i : Int) : Quiz {
        val questions = data[q].questions.map { Quiz(it.text, it.answer, it.answers) }
        return questions[i]
    }

    override fun addTopic(t: Topic): Array<Topic>? {
        return null
    }

    override fun addQuiz(q: Quiz): Array<Question>? {
        return null
    }

    override fun deleteTopic(i: Int): Topic? {
        return null
    }

    override fun deleteQuiz(i: Int): Quiz? {
        return null
    }

    override fun updateTopic(t: Topic): Topic? {
        return null
    }

    override fun updateQuiz(q: Quiz): Quiz? {
        return null
    }
}
