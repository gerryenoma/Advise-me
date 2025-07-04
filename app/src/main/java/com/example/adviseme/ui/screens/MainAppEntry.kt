package com.example.adviseme.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController        // ← new
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.adviseme.BottomNavBar
import com.example.adviseme.navigation.BottomNavItem
import com.example.adviseme.screens.*

@Composable
fun MainAppEntry(nav: NavHostController) {        // ← accept one controller
    Scaffold(bottomBar = { BottomNavBar(nav) }) { pad ->
        NavHost(nav, BottomNavItem.Robot.route, Modifier.padding(pad)) {
            composable(BottomNavItem.Robot.route)   { RobotScreen() }
            composable(BottomNavItem.Feed.route)    { FeedScreen()  }
            composable(BottomNavItem.Chat.route)    { ChatScreen(nav) }
            composable(BottomNavItem.Profile.route) { ProfileScreen() }
        }
    }
}
