package com.trisnasejati.testingapps

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.trisnasejati.testingapps.api.model.ApiCreateBlog
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URI
import java.net.URL

class BlogCreateActivity : AppCompatActivity() {

    private var fileUri: Uri? = null
    private lateinit var progress: ProgressBar
    private lateinit var image: ImageView
    private lateinit var title: EditText
    private lateinit var content: EditText
    private lateinit var btnSave: Button

    // untuk mencari nama file
    @SuppressLint("Range")
    fun ContentResolver.getFileName(uri: Uri): String {
        var name = ""
        val cursor = query(
            uri, null, null,
            null, null
        )
        cursor?.use {
            it.moveToFirst()
            name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
        return name
    }

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_create)

        progress = findViewById(R.id.progress)
        progress.visibility = View.GONE

        image = findViewById(R.id.image)
        title = findViewById(R.id.title)
        content = findViewById(R.id.content)
        btnSave = findViewById(R.id.btn_save)

        image.setOnClickListener {
            // untuk memilih file gambar
            progress.visibility = View.VISIBLE
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }

            // buka file exploler
            startActivityForResult(intent, 2)
        }

        btnSave.setOnClickListener{
            // cek apakah semua data udah di isi
            if (fileUri != null && title.text.isNotEmpty() && content.text.isNotEmpty()){

                // aktifkan progressbar
                progress.visibility = View.VISIBLE
                btnSave.isEnabled = false

                // siapkan File gambar
                val parcelFileDescriptor = applicationContext.contentResolver.openFileDescriptor(fileUri!!, "r", null)
                val fileName = applicationContext.contentResolver.getFileName(fileUri!!)
                val fileType = applicationContext.contentResolver.getType(fileUri!!)
                val file = File(
                    applicationContext.cacheDir,
                    fileName
                )
                val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)

                // kirim data ke Server API
                ApiCreateBlog(URL("https://crud.trisnawan.my.id/blog/create")) { result ->
                    progress.visibility = View.GONE
                    if (result) {
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Upload failed", Toast.LENGTH_SHORT).show()
                    }
                }.addFormField(
                    "title", title.text.toString()
                ).addFormField(
                    "content", content.text.toString()
                ).addFilePart(
                    "image", file, fileName, fileType!!
                ).execute()
            } else {
                // jika masih ada yang kosong
                btnSave.isEnabled = true
                Toast.makeText(applicationContext, "Silahkan isi dulu semua data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // untuk menangkap hasil pilihan file yang dipilih user
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // requestCode sama dengan isi startActivityForResult di image.setOnClickListener
        if (requestCode == 2 && resultCode == Activity.RESULT_OK){

            // matikan progressbar
            progress.visibility = View.GONE

            // simpan alamat gambar yang telah dipilih
            data?.data?.also { uri ->
                fileUri = uri
                val inputStream: InputStream? = contentResolver?.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                if (bytes != null) {

                    // ubah gambar terpilih ke Bitmap
                    val parcelFileDescriptor: ParcelFileDescriptor =
                        contentResolver?.openFileDescriptor(uri, "r")!!
                    val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
                    val imageBitmap: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                    parcelFileDescriptor.close()

                    // tampilkan gambar terpilih ke ImageView
                    image.setImageBitmap(imageBitmap)
                }
            }
        }
    }
}