package ie.noel.dunsceal.views.fragment.common

import androidx.fragment.app.FragmentFactory
import ie.noel.dunsceal.main.MainApp
import ie.noel.dunsceal.views.fragment.*

class BaseFragmentFactory(private var app: MainApp) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =
        when (className) {
            BaseFragment::class.java.name -> BaseFragment()
            //DunFragment::class.java.name -> DunFragment(this)
            AboutUsFragment::class.java.name -> AboutUsFragment()
            EditFragment::class.java.name -> ReportAllFragment()
            ReportFragment::class.java.name -> ReportFragment()

            else -> super.instantiate(classLoader, className)
        }
}