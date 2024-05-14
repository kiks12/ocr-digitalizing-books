package com.example.ocr_digital.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.ocr_digital.R
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.home.HomeActivity
import com.example.ocr_digital.ui.theme.OcrDigitalTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutionException

class LoginActivity : AppCompatActivity() {

    private val toastHelper = ToastHelper(this)
    private lateinit var auth : FirebaseAuth
    private lateinit var googleClient : GoogleSignInClient
    private val loading = mutableStateOf(false)

//    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        try {
//            GoogleSignIn.getSignedInAccountFromIntent(result.data)
//                .addOnSuccessListener { signedInAccount ->
//                    val credentials = GoogleAuthProvider.getCredential(signedInAccount.idToken, null)
//                    auth.signInWithCredential(credentials)
//                        .addOnSuccessListener {
//                            val intent = Intent(this, HomeActivity::class.java)
//                            startActivity(intent)
//                            finish()
//                            return@addOnSuccessListener
//                        }
//                        .addOnFailureListener {
//                            it.localizedMessage?.let { it1 -> toastHelper.makeToast(it1) }
//                        }
//
//                }
//                .addOnFailureListener {
//                    toastHelper.makeToast("There's something wrong please try again")
//                    toastHelper.makeToast(it.localizedMessage!!)
//                    toastHelper.makeToast(it.message!!)
//                }
//        } catch (e : ApiException) {
//            toastHelper.makeToast("There's something wrong please try again")
//            toastHelper.makeToast(e.localizedMessage!!)
//            e.localizedMessage?.let { Log.w("LOGIN ACTIVITY", it) }
//        }
//    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) startHomeActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.web_client_d))
            .build()
        googleClient = GoogleSignIn.getClient(applicationContext, googleSignInOptions)
        googleClient.signOut()

        val activityStarterHelper = ActivityStarterHelper(this)
        val loginViewModel = LoginViewModel(
            activityStarterHelper = activityStarterHelper,
            toastHelper = toastHelper,
            continueWithGoogle = ::continueWithGoogle
        )

        setContent {
            OcrDigitalTheme {
                if (loading.value) {
                    Scaffold {
                        Box(modifier = Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    LoginScreen(loginViewModel = loginViewModel)
                }
            }
        }

        supportActionBar?.hide()
    }

    @Suppress("DEPRECATION")
    private fun continueWithGoogle() {
        val googleIntent = googleClient.signInIntent
        startActivityForResult(googleIntent, 1000)
//        resultLauncher.launch(googleIntent)
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1000 -> {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    val googleSignInAccount = task.result
                    Log.w("GOOGLE SIGN IN", googleSignInAccount.email + " " + googleSignInAccount.idToken)

                    lifecycleScope.launch(Dispatchers.Default) {
                        withContext(Dispatchers.Main) {
                            loading.value = true
                        }
                    }

                    when {
                        googleSignInAccount.idToken != null && task.isSuccessful -> {
                            val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
                            auth.signInWithCredential(credential)
                                .addOnSuccessListener {
                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                    return@addOnSuccessListener
                                }
                                .addOnFailureListener {
                                    it.localizedMessage?.let { it1 -> toastHelper.makeToast(it1) }
                                }
                        }
                        else -> {
                            toastHelper.makeToast("There's something wrong please try again")
                        }
                    }

                } catch (e: ApiException) {
                    toastHelper.makeToast("There's something wrong please try again")
                    toastHelper.makeToast(e.localizedMessage!!)
                    e.localizedMessage?.let { Log.w("LOGIN ACTIVITY", it) }
                } catch (e: RuntimeExecutionException) {
                    toastHelper.makeToast("There's something wrong please try again")
                    toastHelper.makeToast(e.localizedMessage!!)
                    e.localizedMessage?.let { Log.w("LOGIN ACTIVITY", it) }
                } catch (e: ExecutionException) {
                    toastHelper.makeToast("There's something wrong please try again")
                    toastHelper.makeToast(e.localizedMessage!!)
                    e.localizedMessage?.let { Log.w("LOGIN ACTIVITY", it) }
                }
            }
        }
    }
}