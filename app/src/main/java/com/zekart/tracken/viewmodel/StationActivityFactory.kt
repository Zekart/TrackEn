package com.zekart.tracken.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StationActivityFactory (val app: Application, private val idUser:Long?, private val idStation:Long?) : ViewModelProvider.AndroidViewModelFactory(app) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityGasStationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActivityGasStationViewModel(app,idUser,idStation) as T
        }

        throw IllegalArgumentException("Cant create view model")
    }
}