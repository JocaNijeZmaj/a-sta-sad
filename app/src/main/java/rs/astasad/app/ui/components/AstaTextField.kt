package rs.astasad.app.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import rs.astasad.app.R
import rs.astasad.app.ui.theme.AstaTheme

@Composable
fun AstaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    supportingText: String? = null,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        label = { Text(label) },
        supportingText = if (errorMessage != null || supportingText != null) {
            {
                if (errorMessage != null) {
                    AstaErrorMessage(errorMessage)
                } else if (supportingText != null) {
                    Text(supportingText, style = MaterialTheme.typography.bodySmall)
                }
            }
        } else {
            null
        },
        isError = errorMessage != null,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        shape = MaterialTheme.shapes.medium
    )
}

@Preview(showBackground = true)
@Composable
private fun AstaTextFieldPreview() {
    AstaTheme {
        AstaTextField(
            value = "primer",
            onValueChange = {},
            label = stringResource(R.string.add_user_email),
            errorMessage = "Unesi ispravnu imejl adresu."
        )
    }
}
