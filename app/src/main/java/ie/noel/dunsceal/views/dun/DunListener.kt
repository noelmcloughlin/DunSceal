package ie.noel.dunsceal.views.dun

import ie.noel.dunsceal.models.entity.DunEntity

interface DunListener {
  fun onDunClick(dun: DunEntity)
}