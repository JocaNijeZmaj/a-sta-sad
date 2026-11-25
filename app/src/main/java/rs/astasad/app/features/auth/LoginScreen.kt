package rs.astasad.app.features.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import rs.astasad.app.R
import rs.astasad.app.ui.components.AstaButton
import rs.astasad.app.ui.components.AstaPlaceholderContent
import rs.astasad.app.ui.theme.AstaTheme

@Composable
fun LoginScreen(onContinueDemo: () -> Unit) {
    AstaPlaceholderContent(
        title = stringResource(R.string.app_name),
        description = stringResource(R.string.login_placeholder)
    ) {
        AstaButton(label = stringResource(R.string.login_continue_demo), onClick = onContinueDemo)
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    AstaTheme {
        LoginScreen(onContinueDemo = {})
    }
}
