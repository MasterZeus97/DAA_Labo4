package com.example.daa_labo4

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

class RecyclerviewAdapter(_items : List<Int> = listOf()) : RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder>() {

    var items = listOf<Int>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    init {
        items = _items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.picture_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val imageToDisplay = view.findViewById<ImageView>(R.id.picture_recyclerview)
        private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_recyclerview)
        fun bind(numberPicture: Int){
            //imageToDisplay.setImageBitmap(BitmapFactory.d)
            activity.lifecycleScope.launch{

            }
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

    }

}