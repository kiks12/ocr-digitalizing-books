package com.example.ocr_digital.repositories

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.ocr_digital.models.Response
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.path.PathUtilities
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File

class FilesFolderRepository {
    private val storage = Firebase.storage("gs://ocr-digital-book.appspot.com")
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

    suspend fun deleteFile(path: String) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO){
                val reference = storage.reference
                reference.child(path).delete()
                    .addOnSuccessListener {
                        response.complete(
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                message = "File successfully deleted"
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

    suspend fun getFilesAndFolders(directory: String) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO){
                val reference = storage.reference
                reference.child(directory).listAll()
                    .addOnSuccessListener { results ->
                        val items = results.items
                        val prefixes = results.prefixes
                        val removedEmptyItems = items.filter { item -> item.name != "EMPTY" }
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

    suspend fun renameFile(currentPath: String, newPath: String) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO) {
                val reference = storage.reference
                val currentFileRef = reference.child(currentPath)
                val parentDir = PathUtilities.removeLastSegment(currentPath)
                val newFileRef = reference.child(newPath)

                reference.child(parentDir).listAll()
                    .addOnSuccessListener { result ->
                        val items = result.items
                        val filteredItem = items.filter { item -> item.name == PathUtilities.getLastSegment(newPath) }

                        if (filteredItem.isNotEmpty()) {
                            response.complete(
                                Response(
                                    status = ResponseStatus.FAILED,
                                    message = "File name already used"
                                )
                            )
                            return@addOnSuccessListener
                        }

                        scope.launch {
                            withContext(Dispatchers.IO) {
                                val localFile = File.createTempFile("temp", null)
                                currentFileRef.getFile(localFile)
                                    .addOnSuccessListener {
                                        newFileRef.putFile(localFile.toUri())
                                            .addOnSuccessListener {
                                                currentFileRef.delete()
                                                    .addOnSuccessListener {
                                                        response.complete(
                                                            Response(
                                                                status = ResponseStatus.SUCCESSFUL,
                                                                message = "Successfully renamed file"
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
                    }
            }
        }

        return response.await()
    }

    suspend fun uploadFile(filePath: String, fileUri: Uri) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            val storageRef = storage.reference
            val directoryRef = storageRef.child("$filePath/${fileUri.lastPathSegment}")
            directoryRef.putFile(fileUri)
                .addOnSuccessListener {
                    response.complete(
                        Response(
                            status = ResponseStatus.SUCCESSFUL,
                            message = "File uploaded successfully"
                        )
                    )
                }
                .addOnFailureListener {
                    it.localizedMessage?.let { it1 ->
                        Response(
                            status = ResponseStatus.SUCCESSFUL,
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
}