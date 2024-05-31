package com.trisnasejati.testingapps.api.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trisnasejati.testingapps.R
import com.trisnasejati.testingapps.api.data.BlogData
import java.io.IOException
import java.io.InputStream
import java.net.URL

// [1] nge ekstend Recycler Adapter
class BlogAdapter(private val list: MutableList<BlogData>, val itemClickListener: (BlogData)->Unit): RecyclerView.Adapter<BlogAdapter.MyViewHolder>() {

    // [2]
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // [4] untuk nyambungkan item konten
        val title: TextView = itemView.findViewById(R.id.title)
        val image: ImageView = itemView.findViewById(R.id.image)

        init {
            itemView.setOnClickListener {
                itemClickListener(list[adapterPosition])
            }
        }
    }

    // [3]
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // menyambungkan layout item
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_blog, parent, false)
        return MyViewHolder(itemView)
    }

    // [5]
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val blog = list[position]

        holder.title.text = blog.title

        // permit akses
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // inisialisasi bitmap
        val bitmap: Bitmap?
        val inputStream: InputStream

        try {
            // ambil gambar jadiin inputStream
            inputStream = URL(blog.image).openStream()

            // mengubah si gambar jadi format Bitmap
            bitmap = BitmapFactory.decodeStream(inputStream)

            // gambar yang udah Bitmap, ditampikan
            holder.image.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getItemCount() = list.size

}