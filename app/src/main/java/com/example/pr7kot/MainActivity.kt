package com.example.pr7kot

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        val mainView = findViewById<View>(R.id.main) //  ID корневого View в activity_main.xml
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val button = findViewById<Button>(R.id.button)
        val input = findViewById<EditText>(R.id.textInput)


        button.setOnClickListener {
            val imageUrl = input.text.toString()
            if (imageUrl.isNotEmpty()) {
                downloadAndSaveImage(imageUrl, this)
            } else {
                Toast.makeText(this, "Введите ссылку на изображение", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun downloadAndSaveImage(imageUrl: String, context: Context) {
        CoroutineScope(Dispatchers.Main).launch {
            val bitmap = downloadImage(imageUrl).await()
            if (bitmap != null) {
                val imageView = findViewById<ImageView>(R.id.imageView)
                imageView.setImageBitmap(bitmap)
                saveImageToDisk(bitmap, context)
            } else {
                Toast.makeText(context, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun downloadImage(imageUrl: String): Deferred<Bitmap?> {
        return CoroutineScope(Dispatchers.IO).async {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection()
                connection.doInput = true
                connection.connect()
                val input = connection.getInputStream()
                BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun saveImageToDisk(bitmap: Bitmap, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val file = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "downloaded_image.jpg"
                )
                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Изображение сохранено", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Ошибка сохранения изображения", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
