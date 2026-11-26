package rs.astasad.app.data.repository

class AuthFailure(val reason: Reason) : Exception(reason.name) {
    enum class Reason {
        INVALID_CREDENTIALS,
        INVALID_EMAIL,
        NETWORK,
        RATE_LIMITED,
        CONFIGURATION_UNAVAILABLE,
        REGISTRATION_UNAVAILABLE,
        UNEXPECTED
    }
}
