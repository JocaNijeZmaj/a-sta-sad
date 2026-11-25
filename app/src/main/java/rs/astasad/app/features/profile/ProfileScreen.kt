package rs.astasad.app.features.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import rs.astasad.app.R
import rs.astasad.app.ui.components.AstaPlaceholderContent
import rs.astasad.app.ui.theme.AstaTheme

@Composable
fun ProfileScreen() {
    AstaPlaceholderContent(
        title = stringResource(R.string.profile_title),
        description = stringResource(R.string.profile_placeholder)
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    AstaTheme {
        ProfileScreen()
    }
}
