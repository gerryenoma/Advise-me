package com.example.adviseme.navigation

sealed class BottomNavItem(var route: String, var title: String) {
    object Robot : BottomNavItem("robot", "Robot")
    object Feed : BottomNavItem("feed", "Feed")
    object Chat : BottomNavItem("chat", "Chat")
    object Profile : BottomNavItem("profile", "Profile")
}
