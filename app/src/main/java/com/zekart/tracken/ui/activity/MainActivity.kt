package com.zekart.tracken.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.google.android.material.tabs.TabLayoutMediator
import com.zekart.tracken.R
import com.zekart.tracken.adapter.ViewPagerAdapter
import com.zekart.tracken.databinding.ActivityMainBinding
import com.zekart.tracken.services.WorkManagerImpl
import com.zekart.tracken.utils.DataAppUtil
import com.zekart.tracken.utils.ViewUtil
import com.zekart.tracken.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*



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
        val tempIdUser = DataAppUtil.getUserID(this)

        if (tempIdUser <=0){
            mViewModel?.createUser("Ihor")
            mViewModel?.isCreatedUser()?.observe(this, {
                if (it > 0) {
                    mViewModel?.setUserId(it)
                    DataAppUtil.saveUserID(this, it)
                    initTabLayout()
                } else {
                    Toast.makeText(this, "Error to create", Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            mViewModel?.setUserId(tempIdUser)
            initTabLayout()
        }
    }

    private fun initSynchronize(){

        mViewModel?.checkConnection()

        mViewModel?.synchronizeIsErrorConsume()?.observe(this, Observer {
            if (it!=null && it){
                ViewUtil.showToastApplication(this,getString(R.string.error_synchronize_consume))
            }
        })

        mViewModel?.synchronizeIsErrorStation()?.observe(this, Observer {
            if (it!=null && it){
                ViewUtil.showToastApplication(this,getString(R.string.error_synchronize_station))
            }
        })
    }

    private fun initTabLayout(){
        initSynchronize()

        pagerAdapter = ViewPagerAdapter(this)
        viewpager.adapter= pagerAdapter

        TabLayoutMediator(tabs, viewpager) { tab, position ->
            tab.text = resources.getStringArray(R.array.tab_item_title)[position]
        }.attach()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        WorkManagerImpl.initWorkManager(this)
    }
}