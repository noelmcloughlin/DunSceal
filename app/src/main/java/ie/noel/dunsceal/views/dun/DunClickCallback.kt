
package ie.noel.dunsceal.views.dun

import ie.noel.dunsceal.models.entity.DunEntity

interface DunClickCallback {
  fun onClick(dun: DunEntity?)
}