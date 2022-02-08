package edu.us.ischool.quizdroid

interface TopicRepository {
    // Create
    fun addTopic(t : Topic) : Array<Topic>?
    fun addQuiz(q : Quiz) : Array<Question>?

    // Retrieve
    fun getAllTopics() : Array<Topic>
    fun getTopic(i : Int) : Topic
    fun getQuiz(i : Int) : Quiz

    // Update
    fun updateTopic(t : Topic) : Topic?
    fun updateQuiz(q : Quiz) : Quiz?

    // Delete
    fun deleteTopic(i : Int) : Topic?
    fun deleteQuiz(i : Int) : Quiz?
}

class Topic (
    val title: String,
    val shortDesc: String,
    val longDesc: String,
    val questions: Array<Question>
)

class Quiz (
    val question: String,
    val a1: String,
    val a2: String,
    val a3: String,
    val a4: String,
    val correct: Int
)