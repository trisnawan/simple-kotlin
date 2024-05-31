package com.trisnasejati.testingapps.api.model

import android.os.AsyncTask
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL

class ApiCreateBlog(private val url: URL, private val callback: (Boolean) -> Unit) : AsyncTask<String, Void, Boolean>() {

    companion object {
        private const val LINE_FEED = "\r\n"
        private const val maxBufferSize = 1024 * 1024
        private const val charset = "UTF-8"
    }

    // mempersiapkan MultiPart Forms
    private val boundary: String = "===" + System.currentTimeMillis() + "==="
    private val httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
    private var outputStream: OutputStream
    private var writer: PrintWriter

    init {
        // inisialisasi koneksi
        httpConnection.setRequestProperty("Accept-Charset", "UTF-8")
        httpConnection.setRequestProperty("Connection", "Keep-Alive")
        httpConnection.setRequestProperty("Cache-Control", "no-cache")
        httpConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary)
        httpConnection.setChunkedStreamingMode(maxBufferSize)
        httpConnection.doInput = true
        httpConnection.doOutput = true
        httpConnection.useCaches = false
        outputStream = httpConnection.outputStream
        writer = PrintWriter(OutputStreamWriter(outputStream, charset), true)
    }

    // dipanggil untuk menambahkan field $_POST
    fun addFormField(name: String, value: String): ApiCreateBlog {
        writer.append("--").append(boundary).append(LINE_FEED)
        writer.append("Content-Disposition: form-data; name=\"")
            .append(name)
            .append("\"")
            .append(LINE_FEED)
        writer.append(LINE_FEED)
        writer.append(value).append(LINE_FEED)
        writer.flush()
        return this
    }

    // di panggil untuk menambahkan field $_FILES
    @Throws(IOException::class)
    fun addFilePart(fieldName: String, uploadFile: File, fileName: String, fileType: String): ApiCreateBlog {
        writer.append("--").append(boundary).append(LINE_FEED)
        writer.append("Content-Disposition: file; name=\"")
            .append(fieldName).append("\"; filename=\"")
            .append(fileName).append("\"")
            .append(LINE_FEED)
        writer.append("Content-Type: ").append(fileType).append(LINE_FEED)
        writer.append(LINE_FEED)
        writer.flush()

        val inputStream = FileInputStream(uploadFile)
        inputStream.copyTo(outputStream, maxBufferSize)

        outputStream.flush()
        inputStream.close()
        writer.append(LINE_FEED)
        writer.flush()
        return this
    }

    override fun doInBackground(vararg params: String?): Boolean {
        // tutup fields Multipart
        writer.append(LINE_FEED).flush()
        writer.append("--").append(boundary).append("--").append(LINE_FEED)
        writer.close()

        // kirim data ke Server API
        try {
            val status = httpConnection.responseCode
            if (status == HttpURLConnection.HTTP_OK || status == HttpURLConnection.HTTP_CREATED) {
                try {
                    val inputStream = httpConnection.inputStream
                    val jsonString = inputStream.bufferedReader().use { it.readText() }
                    val jsonObject = JSONObject(jsonString)
                } finally {
                    httpConnection.disconnect()
                }
            }else{
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        callback(result)
    }
}