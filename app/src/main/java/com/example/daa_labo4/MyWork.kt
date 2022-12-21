package com.example.daa_labo4

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.IOException

class MyWork(appContext:Context, workerParams:WorkerParameters) : Worker(appContext, workerParams){

    private val dir = appContext.cacheDir

    override fun doWork(): Result {
        try {
            dir.listFiles()?.forEach { it.delete() }
        }catch(e : IOException){
            return Result.failure()
        }
        return Result.success()
    }

}