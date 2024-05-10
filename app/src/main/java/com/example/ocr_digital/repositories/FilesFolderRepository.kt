package com.example.ocr_digital.repositories

import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.ocr_digital.file_saver.FileMetadata
import com.example.ocr_digital.models.Response
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.models.SearchFilesFoldersResult
import com.example.ocr_digital.path.PathUtilities
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class FilesFolderRepository {
    private val storage = Firebase.storage("gs://ocr-digital-book.appspot.com")
    private val db = Firebase.firestore
    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    suspend fun createFolder(directory: String) : Response{
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO){
                val reference = storage.reference
                val parentDir = PathUtilities.removeLastSegment(directory)
                val newDirectory = PathUtilities.getLastSegment(directory)
                val parentDirectoryRef = reference.child(parentDir)
                parentDirectoryRef.listAll()
                    .addOnSuccessListener { result ->
                        val prefixes = result.prefixes
                        val directoryPrefix = prefixes.filter { prefix -> prefix.name == newDirectory }
                        if (directoryPrefix.isEmpty()) {
                            val directoryRef = reference.child("$directory/EMPTY")
                            directoryRef.putBytes(byteArrayOf())
                                .addOnSuccessListener {
                                    response.complete(
                                        Response(
                                            status = ResponseStatus.SUCCESSFUL,
                                            message = "Successfully created directory"
                                        )
                                    )
                                }
                                .addOnFailureListener { exception ->
                                    exception.localizedMessage?.let {
                                        Response(
                                            status = ResponseStatus.FAILED,
                                            message = it
                                        )
                                    }?.let {
                                        response.complete(
                                            it
                                        )
                                    }
                                }
                        } else {
                            response.complete(
                                Response(
                                    status = ResponseStatus.FAILED,
                                    message = "Folder already exists"
                                )
                            )
                        }
                }
            }
        }

        return response.await()
    }

    private fun getAssociatedTextFilePath(path: String) : String {
        val extension = PathUtilities.getFileExtension(path)
        val filePath = path.substringBeforeLast(".$extension")
        return "$filePath.txt"
    }

    suspend fun deleteFileMetadata(path: String) : Response {
        val response = CompletableDeferred<Response>(null)

        try {
            coroutineScope {
                launch(Dispatchers.IO){
                    db.collection("books").document(path).delete().await()
                    response.complete(
                        Response(
                            status = ResponseStatus.SUCCESSFUL,
                            message = "File successfully deleted"
                        )
                    )
                }
            }
            return response.await()
        } catch (e: Exception) {
            response.complete(
                Response(
                    status = ResponseStatus.FAILED,
                    message = e.localizedMessage!!
                )
            )
        }
        return response.await()
    }

    suspend fun deleteFile(path: String) : Response {
        val response = CompletableDeferred<Response>(null)
        val textFilePath = getAssociatedTextFilePath(path)
        try {
            coroutineScope {
                launch(Dispatchers.IO){
                    val reference = storage.reference
                    reference.child(path).delete().await()
                    reference.child(textFilePath).delete().await()
                    response.complete(
                        Response(
                            status = ResponseStatus.SUCCESSFUL,
                            message = "File successfully deleted"
                        )
                    )
                }
            }

            return response.await()
        } catch (e: Exception) {
            response.complete(
                Response(
                    status = ResponseStatus.FAILED,
                    message = e.localizedMessage!!
                )
            )
        }
        return response.await()
    }

    suspend fun deleteFolder(path: String) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO) {
                val reference = storage.reference
                reference.child(path).listAll()
                    .addOnSuccessListener { result ->
                        result.items.forEach { item ->
                            item.delete()
                                .addOnSuccessListener {
                                    Log.w(
                                        "DELETE FOLDER 1",
                                        "DELETED"
                                    )
                                }
                                .addOnFailureListener {
                                    response.complete(
                                        Response(
                                            status = ResponseStatus.FAILED,
                                            message = "There is an error, check if folder is deleted"
                                        )
                                    )
                                }
                        }

                        result.prefixes.forEach { prefix ->
                            scope.launch {
                                deleteFolder(prefix.path)
                            }
                        }

                        response.complete(
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                message = "Successfully deleted folder"
                            )
                        )
                    }
                    .addOnFailureListener {
                        it.localizedMessage?.let { it1 ->
                            Response(
                                status = ResponseStatus.FAILED,
                                message = it1
                            )
                        }?.let { it2 ->
                            response.complete(
                                it2
                            )
                        }
                    }
            }
        }

        return response.await()
    }

    suspend fun getFolders(directory: String) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO){
                val reference = storage.reference
                reference.child(directory).listAll()
                    .addOnSuccessListener { results ->
                        val prefixes = results.prefixes
                        response.complete(
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                message = "Successfully retrieved folders of $directory",
                                data = mapOf(
                                    "FOLDERS" to prefixes
                                )
                            )
                        )
                    }
                    .addOnFailureListener {
                        it.localizedMessage?.let { it1 -> Log.w("PREFIXES", it1) }
                        it.localizedMessage?.let { it1 ->
                            Response(
                                status = ResponseStatus.FAILED,
                                message = it1
                            )
                        }?.let { it2 ->
                            response.complete(
                                it2
                            )
                        }
                    }
            }
        }

        return response.await()
    }

    suspend fun getFilesAndFolders(directory: String) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO){
                val reference = storage.reference
                reference.child(directory).listAll()
                    .addOnSuccessListener { results ->
                        val items = results.items
                        val prefixes = results.prefixes
                        val removedEmptyItems = items.filter { item -> item.name != "EMPTY" && item.name.endsWith(".txt").not() }
                        response.complete(
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                message = "Successfully retrieved files and folders of $directory",
                                data = mapOf(
                                    "FILES" to removedEmptyItems,
                                    "FOLDERS" to prefixes
                                )
                            )
                        )
                    }
                    .addOnFailureListener {
                        it.localizedMessage?.let { it1 -> Log.w("PREFIXES", it1) }
                        it.localizedMessage?.let { it1 ->
                            Response(
                                status = ResponseStatus.FAILED,
                                message = it1
                            )
                        }?.let { it2 ->
                            response.complete(
                                it2
                            )
                        }
                    }
            }
        }

        return response.await()
    }

    suspend fun renameFolder(currentPath: String, newPath: String) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO) {
                val reference = storage.reference
                val currentFolderRef = reference.child(currentPath)
                val newFolderRef = reference.child(newPath)

                newFolderRef.child("EMPTY").putBytes(byteArrayOf()).await()
                currentFolderRef.listAll()
                    .addOnSuccessListener { result ->
                        scope.launch {
                            async {
                                result.items.forEach { item ->
                                    val localFile = File.createTempFile("temp", null)
                                    item.getFile(localFile)
                                        .addOnSuccessListener {
                                            newFolderRef.child(item.name).putFile(localFile.toUri())
                                                .addOnSuccessListener {
                                                    item.delete()
                                                        .addOnSuccessListener {  }
                                                        .addOnFailureListener {
                                                            response.complete(
                                                                Response(
                                                                    status = ResponseStatus.FAILED,
                                                                    message = "There is an error, please check if the folder is renamed"
                                                                )
                                                            )
                                                        }
                                                }
                                                .addOnFailureListener {
                                                    response.complete(
                                                        Response(
                                                            status = ResponseStatus.FAILED,
                                                            message = "There is an error, please check if the folder is renamed"
                                                        )
                                                    )
                                                }
                                        }
                                }
                            }.await()

                            async {
                                result.prefixes.forEach { prefix ->
                                    scope.launch {
                                        renameFolder(prefix.path, "$newPath/${PathUtilities.getLastSegment(prefix.path)}")
                                    }
                                }
                            }.await()

                            async {
                                response.complete(
                                    Response(
                                        status = ResponseStatus.SUCCESSFUL,
                                        message = "Successfully renamed folder"
                                    )
                                )
                            }.await()
                        }
                    }
                    .addOnFailureListener {
                        it.localizedMessage?.let { it1 ->
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                it1
                            )
                        }?.let { it2 ->
                            response.complete(
                                it2
                            )
                        }
                    }
            }
        }

        return response.await()
    }

    private suspend fun getFilenameAvailability(filePath: String) : Boolean{
        val response = CompletableDeferred<Boolean>(null)
        val parentDir = PathUtilities.removeLastSegment(filePath)
        val filename = PathUtilities.getLastSegment(filePath)

        try {
            val reference = storage.reference
            val parentDirList = reference.child(parentDir).listAll().await()

            parentDirList.items.forEach { item ->
                if (item.name == filename) {
                    response.complete(false)
                }
            }

            response.complete(true)
        } catch (e: Exception) {
            response.complete(false)
        }

        return response.await()
    }

    suspend fun copyFile(currentPath: String, newPath: String) : Response {
        val response = CompletableDeferred<Response>(null)

        try {
            coroutineScope {
                launch(Dispatchers.IO) {
                    val reference = storage.reference
                    val currentFileRef = reference.child(currentPath)
                    val newFileRef = reference.child(newPath)
                    val currentTextFilePath = getAssociatedTextFilePath(currentPath)
                    val newTextFilePath = getAssociatedTextFilePath(newPath)
                    val currentTextFileRef = reference.child(currentTextFilePath)
                    val newTextFileRef = reference.child(newTextFilePath)

                    val filenameAvailable = getFilenameAvailability(newPath)

                    if (!filenameAvailable) {
                        response.complete(
                            Response(
                                status = ResponseStatus.FAILED,
                                message = "File name already in use"
                            )
                        )
                    } else {
                        val localFile = File.createTempFile("temp", null)
                        currentFileRef.getFile(localFile).await()
                        newFileRef.putFile(localFile.toUri()).await()

                        val localTextFile = File.createTempFile("textTemp", null)
                        currentTextFileRef.getFile(localTextFile).await()
                        newTextFileRef.putFile(localTextFile.toUri()).await()

                        response.complete(
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                message = "Successfully copied file"
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            response.complete(
                Response(
                    status = ResponseStatus.FAILED,
                    message = e.localizedMessage!!
                )
            )
        } catch (e: IOException) {
            response.complete(
                Response(
                    status = ResponseStatus.FAILED,
                    message = e.localizedMessage!!
                )
            )
        }

        return response.await()
    }

    suspend fun renameFile(currentPath: String, newPath: String) : Response {
        val response = CompletableDeferred<Response>(null)

        try {
            coroutineScope {
                launch(Dispatchers.IO) {
                    val reference = storage.reference
                    val currentFileRef = reference.child(currentPath)
                    val newFileRef = reference.child(newPath)
                    val currentTextFilePath = getAssociatedTextFilePath(currentPath)
                    val newTextFilePath = getAssociatedTextFilePath(newPath)
                    val currentTextFileRef = reference.child(currentTextFilePath)
                    val newTextFileRef = reference.child(newTextFilePath)

                    val filenameAvailable = getFilenameAvailability(newPath)

                    if (!filenameAvailable) {
                        response.complete(
                            Response(
                                status = ResponseStatus.FAILED,
                                message = "File name already in use"
                            )
                        )
                    } else {
                        val localFile = File.createTempFile("temp", null)
                        currentFileRef.getFile(localFile).await()
                        newFileRef.putFile(localFile.toUri()).await()
                        currentFileRef.delete().await()

                        val localTextFile = File.createTempFile("textTemp", null)
                        currentTextFileRef.getFile(localTextFile).await()
                        newTextFileRef.putFile(localTextFile.toUri()).await()
                        currentTextFileRef.delete().await()

                        response.complete(
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                message = "Successfully renamed file"
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            response.complete(
                Response(
                    status = ResponseStatus.FAILED,
                    message = e.localizedMessage!!
                )
            )
        } catch (e: IOException) {
            response.complete(
                Response(
                    status = ResponseStatus.FAILED,
                    message = e.localizedMessage!!
                )
            )
        }

        return response.await()
    }

    suspend fun getFileMetadata(path: String) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            db.collection("books").document(path).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.data == null) {
                        response.complete(
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                message = "Successfully fetched file metadata",
                                data = mapOf(
                                    "metadata" to FileMetadata("","","","","", Timestamp.now()) as Any
                                )
                            )
                        )
                    } else {
                        response.complete(
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                message = "Successfully fetched file metadata",
                                data = mapOf(
                                    "metadata" to snapshot.toObject(FileMetadata::class.java) as Any
                                )
                            )
                        )
                    }
                }.addOnFailureListener {
                    it.localizedMessage?.let { it1 ->
                        Response(
                            status = ResponseStatus.FAILED,
                            message = it1,
                        )
                    }?.let { it2 ->
                        response.complete(
                            it2
                        )
                    }
                }
        }

        return response.await()
    }

    suspend fun uploadFileMetadata(metadata: FileMetadata) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            db.collection("books").document(metadata.path).set(
                mapOf(
                    "title" to metadata.title,
                    "author" to metadata.author,
                    "publishedYear" to metadata.publishedYear,
                    "genre" to metadata.genre,
                    "path" to metadata.path,
                    "createdAt" to metadata.createdAt
                )
            ).addOnSuccessListener {
                response.complete(
                    Response(
                        status = ResponseStatus.SUCCESSFUL,
                        message = "File Metadata uploaded"
                    )
                )
            }.addOnFailureListener {
                it.localizedMessage?.let { it1 ->
                    Response(
                        status = ResponseStatus.FAILED,
                        message = it1
                    )
                }?.let { it2 ->
                    response.complete(
                        it2
                    )
                }
            }

        }

        return response.await()
    }

    suspend fun updateFileMetadata(metadata: FileMetadata) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            db.collection("books").document(metadata.path).set(
                mapOf(
                    "title" to metadata.title,
                    "author" to metadata.author,
                    "publishedYear" to metadata.publishedYear,
                    "genre" to metadata.genre,
                    "path" to metadata.path,
                    "createdAt" to metadata.createdAt
                )
            ).addOnSuccessListener {
                response.complete(
                    Response(
                        status = ResponseStatus.SUCCESSFUL,
                        message = "Successfully updated file metadata"
                    )
                )
            }.addOnFailureListener {
                it.localizedMessage?.let { it1 ->
                    Response(
                        status = ResponseStatus.FAILED,
                        message = it1
                    )
                }?.let { it2 ->
                    response.complete(
                        it2
                    )
                }
            }

        }

        return response.await()
    }

    suspend fun uploadFile(filePath: String, fileUri: Uri) : Response {
        val response = CompletableDeferred<Response>(null)

        try {
            coroutineScope {
                launch(Dispatchers.IO) {
                    val filenameAvailable = getFilenameAvailability("$filePath/${fileUri.lastPathSegment}")

                    if (filenameAvailable) {
                        val storageRef = storage.reference
                        val directoryRef = storageRef.child("$filePath/${fileUri.lastPathSegment}")
                        directoryRef.putFile(fileUri).await()

                        response.complete(
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                message = "File uploaded successfully"
                            )
                        )
                    } else {
                        response.complete(
                            Response(
                                status = ResponseStatus.FAILED,
                                message = "File name already in use"
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            response.complete(
                Response(
                    status = ResponseStatus.SUCCESSFUL,
                    message = e.localizedMessage!!
                )
            )
        } catch (e: IOException) {
            response.complete(
                Response(
                    status = ResponseStatus.SUCCESSFUL,
                    message = e.localizedMessage!!
                )
            )
        }

        return response.await()
    }

    suspend fun openFirebaseDocument(context: Context, path: String, mimeType: String) {
        coroutineScope{
            launch(Dispatchers.IO) {
                val storageReference = storage.reference

                if (PathUtilities.getFileExtension(path) != "docx") {
                    val tempUrlTask: Task<Uri> = storageReference.child(path).downloadUrl
                    Tasks.await(tempUrlTask, 10, TimeUnit.SECONDS)
                    if (tempUrlTask.isSuccessful) {
                        val downloadUrl = tempUrlTask.result as Uri

                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setDataAndType(downloadUrl, mimeType)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        context.startActivity(intent)
                    }
                } else {
                    val tempFile = File(context.cacheDir, PathUtilities.getLastSegment(path))
                    val downloadTask = storageReference.child(path).getFile(tempFile)
                    downloadTask.addOnSuccessListener {
                        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)

                        try {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(uri, mimeType)
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, "No Activity found to open file, download the file and use other apps to view", Toast.LENGTH_LONG).show()
                        }
                    }.addOnFailureListener {
                        it.printStackTrace()
                    }
                }
            }
        }
    }

    suspend fun downloadFile(context: Context, path: String) {
        coroutineScope {
            val storageRef = storage.reference
            val fileRef = storageRef.child(path).downloadUrl.await()
            val filename = PathUtilities.getLastSegment(path)

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val destinationUri = Uri.withAppendedPath(Uri.fromFile(downloadFolder), filename)

            val request = DownloadManager.Request(Uri.parse(fileRef.toString()))
                .setTitle(filename)
                .setDestinationUri(destinationUri)

            val downloadId = downloadManager.enqueue(request)

            Toast.makeText(context, "File downloaded - DID($downloadId)", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun printFile(context: Context, path: String) {
        coroutineScope {
            val storageRef = storage.reference
            val tempFile = File(context.cacheDir, PathUtilities.getLastSegment(path))
            val downloadTask = storageRef.child(path).getFile(tempFile)
            downloadTask
                .addOnSuccessListener {
                    try {
                        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
                        val uri = FileProvider.getUriForFile(
                            context,
                            context.packageName + ".provider",
                            tempFile
                        )

                        val printAdapter = object : PrintDocumentAdapter() {
                            override fun onLayout(
                                oldAttributes: PrintAttributes?,
                                newAttributes: PrintAttributes?,
                                cancellationSignal: CancellationSignal?,
                                callback: LayoutResultCallback?,
                                extras: Bundle?
                            ) {
                                if (cancellationSignal?.isCanceled == true) {
                                    callback?.onLayoutCancelled()
                                    return
                                }

                                val info = PrintDocumentInfo.Builder(tempFile.name)
                                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                                    .build()

                                callback?.onLayoutFinished(info, true)
                            }

                            override fun onWrite(
                                pages: Array<out PageRange>?,
                                destination: ParcelFileDescriptor?,
                                cancellationSignal: CancellationSignal?,
                                callback: WriteResultCallback?
                            ) {
                                val input = context.contentResolver.openInputStream(uri)
                                val output = FileOutputStream(destination?.fileDescriptor)

                                input?.use { inputStream ->
                                    output.use { outputStream ->
                                        inputStream.copyTo(outputStream)
                                    }
                                }
                                callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                            }
                        }

                        val jobName = "${context.packageName} - ${tempFile.name}"

                        printManager.print(
                            jobName,
                            printAdapter,
                            PrintAttributes.Builder().build()
                        )
                    } catch (e: IOException) {
                        Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                    } catch (e: RuntimeException) {
                        Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        }
    }

    suspend fun searchFilesFolders(query: String, directory: String) : SearchFilesFoldersResult {
        val filesAndFolders = CompletableDeferred<SearchFilesFoldersResult>(null)
        val queryLowercase = query.lowercase()

        coroutineScope {
            val storageRef = storage.reference
            val tempFiles = ArrayList<StorageReference>()
            val tempFolders = ArrayList<StorageReference>()
            val firstList = storageRef.child(directory).listAll().await()

            firstList.items.forEach { item ->
                if (item.name.lowercase().contains(queryLowercase) && item.name != "EMPTY") tempFiles.add(item)
            }

            firstList.prefixes.forEach { prefix ->
                if (prefix.name.lowercase().contains(queryLowercase)) tempFolders.add(prefix)
                val prefixList = prefix.listAll().await()
                prefixList.items.forEach { item ->
                    if (item.name.lowercase().contains(queryLowercase) && item.name != "EMPTY") tempFiles.add(item)
                }

                prefixList.prefixes.forEach { prefixTwo ->
                    if (prefixTwo.name.lowercase().contains(queryLowercase)) tempFolders.add(prefixTwo)
                    val prefixTwoList = prefixTwo.listAll().await()

                    prefixTwoList.items.forEach { item ->
                        if (item.name.lowercase().contains(queryLowercase) && item.name != "EMPTY") tempFiles.add(item)
                    }
                    prefixTwoList.prefixes.forEach { prefixThree ->
                        if (prefixThree.name.lowercase().contains(queryLowercase)) tempFolders.add(prefixThree)
                    }
                }
            }

            filesAndFolders.complete(SearchFilesFoldersResult(files = tempFiles, folders = tempFolders))
        }

        return filesAndFolders.await()
    }

    suspend fun readTextFromTxtFile(path: String) : String {
        val completable = CompletableDeferred<String>(null)

        coroutineScope {
            launch(Dispatchers.IO){
                try {
                    val storageRef = storage.reference
                    val fileRef = storageRef.child(path)

                    val bytes = fileRef.getBytes(Long.MAX_VALUE).await()
                    val content = String(bytes, Charsets.UTF_8)
                    completable.complete(content)
                } catch (e: IOException) {
                    e.printStackTrace()
                    completable.complete("")
                }
            }
        }

        return completable.await()
    }

}