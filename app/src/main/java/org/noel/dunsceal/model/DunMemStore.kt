package org.noel.dunsceal.model

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class DunMemStore : DunStore, AnkoLogger {

    val duns = ArrayList<DunModel>()

    override fun findAll(): List<DunModel> {
        return duns
    }

    override fun create(dun: DunModel) {
        dun.id = getId()
        duns.add(dun)
        logAll()
    }

    override fun update(dun: DunModel) {
        var foundDun: DunModel? = duns.find { p -> p.id == dun.id }
        if (foundDun != null) {
            foundDun.name = dun.name
            foundDun.description = dun.description
            foundDun.image = dun.image
            logAll();
        }
    }

    fun logAll() {
        duns.forEach { info("${it}") }
    }
}