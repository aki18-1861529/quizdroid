package edu.us.ischool.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    private lateinit var arrayAdapter : ArrayAdapter<String>
    private val EXTRA_TOPIC = "edu.us.ischool.quizdroid.TOPIC"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val quizApp = QuizApp()
        val repo : TopicRepository = quizApp.getTopicRepository()
        val listView = findViewById<ListView>(R.id.listView)

        val allTopics = repo.getAllTopics()
        val topicTitles = allTopics.map { it.title }

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, topicTitles)
        listView.adapter = arrayAdapter
        listView.setOnItemClickListener { parent, view, pos, id ->

            val intent = Intent(this, TopicOverview::class.java).apply {
                putExtra(EXTRA_TOPIC, pos)
            }
            startActivity(intent)
        }


    }
}