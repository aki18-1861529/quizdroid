package edu.us.ischool.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TopicOverview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_overview)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val quizApp = QuizApp()
        val repo : TopicRepository = quizApp.getTopicRepository(this)

        val topic = intent.getIntExtra("topic", 0)
        val info = repo.getTopic(topic)

        findViewById<TextView>(R.id.textView2).text = info.title
        findViewById<TextView>(R.id.textView3).text = info.desc
        findViewById<TextView>(R.id.textView4).text = if (info.questions.size == 1) {
            info.questions.size.toString() + " Question"
        } else {
            info.questions.size.toString() + " Questions"
        }

        val beginBtn = findViewById<Button>(R.id.button)
        val bundle = Bundle()
        bundle.putInt("topic", topic)
        bundle.putString("answer", "")
        bundle.putInt("correct", 0)
        bundle.putInt("score", 0)
        bundle.putInt("question", 1)
        bundle.putInt("questionMax", info.questions.size)
        beginBtn.setOnClickListener {
            val intent = Intent(this, QuizPage::class.java).putExtra("bundle", bundle)
            startActivity(intent)
        }
    }
}