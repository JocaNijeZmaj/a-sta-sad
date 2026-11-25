package rs.astasad.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import rs.astasad.app.R
import rs.astasad.app.ui.theme.AstaDimens
import rs.astasad.app.ui.theme.AstaTheme

@Composable
fun AstaCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    OutlinedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(AstaDimens.cardPadding), content = content)
    }
}

@Preview(showBackground = true)
@Composable
private fun AstaCardPreview() {
    AstaTheme {
        AstaCard {
            Text(
                stringResource(R.string.admin_dashboard_section),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
