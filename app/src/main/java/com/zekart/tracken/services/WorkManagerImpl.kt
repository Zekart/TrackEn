package com.zekart.tracken.services

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.zekart.tracken.utils.Constans
import java.util.concurrent.ExecutionException


class WorkManagerImpl {
    companion object{
        private fun getBuilderOneTime(): OneTimeWorkRequest.Builder {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            return OneTimeWorkRequest
                .Builder(SynchronizeToFirebase::class.java)
                .addTag(Constans.TAG_SYNCHRONIZE)
                .setConstraints(constraints)
        }

        private fun isScheduled(manager: WorkManager){
            try {

                manager.cancelAllWorkByTag(Constans.TAG_SYNCHRONIZE)
                manager.enqueue(getBuilderOneTime().build())

            }catch (e: ExecutionException) {
                e.printStackTrace()
            }catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        fun initWorkManager(context: Context){
            val mn = WorkManager.getInstance(context)
            isScheduled(mn)
        }
    }
}