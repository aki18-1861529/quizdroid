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
    private val EXTRA_TOPIC = "edu.us.ischool.quizdroid.TOPIC"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)

        val quizApp = QuizApp()
        val repo : TopicRepository = quizApp.getTopicRepository()
        val listView = findViewById<ListView>(R.id.listView)

        val allTopics = repo.getAllTopics()
        val topicTitles = allTopics.map { it.title }

//        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, topicTitles)
        val adapter = TopicAdapter(this, allTopics)
        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view, pos, id ->

            val intent = Intent(this, TopicOverview::class.java).apply {
                putExtra(EXTRA_TOPIC, pos)
            }
            startActivity(intent)
        }
    }

    class TopicAdapter(private val context: Activity, private val data: Array<Topic>): ArrayAdapter<Topic>(context, R.layout.topic_list_item) {
        override fun getItem(position: Int): Topic {
            return data[position]
        }

        override fun getCount(): Int {
            return data.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = if (convertView == null) {
                val inflater = LayoutInflater.from(context)
                inflater.inflate(R.layout.topic_list_item, parent, false)
            } else {
                convertView
            }

            val titleText = view.findViewById(R.id.topicTitle) as TextView
            val descText = view.findViewById(R.id.topicDescription) as TextView

            titleText.text = data[position].title
            descText.text = data[position].shortDesc

            return view
        }
    }
}