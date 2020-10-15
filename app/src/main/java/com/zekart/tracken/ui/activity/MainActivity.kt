package com.zekart.tracken.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.work.WorkManager
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.zekart.tracken.R
import com.zekart.tracken.adapter.ViewPagerAdapter
import com.zekart.tracken.databinding.ActivityMainBinding
import com.zekart.tracken.services.WorkManagerImpl
import com.zekart.tracken.utils.LocalDataUtil
import com.zekart.tracken.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*


//TODO init permission requests

class MainActivity : FragmentActivity(), LifecycleObserver {
    private lateinit var binding: ActivityMainBinding
    private lateinit var pagerAdapter:ViewPagerAdapter
    private var mViewModel: MainActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        mViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        checkIfUserAlreadyRegistered()
    }

    private fun checkIfUserAlreadyRegistered(){
        val tempIdUser = LocalDataUtil.getUserID(this)

        if (tempIdUser <=0){
            mViewModel?.createUser("Ihor")
            mViewModel?.isCreatedUser()?.observe(this, {
                if (it > 0) {
                    LocalDataUtil.saveUserID(this, it)
                    initTabLayout()
                } else {
                    Toast.makeText(this, "Error to create", Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            initTabLayout()
        }
    }

    private fun initTabLayout(){
        pagerAdapter = ViewPagerAdapter(this)
        viewpager.adapter= pagerAdapter

        TabLayoutMediator(tabs, viewpager) { tab, position ->
            tab.text = resources.getStringArray(R.array.tab_item_title)[position]
        }.attach()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        Log.d("manager","background")
        WorkManagerImpl.initWorkManager(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        Log.d("manager","foreground")
    }
}