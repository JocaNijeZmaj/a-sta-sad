package rs.astasad.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseDevApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val firebaseApp = FirebaseApp.initializeApp(this) ?: return
        check(firebaseApp.options.projectId == "a-sta-sad-dev") {
            "Use the Firebase configuration for a-sta-sad-dev."
        }
        if (BuildConfig.FIREBASE_DEV_CLOUD) return

        FirebaseAuth.getInstance(firebaseApp).useEmulator(EMULATOR_HOST, 9099)
        FirebaseFirestore.getInstance(firebaseApp).useEmulator(EMULATOR_HOST, 8080)
    }

    private companion object {
        const val EMULATOR_HOST = "10.0.2.2"
    }
}
