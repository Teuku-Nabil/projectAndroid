package com.example.jetgame

import androidx.compose.runtime.getValue
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.jetgame.model.BottomBarItem
import com.example.jetgame.model.GamesData
import com.example.jetgame.navigation.Screen

@Composable
fun JetGamesApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(bottomBar = { BottomBar(navController) }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                JetGamesAppView(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    navigateToDetail = { GameId ->
                        navController.navigate(Screen.DetailGame.createRoute(GameId))
                    }
                )
            }
            composable(Screen.About.route) {
                AboutPage(
                    modifier = Modifier.padding(innerPadding)
                )
            }
            composable("home/{id}") { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("id")
                DetailGameView(modifier, itemId ?: "")
            }
        }
    }
}

@Composable
fun JetGamesAppView(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigateToDetail: (String) -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        LazyColumn {
            items(GamesData.games, key = { it.id }) { game ->
                GameListItem(
                    name = game.name,
                    photoUrl = game.photoUrl,
                    id = game.id,
                    navigateToDetail = navigateToDetail
                )
            }
        }
    }
}

@Composable
fun DetailGameView(
    modifier: Modifier = Modifier,
    gameId: String,
) {
    Box(
        modifier = modifier
    ) {
        GamesData.games.find { it.id == gameId }?.let { game ->
            DetailGameApp(
                navController = rememberNavController(),
                gameId = game.id,
                name = game.name,
                photoUrl = game.photoUrl,
                desc = game.description,
            )
        }
    }
}

@Composable
fun GameListItem(
    id: String,
    name: String,
    photoUrl: String,
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable {
            navigateToDetail(id)
        }
    ) {
        AsyncImage(
            model = photoUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(8.dp)
                .size(60.dp)
                .clip(CircleShape)
        )
        Text(
            text = name,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 16.dp)
        )
    }
}

@Composable
fun DetailGameApp(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    gameId: String,
    name: String,
    photoUrl: String,
    desc: String,
) {
    Column(modifier = modifier) {
        Column(modifier = modifier) {
            Box {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Justify,
                )
            }
        }
    }
}

@Composable
fun AboutPage(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Box {
                AsyncImage(
                    model = "https://raw.githubusercontent.com/Teuku-Nabil/ProjectImageRepo/b20520fe5fecea59a9ce14c264f614d90ac74550/pp.jpg",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(400.dp)
                        .clip(CircleShape)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
            ) {
                Text(
                    text = "Teuku Nabil Muhammad Dhuha",
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .padding(top = 8.dp)
                )
                Text(
                    text = "nabilmuhammad246@gmail.com",
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Justify,
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    BottomNavigation(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            BottomBarItem(
                title = stringResource(R.string.home_page),
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            BottomBarItem(
                title = stringResource(R.string.about_page),
                icon = Icons.Default.Person,
                screen = Screen.About
            ),
        )
        navigationItems.map { game ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = game.icon,
                        contentDescription = game.title
                    )
                },
                label = {
                    Text(game.title)
                },
                selected = currentRoute == game.screen.route,
                onClick = {
                    navController.navigate(game.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
