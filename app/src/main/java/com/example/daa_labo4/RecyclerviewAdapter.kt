package com.example.daa_labo4

/**
 * @author Perrenoud Pascal
 * @author Seem Thibault
 * @description Adapter qui gère le téléchargement et cache des images utilisées
 */

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.concurrent.TimeUnit


class RecyclerviewAdapter(_lifeCycle : LifecycleCoroutineScope, _items : List<Int> = listOf()) : RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder>() {

    var items = listOf<Int>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    lateinit var lifeCycleScope : LifecycleCoroutineScope
    var listOfJob : MutableList<Job?> = mutableListOf()
    private lateinit var context : Context

    init {
        items = _items
        lifeCycleScope = _lifeCycle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.picture_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        listOfJob.remove(holder.job)
        holder.stopJob()
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val imageToDisplay = view.findViewById<ImageView>(R.id.picture_recyclerview)
        private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_recyclerview)
        var job : Job? = null

        fun bind(numberPicture: Int) {

            job = lifeCycleScope.launch {
                val file = File(context.cacheDir, "$numberPicture.jpg")
                var save = false

                //Test si le file exist et s'il n'est pas trop vieux
                val bytes : ByteArray? = if(file.exists() && TimeUnit.MILLISECONDS.toMinutes((System.currentTimeMillis() - file.lastModified())) < 5){
                        file.readBytes()
                    } else {
                        save = true
                        displayImage(null)
                        downloadImage(URL("https://daa.iict.ch/images/$numberPicture.jpg"))
                    }

                val bmp = decodeImage(bytes)

                if (save) {
                    cachePicture(bmp, numberPicture)
                }

                displayImage(bmp)
            }
            listOfJob.add(job)
        }

        suspend fun downloadImage(url : URL) : ByteArray? = withContext(Dispatchers.IO){
            try {
                url.readBytes()
            }catch(e:IOException){
                Log.w(TAG, "Exception while downloading image", e)
                null
            }
        }

        suspend fun decodeImage(bytes : ByteArray?): Bitmap? = withContext(Dispatchers.Default){
            try {
                BitmapFactory.decodeByteArray(bytes, 0, bytes?.size?:0)
            }catch(e:IOException){
                Log.w(TAG, "Exception while decoding image", e)
                null
            }
        }

        suspend fun displayImage(bmp : Bitmap?) = withContext(Dispatchers.Main){
            if(bmp != null){
                imageToDisplay.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                imageToDisplay.setImageBitmap(bmp)
            }else{
                progressBar.visibility = View.VISIBLE
                imageToDisplay.visibility = View.GONE
            }
        }

        suspend fun cachePicture(bmp: Bitmap?, numberPicture: Int) = withContext(Dispatchers.Default) {
            val file = File(context.cacheDir, "$numberPicture.jpg")
            file.outputStream().use {
                bmp?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
        }
        fun stopJob(){
            job?.cancel()
        }
    }
}