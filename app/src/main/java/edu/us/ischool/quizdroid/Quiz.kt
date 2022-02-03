package edu.us.ischool.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.isVisible

class Quiz : AppCompatActivity() {

    class Question (val q: String, val a1: String, val a2: String, val a3: String, val a4: String, private val correct: Int) {
        fun getCorrectAnswer() : String {
            return when (correct) {
                1 -> a1
                2 -> a2
                3 -> a3
                4 -> a4
                else -> ""
            }
        }
    }

    private val questions : Array<Question> = arrayOf(
        Question("Question 1", "Answer 1", "Answer 2", "Answer 3", "Answer 4", 1),
        Question("Question 2", "Answer 1", "Answer 2", "Answer 3", "Answer 4", 2),
        Question("Question 3", "Answer 1", "Answer 2", "Answer 3", "Answer 4", 3),
        Question("Question 4", "Answer 1", "Answer 2", "Answer 3", "Answer 4", 4),
        Question("Question 5", "Answer 1", "Answer 2", "Answer 3", "Answer 4", 4),
        Question("Question 6", "Answer 1", "Answer 2", "Answer 3", "Answer 4", 3),
        Question("Question 7", "Answer 1", "Answer 2", "Answer 3", "Answer 4", 2),
        Question("Question 8", "Answer 1", "Answer 2", "Answer 3", "Answer 4", 1),
        Question("Question 9", "Answer 1", "Answer 2", "Answer 3", "Answer 4", 2)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val bundle : Bundle = intent.getBundleExtra("bundle") as Bundle
        val q : Int = bundle.getInt("question")
        val qIdx : Int = q - 1

        val question = findViewById<TextView>(R.id.question)
        val a1 = findViewById<TextView>(R.id.a1)
        val a2 = findViewById<TextView>(R.id.a2)
        val a3 = findViewById<TextView>(R.id.a3)
        val a4 = findViewById<TextView>(R.id.a4)
        question.text = questions[qIdx].q
        a1.text = questions[qIdx].a1
        a2.text = questions[qIdx].a2
        a3.text = questions[qIdx].a3
        a4.text = questions[qIdx].a4

        val submitBtn = findViewById<Button>(R.id.submitBtn)
        submitBtn.isVisible = false

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup.setOnCheckedChangeListener { _, _ ->
            submitBtn.isVisible = true
        }
        submitBtn.setOnClickListener {
            val selected : RadioButton = findViewById(radioGroup.checkedRadioButtonId)

            val bundle2 = Bundle()
            bundle2.putString("answer", selected.text.toString())
            bundle2.putString("correct", questions[qIdx].getCorrectAnswer())
            bundle2.putInt("score", bundle.getInt("score"))
            bundle2.putInt("question", qIdx)
            bundle2.putInt("questionMax", bundle.getInt("questionMax"))
            val intent = Intent(this, Answer::class.java).putExtra("bundle", bundle2)
            finish()
            startActivity(intent)
        }
    }
}