package com.zekart.tracken.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BaseFactoryVM(val app: Application) : ViewModelProvider.AndroidViewModelFactory(app) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FragmentStationListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FragmentStationListViewModel(app) as T
        }
        if (modelClass.isAssignableFrom(FragmentStatisticViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FragmentStatisticViewModel(app) as T
        }

        throw IllegalArgumentException("Cant create view model")
    }
}