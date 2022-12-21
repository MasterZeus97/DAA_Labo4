package com.example.daa_labo4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

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