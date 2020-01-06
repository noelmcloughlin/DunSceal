
package ie.noel.dunsceal.views.home.dun

import ie.noel.dunsceal.models.Dun

interface DunClickCallback {
  fun onClick(dun: Dun?)
}