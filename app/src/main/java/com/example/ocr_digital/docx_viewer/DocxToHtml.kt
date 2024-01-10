package com.example.ocr_digital.docx_viewer

import android.content.Context
import android.net.Uri
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

//object DocxToHtml {
//
//    fun convertToHtml(context: Context, docxUri: Uri): String? {
//        try {
//            val wordMLPackage = WordprocessingMLPackage.load(File(docxFilePath))
//            val html = org.docx4j.convert.out.html.HtmlExporterNonXSLT.word2007ToHtml(wordMLPackage)
//
//            // Optionally, you can perform additional preprocessing
//            return Preprocess.process(html)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return null
//    }
//
//}