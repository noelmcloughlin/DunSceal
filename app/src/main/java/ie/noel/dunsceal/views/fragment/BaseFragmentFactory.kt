package ie.noel.dunsceal.views.fragment

import androidx.fragment.app.FragmentFactory
import ie.noel.dunsceal.views.BasePresenter
import ie.noel.dunsceal.views.dun.EditFragment
import ie.noel.dunsceal.views.dunlist.ReportAllFragment
import ie.noel.dunsceal.views.dunlist.ReportFragment
import ie.noel.dunsceal.views.about.AboutUsFragment
import ie.noel.dunsceal.views.home.HomeFragment
import ie.noel.dunsceal.views.home.HomePresenter

class BaseFragmentFactory(private var presenter: BasePresenter, private val user: String) :
    FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =
        when (className) {
            BaseFragment::class.java.name -> BaseFragment()
            HomeFragment::class.java.name -> HomeFragment(
                presenter as HomePresenter,
                user
            )
            AboutUsFragment::class.java.name -> AboutUsFragment()
            EditFragment::class.java.name -> ReportAllFragment(
                presenter as HomePresenter
            )
            ReportFragment::class.java.name -> ReportFragment(
                presenter as HomePresenter
            )
            ReportAllFragment::class.java.name -> ReportAllFragment(
                presenter as HomePresenter
            )

            else -> super.instantiate(classLoader, className)
        }
}