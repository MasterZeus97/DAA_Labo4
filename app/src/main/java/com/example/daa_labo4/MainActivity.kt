package com.example.daa_labo4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
}