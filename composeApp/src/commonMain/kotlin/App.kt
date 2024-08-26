import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import domain.model.Movie
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.thauvin.erik.urlencoder.UrlEncoderUtil
import org.jetbrains.compose.ui.tooling.preview.Preview
import theme.ComposeTheme
import ui.DetailScreen
import ui.HomeScreen
import utils.MainDestinations.DETAIL_ROUTE
import utils.MainDestinations.DISNEY_KEY
import utils.MainDestinations.HOME_ROUTE

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
@Preview
fun App(navController: NavHostController = rememberNavController()) {
    ComposeTheme {
        SharedTransitionLayout {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this
            ) {
                NavHost(
                    navController = navController,
                    startDestination = HOME_ROUTE,
                ) {
                    composable(route = HOME_ROUTE) {
                        CompositionLocalProvider(
                            LocalNavAnimatedVisibilityScope provides this@composable
                        ) {
                            HomeScreen(onClick = { movie ->
                                navController.navigate(
                                    "$DETAIL_ROUTE/${
                                        UrlEncoderUtil.encode(Json.encodeToString(movie))
                                    }"
                                )
                            })
                        }
                    }
                    composable(
                        route = "$DETAIL_ROUTE/{${DISNEY_KEY}}",
                        arguments = listOf(
                            navArgument(DISNEY_KEY) { type = NavType.StringType })
                    ) { from ->
                        CompositionLocalProvider(
                            LocalNavAnimatedVisibilityScope provides this@composable
                        ) {
                            from.arguments?.getString(DISNEY_KEY)?.let {
                                DetailScreen(
                                    Json.decodeFromString<Movie>(it),
                                    navController::navigateUp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }