package edu.us.ischool.quizdroid

import android.util.Log

class MemoryTopicRepository : TopicRepository {
    private val topics: Array<String>
        get() = arrayOf(
            "Math",
            "Physics",
            "Marvel Super Heroes"
        )

    private val shortDesc: Array<String>
        get() = arrayOf(
            "Brief description of Math",
            "Brief description of Physics",
            "Brief description of Marvel Super Heroes"
        )

    private val longDesc: Array<String>
        get() = arrayOf(
            "Long description of Math",
            "Long description of Physics",
            "Long description of Marvel Super Heroes"
        )

    private val questions : Array<Question>
        get() = arrayOf(
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

    override fun getAllTopics() : Array<Topic> {
        val topicArr : MutableList<Topic> = mutableListOf()
        for (i in topics.indices) {
            topicArr.add(Topic(topics[i], shortDesc[i], longDesc[i], questions))
        }
        Log.i("Topics", topicArr.toTypedArray().toString())
        return topicArr.toTypedArray()
    }

    override fun getTopic(i : Int) : Topic {
        return Topic (
            topics[i],
            shortDesc[i],
            longDesc[i],
            questions
        )
    }

    override fun getQuiz(i : Int) : Quiz {
        return Quiz (
            questions[i].q,
            questions[i].a1,
            questions[i].a2,
            questions[i].a3,
            questions[i].a4,
            questions[i].correct
        )
    }

    override fun addTopic(t: Topic): Array<Topic>? {
        return null
    }

    override fun addQuiz(q: Quiz): Array<Question>? {
        return null
    }

    override fun deleteTopic(i: Int): Topic? {
        return null
    }

    override fun deleteQuiz(i: Int): Quiz? {
        return null
    }

    override fun updateTopic(t: Topic): Topic? {
        return null
    }

    override fun updateQuiz(q: Quiz): Quiz? {
        return null
    }

}
