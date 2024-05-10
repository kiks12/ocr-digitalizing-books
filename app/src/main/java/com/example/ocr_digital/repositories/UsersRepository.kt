package com.example.ocr_digital.repositories

import android.util.Log
import com.example.ocr_digital.models.Response
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.models.UserInformation
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class UsersRepository {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

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

    suspend fun getPublicAdminUser() : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO) {
                val ref = db.collection("profiles")
                    .whereEqualTo("firstName", "Public")
                    .whereEqualTo("lastName", "Admin").limit(1).get()
                ref.addOnSuccessListener { snapshot ->
                    response.complete(
                        Response(
                            status = ResponseStatus.SUCCESSFUL,
                            message = "",
                            data = mapOf(
                                "user" to snapshot.toObjects(UserInformation::class.java)
                            )
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

    suspend fun getUser(uid: String) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO){
                val ref = db.collection("profiles").whereEqualTo("profileId", uid).limit(1).get()
                ref.addOnSuccessListener { snapshot ->
                        response.complete(
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                message = "Successfully retrieved user information",
                                data = mapOf(
                                    "user" to snapshot.toObjects(UserInformation::class.java)
                                )
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

    suspend fun getUsers() : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO){
                val ref = db.collection("profiles").get()
                ref.addOnSuccessListener { snapshot ->
                    response.complete(
                        Response(
                            status = ResponseStatus.SUCCESSFUL,
                            message = "Successfully retrieved users",
                            data = mapOf(
                                "users" to snapshot.toObjects(UserInformation::class.java)
                            )
                        )
                    )
                }.addOnFailureListener {
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

    suspend fun getUid(authUid: String) : String? {
        val response = CompletableDeferred<String?>(null)

        coroutineScope {
            launch(Dispatchers.IO) {
                val ref = db.collection("profiles").whereEqualTo("profileId", authUid).limit(1).get()
                ref.addOnSuccessListener {
                        Log.w("USERS REPO", it.isEmpty.toString())
                        if (it.size() == 0 || it.isEmpty || it == null) response.complete(null)
                        else response.complete(it.documents[0].id)
                    }
                    .addOnFailureListener {
                        response.complete(null)
                    }

            }
        }

        return response.await()
    }

    suspend fun changePassword(currentPassword: String, newPassword: String) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO) {
                val currentUser = auth.currentUser

                if (currentUser != null) {
                    val credentials = EmailAuthProvider.getCredential(currentUser.email!!, currentPassword)
                    currentUser.reauthenticate(credentials)
                        .addOnSuccessListener {
                            currentUser.updatePassword(newPassword)
                                .addOnSuccessListener {
                                    response.complete(
                                        Response(
                                            status = ResponseStatus.SUCCESSFUL,
                                            message = "Successfully changed password"
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
            }
        }

        return response.await()
    }

    suspend fun resetPassword(email: String) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO) {
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        response.complete(
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                message = "Password reset email sent to $email"
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
        }

        return response.await()
    }

    suspend fun onboardUser(uid: String) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO) {
                val ref = db.collection("profiles").document(uid)
                ref.update("onboarding", false)
                    .addOnSuccessListener {
                        response.complete(
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                message = "Successfully onboard user"
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

    suspend fun finishUserWalkthrough(uid: String) : Response {
        val response = CompletableDeferred<Response>(null)

        coroutineScope {
            launch(Dispatchers.IO) {
                val ref = db.collection("profiles").document(uid)
                ref.update("walkthrough", false)
                    .addOnSuccessListener {
                        response.complete(
                            Response(
                                status = ResponseStatus.SUCCESSFUL,
                                message = "Finished user walkthrough"
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
}