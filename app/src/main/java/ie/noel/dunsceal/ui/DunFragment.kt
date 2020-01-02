
package ie.noel.dunsceal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ie.noel.dunsceal.R
import ie.noel.dunsceal.adapters.InvestigationAdapter
import ie.noel.dunsceal.databinding.DunFragmentBinding
import ie.noel.dunsceal.models.Investigation
import ie.noel.dunsceal.persistence.viewmodel.DunViewModel

class DunFragment : Fragment() {
  private var mBinding: DunFragmentBinding? = null
  private var mInvestigationAdapter: InvestigationAdapter? = null
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? { // Inflate this data binding layout
    mBinding = DataBindingUtil.inflate(inflater, R.layout.dun_fragment, container, false)
    // Create and set the adapter for the RecyclerView.
    mInvestigationAdapter = InvestigationAdapter(mInvestigationClickCallback)
    mBinding!!.investigationList.adapter = mInvestigationAdapter
    return mBinding!!.investigationList
  }

  private val mInvestigationClickCallback = object : InvestigationClickCallback {
     override fun onClick(investigation: Investigation?) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    // no-op
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val factory = DunViewModel.Factory(
        requireActivity().application, arguments!!.getInt(KEY_DUN_ID))
    val model = ViewModelProvider(this, factory)
        .get(DunViewModel::class.java)
    mBinding!!.dunViewModel = model
    subscribeToModel(model)
  }

  private fun subscribeToModel(model: DunViewModel) { // Observe dun data
    model.observableDun!!.observe(viewLifecycleOwner, Observer { dunEntity -> model.setDun(dunEntity!!) })
    // Observe investigations
    model.investigations!!.observe(viewLifecycleOwner, Observer { investigationEntities ->
      if (investigationEntities != null) {
        mBinding!!.isLoading = false
        mInvestigationAdapter!!.setInvestigationList(investigationEntities)
      } else {
        mBinding!!.isLoading = true
      }
    })
  }

  companion object {
    private const val KEY_DUN_ID = "dun_id"
    /** Creates dun fragment for specific dun ID  */
    fun forDun(dunId: Int): DunFragment {
      val fragment = DunFragment()
      val args = Bundle()
      args.putInt(KEY_DUN_ID, dunId)
      fragment.arguments = args
      return fragment
    }
  }
}