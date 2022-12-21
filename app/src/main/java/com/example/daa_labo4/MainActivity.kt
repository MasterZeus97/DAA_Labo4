package com.example.daa_labo4

/**
 * @author Perrenoud Pascal
 * @author Seem Thibault
 * @description Point d'entrée, activité principale de l'application
 */

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var adapter: RecyclerviewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.recycler_view_notes)
        adapter = RecyclerviewAdapter(lifecycleScope);
        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(this, 3)
        val listOfPicture = IntArray(10000){it}
        adapter.items = listOfPicture.asList()

        val myPeriodicWorkRequest = PeriodicWorkRequestBuilder<MyWork>(15, TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
            .build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueueUniquePeriodicWork("repeatCleanCacheWorker", ExistingPeriodicWorkPolicy.KEEP, myPeriodicWorkRequest)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_clean_cache->{
                val workManager = WorkManager.getInstance(applicationContext)
                val myWorkRequest = OneTimeWorkRequestBuilder<MyWork>().build()
                workManager.enqueue(myWorkRequest)
                adapter.notifyDataSetChanged()
                true
            }
            else -> {true}
        }
    }

}