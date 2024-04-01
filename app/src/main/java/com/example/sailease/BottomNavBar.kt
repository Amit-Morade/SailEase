package com.example.sailease

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MyNavBar(items: List<BottomNavigationItem>, navController: NavHostController) {
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    if(currentDestination?.route!=Screen.Welcome.route && currentDestination?.route!=Screen.Login.route) {
        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedItemIndex == index,
                    onClick = {
                        selectedItemIndex = index
                        navController.navigate(item.onClick)
                    },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedItemIndex) {
                                item.selectedIcon
                            }else item.unselectedIcon, contentDescription = item.title
                        )
                    })
            }
        }
    }

}