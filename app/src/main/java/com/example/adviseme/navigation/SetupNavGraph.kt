package com.example.adviseme.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.adviseme.ui.auth.*
import com.example.adviseme.ui.screens.MainAppEntry
import com.example.adviseme.screens.ChatScreen
import com.example.adviseme.screens.ChatDetailScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    val vm = AuthViewModel()

    NavHost(
        navController = navController,
        startDestination = "signin"
    ) {

        /* ---------- Auth flow ---------- */
        composable("signin") {
            SignInScreen(
                viewModel = vm,
                onSuccess = {
                    navController.navigate("main") {
                        popUpTo("signin") { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate("signup") }
            )
        }

        composable("signup") {
            SignUpScreen(
                viewModel = vm,
                onSuccess = {
                    navController.navigate("main") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("main") {
            MainAppEntry(navController)   // ← pass it
        }

        /* ---------- Chat list ---------- */
        composable("chatList") { ChatScreen(nav = navController) }


        /* ---------- One‑to‑one chat ---------- */
        composable(
            route = "chat/{cid}/{otherUid}",
            arguments = listOf(
                navArgument("cid") { type = NavType.StringType },
                navArgument("otherUid") { type = NavType.StringType }
            )
        ) { backStack ->
            val cid = backStack.arguments?.getString("cid")!!
            val otherUid = backStack.arguments?.getString("otherUid")!!

            ChatDetailScreen(
                conversationId = cid,
                otherUid = otherUid,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
