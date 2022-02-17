package edu.us.ischool.quizdroid

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type
import java.net.URL
import java.lang.Exception


class MemoryTopicRepository(c : Context) : TopicRepository {
    lateinit var topics : Array<Topic>
    private val sharedPreference: SharedPreferences =  c.applicationContext.getSharedPreferences("DOWNLOAD_PREFERENCE", MODE_PRIVATE)
    private var url = "https://tednewardsandbox.site44.com/questions.json"
    var resultString = ""

//    init {
//        if (sharedPreference.getString("URL", "") != "") {
//            url = sharedPreference.getString("URL", "") as String
//        }
//        try {
//            resultString = URL(url).readText()
//        } catch (e: Exception) {
//            Log.e("QuizApp", e.toString())
//        }
//        var editor = sharedPreference.edit()
//        editor.putString("URL", url)
//    }

    override val externalFile : String
        get() = File("/data/local/tmp/questions.json").readText()

    override val topicType: Type
        get() = object : TypeToken<Array<Topic>>() {}.type

    override val data: Array<Topic>
        get() = Gson().fromJson(externalFile, topicType)

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
