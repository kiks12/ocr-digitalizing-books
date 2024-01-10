package com.example.ocr_digital.docx_viewer

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ocr_digital.R
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.InputStream

class DocxViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_docx_viewer)
    }

//    fun convertDocxToHtml(context: Context, docxUri: Uri): String? {
//        try {
//            val inputStream: InputStream = context.contentResolver.openInputStream(docxUri) ?: return null
//            val doc = XWPFDocument(inputStream)
//            return AndroidDocxToHtml().convertToHtml(doc)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return null
//    }
}