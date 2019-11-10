package org.noel.dunsceal.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.noel.dunsceal.helpers.*
import java.util.*

val JSON_FILE = "duns.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<DunModel>>() {}.type

fun generateRandomId(): Long {
  return Random().nextLong()
}

class DunJSONStore : DunStore, AnkoLogger {

  val context: Context
  var duns = mutableListOf<DunModel>()

  constructor (context: Context) {
    this.context = context
    if (exists(context, JSON_FILE)) {
      deserialize()
    }
  }

  override fun findAll(): MutableList<DunModel> {
    return duns
  }

  override fun create(dun: DunModel) {
    dun.id = generateRandomId()
    duns.add(dun)
    serialize()
  }

  override fun update(dun: DunModel) {
    val dunsList = findAll() as ArrayList<DunModel>
    var foundDun: DunModel? = dunsList.find { p -> p.id == dun.id }
    if (foundDun != null) {
      foundDun.title = dun.title
      foundDun.description = dun.description
      foundDun.image = dun.image
      foundDun.lat = dun.lat
      foundDun.lng = dun.lng
      foundDun.zoom = dun.zoom
    }
    serialize()
  }

  private fun serialize() {
    val jsonString = gsonBuilder.toJson(duns, listType)
    write(context, JSON_FILE, jsonString)
  }

  private fun deserialize() {
    val jsonString = read(context, JSON_FILE)
    duns = Gson().fromJson(jsonString, listType)
  }
}
