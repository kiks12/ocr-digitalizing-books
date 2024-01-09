package com.example.ocr_digital.file_saver

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileSaver {

    private fun saveTextToDocx(context: Context, text: String, filename: String) : Uri? {
        try {
            val document = XWPFDocument()
            val paragraph = document.createParagraph()

            val run = paragraph.createRun()
            run.setText(text)

            val outputPath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "$filename.docx")
            val outputStream = FileOutputStream(outputPath)
            document.write(outputStream)

            document.close()
            outputStream.close()

            return FileProvider.getUriForFile(context, "${context.packageName}.provider", outputPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    private fun saveTextToPng(context: Context, text: String, filename: String) : Uri? {
        try {
            // Create a bitmap with desired dimensions
            val bitmap = Bitmap.createBitmap(800, 600, Bitmap.Config.ARGB_8888)

            // Create a canvas to draw on the bitmap
            val canvas = android.graphics.Canvas(bitmap)

            // Set background color
            canvas.drawColor(android.graphics.Color.WHITE)

            // Set text color and size
            val paint = android.graphics.Paint()
            paint.color = android.graphics.Color.BLACK
            paint.textSize = 24f

            // Draw text on the canvas
            canvas.drawText(text, 50f, 100f, paint)

            // Save the bitmap as a PNG file
            val outputPath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$filename.png")
            val outputStream = FileOutputStream(outputPath)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            // Close the stream
            outputStream.close()

            // Return the URI of the created file
            return FileProvider.getUriForFile(context, "${context.packageName}.provider", outputPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    private fun saveTextToJpeg(context: Context, text: String, filename: String) : Uri? {
        try {
            // Create a bitmap with desired dimensions
            val bitmap = Bitmap.createBitmap(800, 600, Bitmap.Config.ARGB_8888)

            // Create a canvas to draw on the bitmap
            val canvas = android.graphics.Canvas(bitmap)

            // Set background color
            canvas.drawColor(android.graphics.Color.WHITE)

            // Set text color and size
            val paint = android.graphics.Paint()
            paint.color = android.graphics.Color.BLACK
            paint.textSize = 24f

            // Draw text on the canvas
            canvas.drawText(text, 50f, 100f, paint)

            // Save the bitmap as a JPEG file
            val outputPath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$filename.jpg")
            val outputStream = FileOutputStream(outputPath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            // Close the stream
            outputStream.close()

            // Return the URI of the created file
            return FileProvider.getUriForFile(context, "${context.packageName}.provider", outputPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    private fun saveTextToPdf(context: Context, text: String, filename: String) : Uri? {
        try {
            val document = PDDocument()
            var remainingText = text

            while (remainingText.isNotEmpty()) {
                val page = PDPage()
                document.addPage(page)

                val contentStream = PDPageContentStream(document, page)
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12f)

                val (currentPageText, remaining) = getFitTextOnPage(remainingText, contentStream, page, PDType1Font.HELVETICA_BOLD, 12f)
                contentStream.beginText()
                contentStream.newLineAtOffset(20f, page.mediaBox.upperRightY - 20f)
                contentStream.showText(currentPageText)
                contentStream.endText()
                contentStream.close()

                remainingText = remaining
            }

            val outputPath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "$filename.pdf")
            val outputStream = FileOutputStream(outputPath)
            document.save(outputStream)
            document.close()
            outputStream.close()

            return FileProvider.getUriForFile(context, "${context.packageName}.provider", outputPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return  null
    }

    private fun getFitTextOnPage(text: String, contentStream: PDPageContentStream, page: PDPage, font: PDFont, fontSize: Float): Pair<String, String> {
        val maxWidth = page.mediaBox.width - 40f // Adjust for margins
        val fontWidth = font.getStringWidth(text) * fontSize / 1000

        if (fontWidth <= maxWidth) {
            // The entire text fits on the current page
            return Pair(text, "")
        }

        // Find the substring that fits on the current page
        var substring = text
        var remaining = ""
        while (font.getStringWidth(substring) * fontSize / 1000 > maxWidth) {
            val lastIndex = substring.lastIndexOf(' ')
            if (lastIndex == -1) {
                // No space found, break the word
                break
            }
            substring = substring.substring(0, lastIndex)
            remaining = text.substring(lastIndex + 1)
        }

        return Pair(substring, remaining)
    }


    fun saveTextToFile(context: Context, text: String, filename: String, type: FileType) : Uri? {
        return when (type) {
            FileType.DOCX -> saveTextToDocx(context, text, filename)
            FileType.PDF -> saveTextToPdf(context, text, filename)
            FileType.JPEG -> saveTextToJpeg(context, text, filename)
            FileType.PNG -> saveTextToPng(context, text, filename)
        }
    }
}