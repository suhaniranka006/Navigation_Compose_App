package com.example.navigation_compose_app


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)

            }
        }
    }
}



//dependency

//define routes
//Ek sealed class ya object banate hain jisme routes defined hote hain.


sealed class Screen(val route : String) {
    object Login : Screen("login")
    object Home : Screen("home/{username}") {
        fun createRoute(username: String) = "home/$username"
    }

    object Details : Screen("detail/{itemId}") {
        fun createRoute(itemId: Int) = "detail/$itemId"
    }
}


@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {

        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { username ->
                    navController.navigate(Screen.Home.createRoute(username))
                }
            )
        }

        // Home Screen (takes username)
        composable(
            route = Screen.Home.route,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Guest"  //retrieve value
            HomeScreen(
                username = username,
                onItemClick = { itemId ->
                    navController.navigate(Screen.Details.createRoute(itemId))
                }
            )
        }

        // Detail Screen (takes itemId)
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: -1
            DetailScreen(itemId = itemId)
        }
    }
}


//login screen

@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { onLoginSuccess("Suhani") }) {
            Text("Login as Suhani")
        }
    }
}



@Composable
fun HomeScreen(username: String,onItemClick: (Int) -> Unit) {
    Column (modifier = Modifier.fillMaxSize().padding(16.dp)){
        Text(text = "Welcome, $username" , fontSize = 22.sp )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(5) { index ->
               Card (
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(8.dp)
                       .clickable { onItemClick(index) }

               ){
                   Text(text = "Item $index", modifier = Modifier.padding(16.dp))
               }
            }

        }
    }
}

//detailscreen
@Composable
fun DetailScreen(itemId: Int) {
    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ){
        Text(text = "Detail Screen for Item $itemId", fontSize = 22.sp)

    }
}