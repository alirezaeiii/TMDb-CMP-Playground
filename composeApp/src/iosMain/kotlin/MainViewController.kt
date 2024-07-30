import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.ui.window.ComposeUIViewController

@OptIn(ExperimentalSharedTransitionApi::class)
fun MainViewController() = ComposeUIViewController { App() }