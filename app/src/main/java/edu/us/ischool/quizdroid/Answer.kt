package edu.us.ischool.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class Answer : AppCompatActivity(), CoroutineScope {
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

            val quizApp = QuizApp()
            val repo: TopicRepository = quizApp.getTopicRepository(context)

            val bundle = intent.getBundleExtra("bundle") as Bundle
            val actual = findViewById<TextView>(R.id.actualAnswer)
            val correct = findViewById<TextView>(R.id.correctAnswer)
            val score = findViewById<TextView>(R.id.score)
            val btn = findViewById<Button>(R.id.button2)

            val qIdx = bundle.getInt("question") - 1
            val quiz = repo.getQuiz(bundle.getInt("topic"), qIdx)
            val ans = when (quiz.answer) {
                1 -> quiz.answers[0]
                2 -> quiz.answers[1]
                3 -> quiz.answers[2]
                4 -> quiz.answers[3]
                else -> ""
            }

            actual.text = bundle.getString("answer")
            correct.text = ans
            val newScore = if (actual.text == correct.text) {
                bundle.getInt("score") + 1
            } else {
                bundle.getInt("score")
            }
            score.text =
                "You have " + newScore.toString() + " out of " + bundle.getInt("question") + " correct"

            if (bundle.getInt("question") == bundle.getInt("questionMax")) {
                btn.text = "Finish"
            } else {
                btn.text = "Next"
            }
            btn.setOnClickListener {
                if (btn.text == "Next") {
                    val bundle2 = Bundle()
                    bundle2.putInt("topic", bundle.getInt("topic"))
                    bundle2.putInt("score", 0)
                    bundle2.putInt("question", bundle.getInt("question") + 1)
                    bundle2.putInt("questionMax", bundle.getInt("questionMax"))
                    bundle2.putInt("score", newScore)
                    val intent = Intent(context, QuizPage::class.java).putExtra("bundle", bundle2)
                    finish()
                    startActivity(intent)
                } else {
                    val intent = Intent(context, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
            }
        }
        setContentView(R.layout.activity_answer)
    }
}