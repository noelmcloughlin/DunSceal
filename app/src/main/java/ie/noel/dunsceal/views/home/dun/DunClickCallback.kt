
package ie.noel.dunsceal.views.home.dun

import ie.noel.dunsceal.models.entity.DunEntity

interface DunClickCallback {
  fun onClick(dun: DunEntity?)
}