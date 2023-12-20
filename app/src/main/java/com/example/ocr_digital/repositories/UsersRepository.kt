package com.example.ocr_digital.repositories

import com.example.ocr_digital.models.Response
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.models.UserInformation
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class UsersRepository {
    private val db = Firebase.firestore

    suspend fun saveUserInformation(newUser: UserInformation) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO) {
                val ref = db.collection("profiles").document()
                ref.set(newUser)
                    .addOnSuccessListener {
                        response.complete(
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                message = "Successfully saved user information"
                            )
                        )
                    }
                    .addOnFailureListener {
                        response.complete(
                            Response(
                                status = ResponseStatus.FAILED,
                                message = it.message.toString()
                            )
                        )
                    }
            }
        }

        return response.await()
    }
}