package com.example.ocr_digital.path

import java.io.File

class PathUtilities {
    companion object {
        fun removeFirstSegment(filePath: String): String {
            val firstSlashIndex = filePath.indexOf('/')
            if (firstSlashIndex != -1) {
                val secondSlashIndex = filePath.indexOf('/', firstSlashIndex + 1)
                if (secondSlashIndex != -1) {
                    return filePath.substring(secondSlashIndex + 1)
                }
            }
            return filePath
        }

        fun removeLastSegment(input: String): String {
            val lastSlashIndex = input.lastIndexOf("/")
            return if (lastSlashIndex != -1) {
                input.substring(0, lastSlashIndex)
            } else {
                input
            }
        }

        fun getLastSegment(input: String): String {
            val lastSlashIndex = input.lastIndexOf("/")
            return if (lastSlashIndex != -1 && lastSlashIndex < input.length - 1) {
                input.substring(lastSlashIndex + 1)
            } else {
                input
            }
        }

        fun getFileExtension(filePath: String): String {
            val file = File(filePath)
            val fileName = file.name
            val dotIndex = fileName.lastIndexOf('.')

            return if (dotIndex > 0 && dotIndex < fileName.length - 1) {
                fileName.substring(dotIndex + 1)
            } else {
                "" // No extension found or the file starts with a dot
            }
        }
    }
}