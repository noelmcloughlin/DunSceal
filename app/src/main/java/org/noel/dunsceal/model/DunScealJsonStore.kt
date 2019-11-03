package org.noel.dunsceal.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.noel.dunsceal.helpers.*
import java.util.*

val JSON_FILE = "dunsceals.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<ArrayList<DunScealModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class DunScealJsonStore : org.noel.dunsceal.model.DunScealStore, AnkoLogger {

    val context: Context
    var dunsceals = mutableListOf<DunScealModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<DunScealModel> {
        return dunsceals
    }

    override fun create(dunsceal: DunScealModel) {
        dunsceal.id = generateRandomId()
        dunsceals.add(dunsceal)
        serialize()
    }


    override fun update(dunsceal: DunScealModel) {
        // todo
    }

    override fun delete(dunsceal: DunScealModel) {
        // todo
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(dunsceals, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        dunsceals = Gson().fromJson(jsonString, listType)
    }
}