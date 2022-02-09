package edu.us.ischool.quizdroid

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var arrayAdapter : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)

        val quizApp = QuizApp()
        val repo : TopicRepository = quizApp.getTopicRepository()

        val allTopics = repo.getAllTopics()
        val allTitles = arrayOfNulls<String>(allTopics.size)
        for (i in allTitles.indices) {
            allTitles[i] = allTopics[i].title
        }

        val listView = findViewById<ListView>(R.id.listView)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, allTitles)
        listView.adapter = arrayAdapter
        listView.setOnItemClickListener { parent, view, pos, id ->

            val intent = Intent(this, TopicOverview::class.java).apply {
                putExtra("topic", pos)
            }
            startActivity(intent)
        }
    }
}