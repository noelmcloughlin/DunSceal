package org.noel.dunsceal.model

interface DunStore {
    fun findAll(): List<org.noel.dunsceal.model.DunModel>
    fun create(dun: org.noel.dunsceal.model.DunModel)
    fun update(dun: org.noel.dunsceal.model.DunModel)
    //fun delete(dun: org.noel.dunsceal.model.DunModel)
}