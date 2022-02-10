package edu.us.ischool.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.view.Menu
import android.view.MenuItem


class MainActivity : AppCompatActivity() {

    private lateinit var arrayAdapter : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

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
        listView.setOnItemClickListener { _, _, pos, _ ->

            val intent = Intent(this, TopicOverview::class.java).apply {
                putExtra("topic", pos)
            }
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_preferences -> {
            val intent = Intent(this, Preferences::class.java)
            startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}