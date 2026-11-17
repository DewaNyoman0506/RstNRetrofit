package inyo.pam.satuoktober

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import inyo.pam.satuoktober.data.remote.RetrofitClient
import inyo.pam.satuoktober.data.remote.StudentRemoteDataSource
import inyo.pam.satuoktober.data.repository.DefaultStudentRepository
import inyo.pam.satuoktober.ui.screens.DetailScreen
import inyo.pam.satuoktober.ui.screens.HomeScreen
import inyo.pam.satuoktober.ui.screens.ProfileScreen
import inyo.pam.satuoktober.ui.screens.AboutScreen
import inyo.pam.satuoktober.ui.detail.DetailViewModel
import inyo.pam.satuoktober.ui.home.HomeViewModel

@Composable
fun NavigationLabApp() {
    val remoteDataSource = remember {
        StudentRemoteDataSource(RetrofitClient.apiService)
    }
    val repository = remember {
        DefaultStudentRepository(remoteDataSource)
    }
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            val homeViewModel = remember { HomeViewModel(repository) }
            HomeScreen(
                uiState = homeViewModel.uiState,
                onOpenDetail = { id ->
                    navController.navigate(Routes.detail(id))
                },
                onOpenProfile = {
                    navController.navigate(Routes.PROFILE)
                },
                onOpenAbout = {
                    navController.navigate(Routes.ABOUT)
                },
                onRetry = homeViewModel::loadStudents,
                onSearchQueryChange = homeViewModel::onSearchQueryChange,
                onSearch = homeViewModel::searchStudents
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = {
                    navController.navigate(Routes.HOME)
                }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("studentId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: -1
            val viewModel: DetailViewModel = viewModel(
                factory = DetailViewModel.factory(repository)
            )
            LaunchedEffect(studentId) {
                viewModel.loadStudent(studentId)
            }
            DetailScreen(
                uiState = viewModel.uiState,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.ABOUT) {
            AboutScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
