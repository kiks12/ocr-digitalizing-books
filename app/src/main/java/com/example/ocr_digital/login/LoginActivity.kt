package com.example.ocr_digital.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ocr_digital.R
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.home.HomeActivity
import com.example.ocr_digital.ui.theme.OcrdigitalTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : ComponentActivity() {

    private val toastHelper = ToastHelper(this)
    private lateinit var auth : FirebaseAuth
    private lateinit var signInRequest : GoogleSignInClient

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        try {
            if (result.resultCode == Activity.RESULT_OK) {
                val signedInAccount = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val credentials = GoogleAuthProvider.getCredential(signedInAccount.result.idToken, null)
                auth.signInWithCredential(credentials)
                    .addOnSuccessListener {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        return@addOnSuccessListener
                    }
                    .addOnFailureListener {
                        it.localizedMessage?.let { it1 -> toastHelper.makeToast(it1) }
                    }
            } else {
                toastHelper.makeToast("There's something wrong please try again")
            }
        } catch (e : ApiException) {
            e.localizedMessage?.let { Log.w("SIGN IN ERROR", it) }
        }
    }

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
        signInRequest = GoogleSignIn.getClient(applicationContext, googleSignInOptions)

        val activityStarterHelper = ActivityStarterHelper(this)
        val loginViewModel = LoginViewModel(
            activityStarterHelper = activityStarterHelper,
            toastHelper = toastHelper,
            continueWithGoogle = ::continueWithGoogle
        )

        setContent {
            OcrdigitalTheme {
                LoginScreen(loginViewModel = loginViewModel)
            }
        }
    }

    private fun continueWithGoogle() {
        val googleIntent = signInRequest.signInIntent
        resultLauncher.launch(googleIntent)
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}