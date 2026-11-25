package rs.astasad.app.ui.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import rs.astasad.app.R
import rs.astasad.app.data.demo.DemoData
import rs.astasad.app.ui.components.AstaCard
import rs.astasad.app.ui.components.AstaPlaceholderContent
import rs.astasad.app.ui.theme.AstaDimens
import rs.astasad.app.ui.theme.AstaTheme

private val demoDateFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
    .withLocale(Locale.forLanguageTag("sr-Latn-RS"))
    .withZone(ZoneId.of("Europe/Belgrade"))

@Preview(name = "Demo activities", showBackground = true, widthDp = 360)
@Composable
private fun DemoActivitiesPreview() {
    AstaTheme {
        AstaPlaceholderContent(
            title = stringResource(R.string.activities_title),
            description = stringResource(R.string.demo_preview_read_only)
        ) {
            DemoData.publishedActivities.take(2).forEach { activity ->
                val school = DemoData.schools.first { it.id == activity.schoolId }
                AstaCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(AstaDimens.textSpacing)) {
                        Text(activity.title, style = MaterialTheme.typography.titleMedium)
                        Text(activity.description, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            stringResource(
                                R.string.demo_preview_activity_details,
                                school.name,
                                demoDateFormat.format(activity.startsAt),
                                activity.location
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Demo forum", showBackground = true, widthDp = 360)
@Composable
private fun DemoForumPreview() {
    AstaTheme {
        AstaPlaceholderContent(
            title = stringResource(R.string.nav_forum),
            description = stringResource(R.string.demo_preview_read_only)
        ) {
            DemoData.publishedQuestions.asReversed().take(2).forEach { question ->
                val author = DemoData.users.first { it.id == question.authorId }
                val answerCount = DemoData.visibleAnswers.count { it.questionId == question.id }
                AstaCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(AstaDimens.textSpacing)) {
                        Text(question.title, style = MaterialTheme.typography.titleMedium)
                        Text(question.body, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            stringResource(R.string.demo_preview_question_details, author.displayName, answerCount),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
