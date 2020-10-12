package com.zekart.tracken.adapter

import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zekart.tracken.ui.fragment.GasStationStatisticFragment
import com.zekart.tracken.ui.fragment.GasStationListFragment

class ViewPagerAdapter(fragmentAct: FragmentActivity) :
    FragmentStateAdapter(fragmentAct) {

    private val mFragmentList: MutableList<Fragment> = ArrayList()

    init {
        mFragmentList.add(GasStationListFragment.newInstance())
        mFragmentList.add(GasStationStatisticFragment.newInstance())
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }
}