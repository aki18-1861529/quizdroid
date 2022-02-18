package edu.us.ischool.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TopicOverview : AppCompatActivity(), CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        launch {
            setSupportActionBar(findViewById(R.id.toolbar))
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)

            val quizApp = QuizApp()
            val repo: TopicRepository = quizApp.getTopicRepository(context)

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
                val intent = Intent(context, QuizPage::class.java).putExtra("bundle", bundle)
                startActivity(intent)
            }
        }
        setContentView(R.layout.activity_topic_overview)
    }
}