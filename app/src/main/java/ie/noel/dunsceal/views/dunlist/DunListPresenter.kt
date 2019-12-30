package ie.noel.dunsceal.views.dunlist

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ie.noel.dunsceal.models.DunModel
import ie.noel.dunsceal.views.BasePresenter
import ie.noel.dunsceal.views.BaseView
import ie.noel.dunsceal.views.VIEW

class DunListPresenter(view: BaseView) : BasePresenter(view) {

  fun onResume() {
    app.duns.findAll()
  }

  fun doAddDun() {
    view?.navigateTo(VIEW.DUN)
  }

  fun doEditDun(dun: DunModel) {
    view?.navigateTo(VIEW.DUN, 0, "dun_edit", dun)
  }

  fun doShowDunsMap() {
    view?.navigateTo(VIEW.MAPS)
  }

  fun loadDuns() {
    doAsync {
      val duns = app.duns.findAll()
      uiThread {
        view?.getAllDuns(duns as ArrayList<DunModel>)
      }
    }
  }
}