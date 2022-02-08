package edu.us.ischool.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TopicOverview : AppCompatActivity(), TopicRepository {

    private val EXTRA_TOPIC = "edu.us.ischool.quizdroid.TOPIC"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_overview)

        val topic = intent.getIntExtra(EXTRA_TOPIC, 0)
        val info = getTopic(topic)

        findViewById<TextView>(R.id.textView2).text = info.title
        findViewById<TextView>(R.id.textView3).text = info.shortDesc
        findViewById<TextView>(R.id.textView4).text = info.questions.size.toString() + " Questions"

        val beginBtn = findViewById<Button>(R.id.button)
        val bundle = Bundle()
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