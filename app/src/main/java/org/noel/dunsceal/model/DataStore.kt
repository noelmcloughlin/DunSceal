package org.noel.dunsceal.model

interface DunScealStore {
    fun findAll(): List<org.noel.dunsceal.model.DunScealModel>
    fun create(DunSceal: org.noel.dunsceal.model.DunScealModel)
    fun update(DunSceal: org.noel.dunsceal.model.DunScealModel)
    fun delete(DunSceal: org.noel.dunsceal.model.DunScealModel)

}