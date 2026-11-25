package rs.astasad.app.features.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import rs.astasad.app.R
import rs.astasad.app.ui.components.AstaAppScaffold
import rs.astasad.app.ui.components.AstaCard
import rs.astasad.app.ui.components.AstaPlaceholderContent
import rs.astasad.app.ui.theme.AstaTheme

@Composable
fun AdminDashboardScreen() {
    AstaPlaceholderContent(
        title = stringResource(R.string.admin_dashboard_title),
        description = stringResource(R.string.admin_dashboard_description)
    ) {
        AstaCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                stringResource(R.string.admin_dashboard_section),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                stringResource(R.string.admin_dashboard_placeholder),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminDashboardScreenPreview() {
    AstaTheme {
        AstaAppScaffold(title = null, bottomBar = {}) { modifier ->
            Box(modifier) { AdminDashboardScreen() }
        }
    }
}
