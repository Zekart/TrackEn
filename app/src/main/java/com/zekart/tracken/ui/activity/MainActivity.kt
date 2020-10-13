package com.zekart.tracken.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.zekart.tracken.R
import com.zekart.tracken.adapter.ViewPagerAdapter
import com.zekart.tracken.databinding.ActivityMainBinding
import com.zekart.tracken.utils.LocalDataUtil
import com.zekart.tracken.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*


//TODO init permission requests

class MainActivity : FragmentActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var pagerAdapter:ViewPagerAdapter
    private var mViewModel: MainActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        checkIfUserAlreadyRegistered()
    }

    private fun checkIfUserAlreadyRegistered(){
        mViewModel?.getUser()
        mViewModel?.checkUser()?.observe(this, {
            if (it == null) {
                showRegistrationDialog()
                println("My name is $it.mName")
            } else {
                initTabLayout()
            }
        })
    }



    private fun initTabLayout(){
        pagerAdapter = ViewPagerAdapter(this)
        viewpager.adapter= pagerAdapter

        TabLayoutMediator(tabs, viewpager) { tab, position ->
            tab.text = resources.getStringArray(R.array.tab_item_title)[position]
        }.attach()
    }

    private fun showRegistrationDialog(){
        val taskEditText = EditText(this)
        val dialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.not_registered_user_dialog_title)
            .setMessage(R.string.not_registered_user__dialog_message)
            .setView(taskEditText)
            .setPositiveButton(R.string.btn_yes) { _, _ ->
            }
            .setNegativeButton(R.string.btn_cancel) { _, _ ->
                finish()
            }
            .setCancelable(false)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener {
                mViewModel?.createUser(taskEditText.text.toString())
                mViewModel?.isCreatedUser()?.observe(this, {
                    if (it > 0) {
                        LocalDataUtil.saveUserID(this, it)
                        initTabLayout()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Error to create", Toast.LENGTH_SHORT).show()
                    }
                })
            }
    }

}