package com.zekart.tracken.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zekart.tracken.ui.fragment.GasStationStatisticFragment
import com.zekart.tracken.ui.fragment.GasStationListFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GasStationListFragment.newInstance()
            1 -> GasStationStatisticFragment.newInstance()
            else -> GasStationListFragment.newInstance()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}