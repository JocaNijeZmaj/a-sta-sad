package rs.astasad.app.features.forum

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import rs.astasad.app.R
import rs.astasad.app.ui.components.AstaPlaceholderContent
import rs.astasad.app.ui.theme.AstaTheme

@Composable
fun ForumScreen() {
    AstaPlaceholderContent(
        title = stringResource(R.string.forum_title),
        description = stringResource(R.string.forum_placeholder)
    )
}

@Preview(showBackground = true)
@Composable
private fun ForumScreenPreview() {
    AstaTheme {
        ForumScreen()
    }
}
