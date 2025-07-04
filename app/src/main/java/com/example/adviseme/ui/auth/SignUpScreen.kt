@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.adviseme.ui.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adviseme.R
import com.example.adviseme.ui.FieldColors

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onSuccess: () -> Unit,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    /* ────────────────────────────────────────────────────────────────
       Helpers for Google & Apple sign‑in
    ──────────────────────────────────────────────────────────────── */
    val ctx = LocalContext.current
    val activity = ctx as Activity                               // Apple OAuth needs Activity

    // Google options & launcher
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ctx.getString(R.string.default_web_client_id)) // provided by Firebase
            .requestEmail()
            .build()
    }
    val googleClient = remember { GoogleSignIn.getClient(ctx, gso) }

    val googleLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            viewModel.signUpWithGoogle(idToken) { ok, msg ->
                if (ok) onSuccess() else error = msg
            }
        } catch (e: Exception) {
            error = e.message
        }
    }
    /* ──────────────────────────────────────────────────────────────── */

    Surface(Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            // Centered Logo
            Image(
                painter = painterResource(id = R.drawable.adviseme_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )

            Spacer(Modifier.height(24.dp))

            Text("Create Account", fontSize = 24.sp, color = Color.Black)
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = FieldColors
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color.Black) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = FieldColors
            )

            Spacer(Modifier.height(16.dp))

            /* ── Email sign‑up ── */
            Button(
                onClick = {
                    viewModel.signUpWithEmail(email, password) { ok, msg ->
                        if (ok) onSuccess() else error = msg
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = ButtonDefaults.outlinedButtonBorder,
                modifier = Modifier.width(160.dp)
            ) {
                Text("Sign Up", color = Color.Black)
            }

            /* ─── Social sign‑up buttons (same style & width) ─── */

            Spacer(Modifier.height(16.dp))

            // Fakebook
            Button(
                onClick = {
                    viewModel.signUpWithFacebook { ok, msg ->
                        if (ok) onSuccess() else error = msg
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = ButtonDefaults.outlinedButtonBorder,
                modifier = Modifier.width(160.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_facebook),
                    contentDescription = "Facebook",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Facebook", color = Color.Black)
            }

            Spacer(Modifier.height(8.dp))

            // Google
            Button(
                onClick = { googleLauncher.launch(googleClient.signInIntent) },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = ButtonDefaults.outlinedButtonBorder,
                modifier = Modifier.width(160.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Google", color = Color.Black)
            }

            Spacer(Modifier.height(8.dp))

            // Apple ID
            Button(
                onClick = {
                    viewModel.signUpWithApple(activity) { ok, msg ->
                        if (ok) onSuccess() else error = msg
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = ButtonDefaults.outlinedButtonBorder,
                modifier = Modifier.width(160.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_apple),
                    contentDescription = "Apple",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Apple ID", color = Color.Black)
            }

            /* ──────────────────────────────────────────────── */

            Spacer(Modifier.height(16.dp))

            Text(
                "Skip for now",
                color = Color.Black,
                modifier = Modifier
                    .clickable { viewModel.signInAnonymously { onSuccess() } }
                    .padding(8.dp)
            )

            Text(
                "Back to Sign In",
                color = Color.Black,
                modifier = Modifier
                    .clickable { onBack() }
                    .padding(8.dp)
            )

            error?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = Color.Red, fontSize = 14.sp)
            }
        }
    }
}
