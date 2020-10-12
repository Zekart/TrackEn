package com.zekart.tracken.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.zekart.tracken.R
import com.zekart.tracken.adapter.ViewPagerAdapter
import com.zekart.tracken.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

//TODO init permission requests

class MainActivity : FragmentActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var pagerAdapter:ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setSupportActionBar(view.toolbar)

        initTabLayout()
    }

    private fun initTabLayout(){
        pagerAdapter = ViewPagerAdapter(this)
        viewpager.adapter= pagerAdapter

        TabLayoutMediator(tabs, viewpager) { tab, position ->
            tab.text = resources.getStringArray(R.array.tab_item_title)[position]
        }.attach()
    }

}