package rs.astasad.app.features.activities

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import rs.astasad.app.R
import rs.astasad.app.ui.components.AstaPlaceholderContent
import rs.astasad.app.ui.theme.AstaTheme

@Composable
fun ActivitiesScreen() {
    AstaPlaceholderContent(
        title = stringResource(R.string.activities_title),
        description = stringResource(R.string.activities_placeholder)
    )
}

@Preview(showBackground = true)
@Composable
private fun ActivitiesScreenPreview() {
    AstaTheme {
        ActivitiesScreen()
    }
}
