package rs.astasad.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import rs.astasad.app.ui.theme.AstaDimens

@Composable
internal fun AstaPlaceholderContent(
    title: String,
    description: String,
    action: @Composable () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = AstaDimens.screenPadding, vertical = AstaDimens.sectionSpacing),
        verticalArrangement = Arrangement.spacedBy(AstaDimens.sectionSpacing)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(AstaDimens.textSpacing)) {
            Text(title, style = MaterialTheme.typography.headlineSmall)
            Text(description, style = MaterialTheme.typography.bodyMedium)
        }
        action()
    }
}
