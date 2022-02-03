package edu.us.ischool.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Answer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        val bundle = intent.getBundleExtra("bundle") as Bundle
        val actual = findViewById<TextView>(R.id.actualAnswer)
        val correct = findViewById<TextView>(R.id.correctAnswer)
        val score = findViewById<TextView>(R.id.score)
        val btn = findViewById<Button>(R.id.button2)


        actual.text = bundle.getString("answer")
        correct.text = bundle.getString("correct")
        val newScore = if (actual.text == correct.text) {
            bundle.getInt("score") + 1
        } else {
            bundle.getInt("score")
        }
        score.text = "You have " + newScore.toString() + " out of " + bundle.getInt("questionMax") + " correct"

        if (bundle.getInt("question") + 1 == bundle.getInt("questionMax")) {
            btn.text = "Finish"
        } else {
            btn.text = "Next"
        }
        btn.setOnClickListener {
            if (btn.text == "Next") {
                val bundle2 = Bundle()
                bundle2.putInt("score", 0)

                val qIdx = bundle.getInt("question") + 1
                bundle2.putInt("question", qIdx + 1)
                bundle2.putInt("questionMax", 9)
                bundle2.putInt("score", newScore)
                val intent = Intent(this, Quiz::class.java).putExtra("bundle", bundle2)
                finish()
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
    }
}