package edu.us.ischool.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TopicOverview : AppCompatActivity() {

    private val EXTRA_TOPIC = "edu.us.ischool.quizdroid.TOPIC"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_overview)

        val topic = intent.getStringExtra(EXTRA_TOPIC)

        findViewById<TextView>(R.id.textView2).text = topic
        findViewById<TextView>(R.id.textView3).text = "Brief description of topic"
        findViewById<TextView>(R.id.textView4).text = "9 Questions"

        val beginBtn = findViewById<Button>(R.id.button)
        val bundle = Bundle()
        bundle.putString("answer", "")
        bundle.putString("correct", "")
        bundle.putInt("score", 0)
        bundle.putInt("question", 1)
        bundle.putInt("questionMax", 9)
        beginBtn.setOnClickListener {
            val intent = Intent(this, Quiz::class.java).putExtra("bundle", bundle)
            startActivity(intent)
        }
    }
}