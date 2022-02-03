package edu.us.ischool.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    lateinit var arrayAdapter : ArrayAdapter<String>
    private val EXTRA_TOPIC = "edu.us.ischool.quizdroid.TOPIC"

    private var topics: Array<String> = arrayOf(
        "Math",
        "Physics",
        "Marvel Super Heroes"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.listView)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, topics)
        listView.adapter = arrayAdapter
        listView.setOnItemClickListener { parent, view, pos, id ->

            val topic = listView.getItemAtPosition(pos).toString()

            val intent = Intent(this, TopicOverview::class.java).apply {
                putExtra(EXTRA_TOPIC, topic)
            }
            startActivity(intent)
        }


    }
}