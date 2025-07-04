package com.example.adviseme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.adviseme.navigation.BottomNavItem

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Robot,
        BottomNavItem.Feed,
        BottomNavItem.Chat,
        BottomNavItem.Profile
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(containerColor = Color.White, contentColor = Color.Black) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Image(
                        painter = painterResource(
                            id = when (item.route) {
                                BottomNavItem.Robot.route -> R.drawable.robot
                                BottomNavItem.Feed.route -> R.drawable.feed
                                BottomNavItem.Chat.route -> R.drawable.chat
                                BottomNavItem.Profile.route -> R.drawable.profile
                                else -> R.drawable.robot
                            }
                        ),
                        contentDescription = item.title,
                        modifier = Modifier.size(20.dp),
                        contentScale = ContentScale.Fit
                    )
                },
                label = { Text(text = item.title) },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = Color.Black,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
