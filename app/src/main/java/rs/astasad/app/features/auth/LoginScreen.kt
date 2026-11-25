package rs.astasad.app.features.auth

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import rs.astasad.app.R
import rs.astasad.app.ui.components.AstaPlaceholderContent
import rs.astasad.app.ui.theme.AstaTheme

@Composable
fun LoginScreen(onContinueDemo: () -> Unit) {
    AstaPlaceholderContent(
        title = stringResource(R.string.app_name),
        description = stringResource(R.string.login_placeholder)
    ) {
        Button(onClick = onContinueDemo, shape = MaterialTheme.shapes.medium) {
            Text(stringResource(R.string.login_continue_demo))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    AstaTheme {
        LoginScreen(onContinueDemo = {})
    }
}
