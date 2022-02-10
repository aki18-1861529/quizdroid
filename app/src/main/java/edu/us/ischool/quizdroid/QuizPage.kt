package edu.us.ischool.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.isVisible

class QuizPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val quizApp = QuizApp()
        val repo : TopicRepository = quizApp.getTopicRepository()

        val bundle : Bundle = intent.getBundleExtra("bundle") as Bundle
        val q : Int = bundle.getInt("topic")
        val i : Int = bundle.getInt("question")
        val qIdx : Int = i - 1
        val quiz = repo.getQuiz(q, qIdx)

        val question = findViewById<TextView>(R.id.question)
        val a1 = findViewById<TextView>(R.id.a1)
        val a2 = findViewById<TextView>(R.id.a2)
        val a3 = findViewById<TextView>(R.id.a3)
        val a4 = findViewById<TextView>(R.id.a4)
        question.text = quiz.text
        a1.text = quiz.answers[0]
        a2.text = quiz.answers[1]
        a3.text = quiz.answers[2]
        a4.text = quiz.answers[3]

        val submitBtn = findViewById<Button>(R.id.submitBtn)
        submitBtn.isVisible = false

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup.setOnCheckedChangeListener { _, _ ->
            submitBtn.isVisible = true
        }
        submitBtn.setOnClickListener {
            val selected : RadioButton = findViewById(radioGroup.checkedRadioButtonId)

            val bundle2 = Bundle()
            bundle2.putInt("topic", q)
            bundle2.putString("answer", selected.text.toString())
            bundle2.putInt("correct", quiz.answer)
            bundle2.putInt("score", bundle.getInt("score"))
            bundle2.putInt("question", qIdx + 1)
            bundle2.putInt("questionMax", bundle.getInt("questionMax"))
            val intent = Intent(this, Answer::class.java).putExtra("bundle", bundle2)
            finish()
            startActivity(intent)
        }
    }
}