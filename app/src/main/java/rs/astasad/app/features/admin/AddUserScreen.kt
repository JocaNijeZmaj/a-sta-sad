package rs.astasad.app.features.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import rs.astasad.app.R
import rs.astasad.app.ui.components.AstaAppScaffold
import rs.astasad.app.ui.components.AstaButton
import rs.astasad.app.ui.components.AstaPlaceholderContent
import rs.astasad.app.ui.components.AstaTextField
import rs.astasad.app.ui.theme.AstaDimens
import rs.astasad.app.ui.theme.AstaTheme

@Composable
fun AddUserScreen() {
    AstaPlaceholderContent(
        title = stringResource(R.string.add_user_title),
        description = stringResource(R.string.add_user_placeholder)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(AstaDimens.textSpacing)) {
            AstaTextField(
                value = "",
                onValueChange = {},
                label = stringResource(R.string.add_user_name),
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
            AstaTextField(
                value = "",
                onValueChange = {},
                label = stringResource(R.string.add_user_email),
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
        }
        AstaButton(
            label = stringResource(R.string.add_user_submit),
            onClick = {},
            enabled = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddUserScreenPreview() {
    AstaTheme {
        AstaAppScaffold(title = null, bottomBar = {}) { modifier ->
            Box(modifier) { AddUserScreen() }
        }
    }
}
