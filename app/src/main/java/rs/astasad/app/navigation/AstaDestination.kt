package rs.astasad.app.navigation

import rs.astasad.app.R

internal const val LOGIN_ROUTE = "login"

internal enum class AstaDestination(val route: String, val label: Int, val icon: Int) {
    Activities("activities", R.string.nav_activities, R.drawable.ic_activities),
    Forum("forum", R.string.nav_forum, R.drawable.ic_forum),
    Profile("profile", R.string.nav_profile, R.drawable.ic_profile)
}
