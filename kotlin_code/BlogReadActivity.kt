package com.trisnasejati.testingapps

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.trisnasejati.testingapps.api.model.ApiReadBlog
import java.io.IOException
import java.io.InputStream
import java.net.URL

class BlogReadActivity : AppCompatActivity() {
    private lateinit var id: String
    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var content: TextView
    private lateinit var progress: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_read)

        image = findViewById(R.id.image)
        title = findViewById(R.id.title)
        content = findViewById(R.id.content)
        progress = findViewById(R.id.progress)

        // ambil id dari list
        id = intent.getStringExtra("id").toString()

        // menampilkan progress bar (loading)
        progress.setProgress(0, true)
        ApiReadBlog{ blog ->
            supportActionBar?.title = blog.title
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            title.text = blog.title
            content.text = blog.content

            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            val bitmap: Bitmap?
            val inputStream: InputStream
            try {
                inputStream = URL(blog.image).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
                image.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            progress.setProgress(0, false)
        }.execute("https://crud.trisnawan.my.id/blog/read?id=$id")
    }
}