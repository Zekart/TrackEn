package com.zekart.tracken.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zekart.tracken.R
import com.zekart.tracken.adapter.GasStationListAdapter
import com.zekart.tracken.databinding.FragmentGasStationListBinding
import com.zekart.tracken.ui.activity.GasStationActivity
import com.zekart.tracken.ui.listeners.GasStationAdapterListener
import com.zekart.tracken.utils.Constans
import com.zekart.tracken.utils.ViewUtil
import com.zekart.tracken.viewmodel.FragmentStationListViewModel
import kotlinx.android.synthetic.main.fragment_gas_station_list.view.*

class GasStationListFragment: Fragment(), GasStationAdapterListener,LifecycleOwner {
    private var binding: FragmentGasStationListBinding? = null
    private var adapterRecyclerView: GasStationListAdapter? = null
    private lateinit var viewModel:FragmentStationListViewModel

    companion object {
        @JvmStatic
        fun newInstance() = GasStationListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGasStationListBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(FragmentStationListViewModel::class.java)

        initRecyclerViewStationList()

        binding?.fabAddNewStation?.setOnClickListener {
            onCreateStationActivity(null)
        }

        viewModel.getStationList().observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()){
                ViewUtil.showSnackBar(view,getString(R.string.empty_value_message))
            }else{
                adapterRecyclerView?.setStation(it)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_item_add_new_station ->{
                onCreateStationActivity(null)
            }
        }
        return true
    }

    override fun onGasStationClick(id_station: Long?) {
        onCreateStationActivity(id_station)
    }

    private fun initRecyclerViewStationList(){
        binding?.root?.recycler_gas_station.apply {
            adapterRecyclerView = context?.let { GasStationListAdapter(it,this@GasStationListFragment) }
            this?.adapter = adapterRecyclerView
            this?.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        }
    }

    private fun onCreateStationActivity(id:Long?){
        val intent = Intent(activity, GasStationActivity::class.java).apply {
            id.let {
                this.putExtra(Constans.START_STATION_ACTIVITY,id)
            }
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}