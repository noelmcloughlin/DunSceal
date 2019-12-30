package ie.noel.dunsceal.views

import androidx.fragment.app.FragmentFactory
import ie.noel.dunsceal.views.dunlist.DunListPresenter
import ie.noel.dunsceal.views.fragment.*
import ie.noel.dunsceal.views.home.HomePresenter

class BaseFragmentFactory(private var presenter: BasePresenter, private val user: String) :
    FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =
        when (className) {
            BaseFragment::class.java.name -> BaseFragment()
            HomeFragment::class.java.name -> HomeFragment(presenter as HomePresenter, user)
            AboutUsFragment::class.java.name -> AboutUsFragment()
            EditFragment::class.java.name -> ReportAllFragment(presenter as DunListPresenter)
            ReportFragment::class.java.name -> ReportFragment(presenter as DunListPresenter)
            ReportAllFragment::class.java.name -> ReportAllFragment(presenter as DunListPresenter)

            else -> super.instantiate(classLoader, className)
        }
}