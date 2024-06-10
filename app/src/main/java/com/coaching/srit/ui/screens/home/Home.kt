package com.coaching.srit.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dehaze
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coaching.srit.ui.components.BackgroundImage
import com.coaching.srit.ui.components.ClickableTextWithArrowSign
import com.coaching.srit.ui.components.HeadingTextComposable
import com.coaching.srit.ui.components.NormalTextComposable
import com.coaching.srit.ui.navigation.HomeScreen
import com.coaching.srit.ui.navigation.HomeScreenRouter
import com.coaching.srit.ui.navigation.HomeScreenRouter.currentScreen
import com.coaching.srit.ui.navigation.Router
import com.coaching.srit.ui.screens.home.about.AboutScreen
import com.coaching.srit.ui.screens.home.batches.BatchesScreen
import com.coaching.srit.ui.screens.home.notice.NoticeScreen
import com.coaching.srit.ui.screens.home.study.StudyScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Home(homeScreenViewModel: HomeScreenViewModel = viewModel()){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Box {
        BackgroundImage()
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                HomeNavigationDrawer(homeScreenViewModel, scope, drawerState)
            },
        ) {
            Scaffold(
                topBar = {
                    HomeTopAppBar(scope, drawerState)

                },
                bottomBar = {
                    HomeBottomNavigationBar(homeScreenViewModel)

                },
                containerColor = Color.Transparent
            ) { contentPadding ->
                Box(modifier = Modifier.padding(contentPadding)) {
                    NavigateToScreen(currentScreen)
                }
            }
        }
    }

}

@Composable
private fun HomeNavigationDrawer(
    homeScreenViewModel: HomeScreenViewModel,
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    ModalDrawerSheet(
        drawerContainerColor = Color(0xFF16171A),
        drawerContentColor = Color(0xFF121316)
    ) {
        Spacer(modifier = Modifier.size(20.dp))
        HeadingTextComposable(textValue = "S.R.I.T")
        Spacer(modifier = Modifier.size(40.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.size(20.dp))
        homeScreenViewModel.navDrawerItems.forEachIndexed { index, item ->
            NavigationDrawerItem(
                label = {
                    NormalTextComposable(
                        textValue = item.title,
                        textAlign = TextAlign.Start
                    )
                },
                selected = index == homeScreenViewModel.navigationDrawerSelectedItemIndex.intValue,
                colors = NavigationDrawerItemDefaults.colors(
                    Color.DarkGray,
                    Color.Transparent
                ),
                onClick = {
                    homeScreenViewModel.updateNavigationDrawerIndex(index)
                    Router.navigateTo(item.screenRoute)
                    if (item.badge != null) {
                        homeScreenViewModel.updateNavBadgeCount(index, 0)
                    }
                    scope.launch {
                        drawerState.close()
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (index == homeScreenViewModel.navigationDrawerSelectedItemIndex.intValue) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                badge = {
                    if (item.badge != null && item.badge != 0) {
                        Text(text = item.badge.toString(), color = Color.White)
                    }
                }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeTopAppBar(
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    TopAppBar(
        title = {
            ClickableTextWithArrowSign(text = "Android Development", onClick = {})
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Dehaze,
                    contentDescription = "Navigation Drawer",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
private fun HomeBottomNavigationBar(homeScreenViewModel: HomeScreenViewModel) {
    NavigationBar(containerColor = Color.Transparent, contentColor = Color.White) {
        homeScreenViewModel.bottomNavItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = homeScreenViewModel.bottomBarSelectedItemIndex.intValue == index,
                onClick = {
                    homeScreenViewModel.updateBottomBarIndex(index)
                    HomeScreenRouter.changeCurrentScreen(item.screenRoute)
                    if (item.badgeCount != null) {
                        homeScreenViewModel.updateBottomNavBadgeCount(index, 0)
                    } else if (item.hasNews) {
                        homeScreenViewModel.updateBottomNavNewsCount(index)
                    }
                },
                label = {
                    Text(text = item.title, color = Color.White)
                },

                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount != null && item.badgeCount != 0) {
                                Badge {
                                    Text(text = item.badgeCount.toString())
                                }
                            } else if (item.hasNews) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (index == homeScreenViewModel.bottomBarSelectedItemIndex.intValue) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title,
                            tint = if (index == homeScreenViewModel.bottomBarSelectedItemIndex.intValue) Color.Unspecified else Color.White
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun NavigateToScreen(currentScreen: MutableState<HomeScreen>) {
    when (currentScreen.value) {
        HomeScreen.StudyScreen -> {
            StudyScreen()
        }
        HomeScreen.AboutScreen -> {
            AboutScreen()
        }
        HomeScreen.BatchesScreen -> {
            BatchesScreen()
        }
        HomeScreen.NoticeScreen -> {
            NoticeScreen()
        }
    }
}