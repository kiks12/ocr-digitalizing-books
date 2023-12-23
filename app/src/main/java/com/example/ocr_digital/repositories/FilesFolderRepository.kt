package com.example.ocr_digital.repositories

import android.util.Log
import com.example.ocr_digital.models.Response
import com.example.ocr_digital.models.ResponseStatus
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class FilesFolderRepository {
    private val storage = Firebase.storage("gs://ocr-digital-book.appspot.com")

    private fun removeLastSegment(input: String): String {
        val lastSlashIndex = input.lastIndexOf("/")
        return if (lastSlashIndex != -1) {
            input.substring(0, lastSlashIndex)
        } else {
            input
        }
    }

    private fun getLastSegment(input: String): String {
        val lastSlashIndex = input.lastIndexOf("/")
        return if (lastSlashIndex != -1 && lastSlashIndex < input.length - 1) {
            input.substring(lastSlashIndex + 1)
        } else {
            input
        }
    }

    suspend fun createFolder(directory: String) : Response{
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO){
                val reference = storage.reference
                val parentDir = removeLastSegment(directory)
                val newDirectory = getLastSegment(directory)
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

    suspend fun deleteFileOrFolder(directory: String) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO){
                val reference = storage.reference
                reference.child(directory).delete()
                    .addOnSuccessListener {
                        response.complete(
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                message = "Folder successfully deleted"
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
}