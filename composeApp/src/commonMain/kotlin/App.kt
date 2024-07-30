import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import domain.model.Poster
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
            NavHost(
                navController = navController,
                startDestination = HOME_ROUTE,
            ) {
                composable(route = HOME_ROUTE) {
                    HomeScreen(onClick = { poster ->
                        navController.navigate(
                            "$DETAIL_ROUTE/${
                                UrlEncoderUtil.encode(Json.encodeToString(poster))
                            }"
                        )
                    }, animatedVisibilityScope = this@composable)
                }
                composable(
                    route = "$DETAIL_ROUTE/{${DISNEY_KEY}}",
                    arguments = listOf(
                        navArgument(DISNEY_KEY) { type = NavType.StringType })
                ) { from ->
                    from.arguments?.getString(DISNEY_KEY)?.let {
                        DetailScreen(
                            Json.decodeFromString<Poster>(it),
                            navController::navigateUp,
                            animatedVisibilityScope = this@composable
                        )
                    }
                }
            }
        }
    }
}