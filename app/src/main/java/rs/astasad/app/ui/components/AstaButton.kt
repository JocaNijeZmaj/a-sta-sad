package rs.astasad.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import rs.astasad.app.R
import rs.astasad.app.ui.theme.AstaDimens
import rs.astasad.app.ui.theme.AstaTheme

@Composable
fun AstaButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge)
    }
}

@Preview(showBackground = true)
@Composable
private fun AstaButtonPreview() {
    AstaTheme {
        Column(verticalArrangement = Arrangement.spacedBy(AstaDimens.textSpacing)) {
            AstaButton(stringResource(R.string.login_continue_demo), onClick = {})
            AstaButton(stringResource(R.string.add_user_submit), onClick = {}, enabled = false)
        }
    }
}
