package component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import utils.Dimens.TMDb_140_dp
import utils.Dimens.TMDb_8_dp

@Composable
fun TMDbVerticalLazyGrid(
    content: LazyGridScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier.background(MaterialTheme.colors.background),
        columns = GridCells.Adaptive(minSize = TMDb_140_dp),
        contentPadding = PaddingValues(
            start = TMDb_8_dp,
            end = TMDb_8_dp,
            bottom = TMDb_8_dp
        ),
        horizontalArrangement = Arrangement.spacedBy(
            TMDb_8_dp, Alignment.CenterHorizontally
        ),
        content = {
            content()
        })

}