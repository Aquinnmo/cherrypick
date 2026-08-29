package com.example.cherrypick

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.savedstate.read
import com.example.cherrypick.ui.LeaguesScreen
import com.example.cherrypick.ui.LoginScreen
import com.example.cherrypick.ui.PicksScreen
import com.example.cherrypick.ui.SignUpScreen
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth

private object Routes {
    const val LOGIN = "login"
    const val SIGN_UP = "signup"
    const val LEAGUES = "leagues"
    const val PICKS = "picks/{leagueId}"
    fun picks(leagueId: String) = "picks/$leagueId"
}

// navigation-compose earns its place over a hand-rolled `when` on a state variable
// for exactly one reason: it wires the Android hardware back button for free. Don't
// grow this into a bigger routing abstraction than that.
@Composable
fun App() = CherryPickTheme {
    val navController = rememberNavController()
    val startDestination = if (Firebase.auth.currentUser != null) Routes.LEAGUES else Routes.LOGIN

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoggedIn = {
                    navController.navigate(Routes.LEAGUES) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate(Routes.SIGN_UP) },
            )
        }
        composable(Routes.SIGN_UP) {
            SignUpScreen(
                onSignedUp = {
                    navController.navigate(Routes.LEAGUES) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onBackToLogin = { navController.popBackStack() },
            )
        }
        composable(Routes.LEAGUES) {
            LeaguesScreen(
                onLeagueClick = { leagueId -> navController.navigate(Routes.picks(leagueId)) },
            )
        }
        composable(
            route = Routes.PICKS,
            arguments = listOf(navArgument("leagueId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val leagueId = backStackEntry.arguments?.read { getString("leagueId") }.orEmpty()
            PicksScreen(leagueId = leagueId, onBack = { navController.popBackStack() })
        }
    }
}
