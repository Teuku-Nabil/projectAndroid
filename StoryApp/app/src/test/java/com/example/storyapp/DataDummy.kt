package com.example.storyapp

import com.example.storyapp.model.StoryModel
import java.util.*

object DataDummy {
    fun generateDummyQuoteResponse(): List<StoryModel> {
        val items: MutableList<StoryModel> = arrayListOf()
        for (i in 0..100) {
            val quote = StoryModel(
                i.toString(),
                "name $i",
                "image $i",
                "description $i",
                0.0,
                0.0,
            )
            items.add(quote)
        }
        return items
    }

    fun generateDummyToken(): String {
        return UUID.randomUUID().toString()
    }
}