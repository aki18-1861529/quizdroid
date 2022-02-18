package edu.us.ischool.quizdroid

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.lang.reflect.Type
import java.net.URL
import java.lang.Exception


class MemoryTopicRepository(c : Context) : TopicRepository {
    private val context : Context = c
    private lateinit var topics : Array<Topic>
    private val sharedPreference : SharedPreferences =  c.applicationContext.getSharedPreferences("DOWNLOAD_PREFERENCE", MODE_PRIVATE)
    private var url = "https://tednewardsandbox.site44.com/questions.json"
    private var resultString = ""
    private lateinit var data : Array<Topic>

    override val topicType: Type
        get() = object : TypeToken<Array<Topic>>() {}.type

    private suspend fun getData() = withContext(Dispatchers.IO) {

        if (sharedPreference.getString("URL", "https://tednewardsandbox.site44.com/questions.json") != "") {
            url = sharedPreference.getString("URL", "https://tednewardsandbox.site44.com/questions.json") as String
        }
        try {
            val path : String = context.filesDir.toString() + "/questions.json"
            val bufferedReader : BufferedReader = File(path).bufferedReader()
            resultString = bufferedReader.use { it.readText() }
            data = Gson().fromJson(resultString, topicType)
        } catch (e: Exception) {
            try {
                resultString = URL(url).readText()
                var editor = sharedPreference.edit()
                editor.putString("URL", url)
                data = Gson().fromJson(resultString, topicType)
            } catch (e2: Exception) {
                Log.e("QuizApp", e.toString())
                val intent = Intent(context, DownloadFailedDialog::class.java)
                context?.startActivity(intent)
            }
        }
    }

    override suspend fun getAllTopics() : Array<Topic> = withContext(Dispatchers.IO) {
        // data.forEachIndexed { idx, data -> Log.i("Topic", "> Item $idx:\n${data.title}") }
        async {
            getData()
            val topicArr: MutableList<Topic> = mutableListOf()
            for (i in data.indices) {
                val questions = data[i].questions.map { Quiz(it.text, it.answer, it.answers) }
                topicArr.add(Topic(data[i].title, data[i].desc, questions.toTypedArray()))
            }
            topics = topicArr.toTypedArray()
            Log.i("Topics", topicArr.toTypedArray().toString())
            return@async topics
        }.await()
    }

    override suspend fun getTopic(i : Int) : Topic = withContext(Dispatchers.IO) {
        async {
            getData()
            return@async data[i]
        }.await()
    }

    override suspend fun getAllQuizzes(i: Int): Array<Quiz> = withContext(Dispatchers.IO) {
        async {
            getData()
            val questions = data[i].questions.map { Quiz(it.text, it.answer, it.answers) }
            return@async questions.toTypedArray()
        }.await()
    }

    override suspend fun getQuiz(q: Int, i : Int) : Quiz = withContext(Dispatchers.IO) {
        async {
            getData()
            val questions = data[q].questions.map { Quiz(it.text, it.answer, it.answers) }
            return@async questions[i]
        }.await()
    }

    override suspend fun addTopic(t: Topic): Array<Topic>? {
        return null
    }

    override suspend fun addQuiz(q: Quiz): Array<Question>? {
        return null
    }

    override suspend fun deleteTopic(i: Int): Topic? {
        return null
    }

    override suspend fun deleteQuiz(i: Int): Quiz? {
        return null
    }

    override suspend fun updateTopic(t: Topic): Topic? {
        return null
    }

    override suspend fun updateQuiz(q: Quiz): Quiz? {
        return null
    }
}
