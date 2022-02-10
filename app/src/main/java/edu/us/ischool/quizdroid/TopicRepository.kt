package edu.us.ischool.quizdroid

import java.lang.reflect.Type

interface TopicRepository {

    val externalFile : String
    val topicType: Type
    val data: Array<Topic>

    // Create
    fun addTopic(t : Topic) : Array<Topic>?
    fun addQuiz(q : Quiz) : Array<Question>?

    // Retrieve
    fun getAllTopics() : Array<Topic>
    fun getTopic(i : Int) : Topic
    fun getAllQuizzes(i : Int) : Array<Quiz>
    fun getQuiz(q : Int, i : Int) : Quiz

    // Update
    fun updateTopic(t : Topic) : Topic?
    fun updateQuiz(q : Quiz) : Quiz?

    // Delete
    fun deleteTopic(i : Int) : Topic?
    fun deleteQuiz(i : Int) : Quiz?
}

class Topic (
    val title: String,
    val desc: String,
    val questions: Array<Quiz>
)

class Quiz (
    val text: String,
    val answer: Int,
    val answers: Array<String>
)