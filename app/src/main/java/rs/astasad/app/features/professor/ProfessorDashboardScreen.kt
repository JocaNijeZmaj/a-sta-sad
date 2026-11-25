package rs.astasad.app.features.professor

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
fun ProfessorDashboardScreen() {
    AstaPlaceholderContent(
        title = stringResource(R.string.professor_dashboard_title),
        description = stringResource(R.string.professor_dashboard_description)
    ) {
        AstaCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                stringResource(R.string.professor_dashboard_section),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                stringResource(R.string.professor_dashboard_placeholder),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfessorDashboardScreenPreview() {
    AstaTheme {
        AstaAppScaffold(title = null, bottomBar = {}) { modifier ->
            Box(modifier) { ProfessorDashboardScreen() }
        }
    }
}
