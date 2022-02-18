package edu.us.ischool.quizdroid

import java.lang.reflect.Type

interface TopicRepository {

    val topicType: Type

    // Create
    suspend fun addTopic(t : Topic) : Array<Topic>?
    suspend fun addQuiz(q : Quiz) : Array<Question>?

    // Retrieve
    suspend fun getAllTopics() : Array<Topic>
    suspend fun getTopic(i : Int) : Topic
    suspend fun getAllQuizzes(i : Int) : Array<Quiz>
    suspend fun getQuiz(q : Int, i : Int) : Quiz

    // Update
    suspend fun updateTopic(t : Topic) : Topic?
    suspend fun updateQuiz(q : Quiz) : Quiz?

    // Delete
    suspend fun deleteTopic(i : Int) : Topic?
    suspend fun deleteQuiz(i : Int) : Quiz?
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