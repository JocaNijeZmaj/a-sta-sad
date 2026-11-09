# A šta sad?

**A šta sad?** (*What Now?*) is an Android application designed to connect high school students with universities and colleges through promotional activities and a community-driven question-and-answer forum.

Inspired by the simplicity of Reddit-style discussions, the app helps prospective students explore educational opportunities, ask questions, and get answers from current students and professors. It also gives educational institutions a structured way to organize and manage promotional activities such as school visits, education fairs, and workshops.

> **Project status:** In development — student project for the **Android Programming, 2026/2027** course. This README describes the **target MVP architecture and features**, not a claim that every feature is already implemented.

## Features

The minimum viable product (MVP) has two main areas:

### Promotional activities

- Browse a feed of activities from participating institutions.
- Search and filter activities by school, type, date, or availability.
- View details such as location, schedule, capacity, and description.
- Allow authorized professors to create and manage activities for **their own institution**.
- Allow eligible college students to register, cancel participation, and confirm completion.
- Provide administrators with activity management and basic reporting.

### Q&A forum

- High school students can ask questions about institutions, study programs, and student life.
- College students and professors can post answers.
- Users can browse and search discussions, vote on helpful answers, and report inappropriate content.
- Question authors can mark an answer as accepted.
- Administrators can moderate reported content.

### User roles and permissions

| Capability | Administrator | Professor | College student | High school student |
| --- | :---: | :---: | :---: | :---: |
| Sign in and manage own profile | ✓ | ✓ | ✓ | ✓ |
| Browse activities and forum discussions | ✓ | ✓ | ✓ | ✓ |
| Create and manage promotional activities | All institutions | Own institution | — | — |
| Manage activity participants | All institutions | Own institution | Own registration only | — |
| Ask questions | ✓ | ✓ | ✓ | ✓ |
| Answer forum questions | ✓ | ✓ | ✓ | — |
| Accept an answer | Own questions | Own questions | Own questions | Own questions |
| Manage users and moderate content | ✓ | — | — | — |

Permissions are enforced by backend security rules, not only by hiding buttons in the UI. Some actions, such as creating privileged accounts, may require a trusted backend component.

**Out of scope for the MVP:** video hosting, private messaging, payments, and push notifications. These may be considered later.

## Tech stack

| Layer | Technology |
| --- | --- |
| Platform | Android |
| Language | Kotlin |
| UI | Jetpack Compose + Material Design 3 |
| Architecture | MVVM (Model–View–ViewModel) |
| UI state and async work | ViewModel, StateFlow, Kotlin Coroutines |
| Navigation | Navigation Compose |
| Data access | Repository pattern |
| Authentication | Firebase Authentication (planned) |
| Database | Cloud Firestore (planned) |
| Build | Gradle / Kotlin DSL |
| IDE | Android Studio |

The team should confirm the Firebase setup and exact dependency versions in the project before implementing data-backed features. The Gradle configuration is the source of truth for library versions.

## Getting started

### Prerequisites

- **Android Studio** with an Android SDK installed.
- A compatible **JDK**, as required by the project's Gradle configuration (normally provided by Android Studio).
- An Android emulator or physical Android device with USB debugging enabled.
- Git.
- Access to the shared Firebase development project **once Firebase integration is enabled**.

### 1. Clone the repository

```bash
git clone <repository-url>
cd <repository-directory>
```

Replace the placeholders with the actual repository URL and folder name.

### 2. Open the project

1. Open **Android Studio**.
2. Select **Open** and choose the repository folder.
3. Allow **Gradle Sync** to complete and install any requested Android SDK components.

### 3. Configure Firebase (when required)

Firebase-backed screens require the team's development Firebase project:

1. Obtain the Android Firebase configuration file (`google-services.json`) from the project maintainer.
2. Place it at `app/google-services.json`, if that is the location configured by the project.
3. Ensure the corresponding Firebase Authentication methods, Firestore database, security rules, and required indexes have been configured.
4. Use test accounts for each role. Do **not** create admin/professor privileges through public registration.

Do not commit private credentials, service account keys, or real user data. Firebase client configuration is not a substitute for security rules.

> Before Firebase integration is complete, screens can be developed and previewed with **fake/sample repositories**. Features requiring live authentication or data will not work until the backend is configured.

### 4. Build and run

Select the **app** run configuration and an emulator/device, then click **Run** in Android Studio.

Alternatively, from the repository root on macOS/Linux:

```bash
./gradlew :app:assembleDebug
```

On Windows:

```powershell
.\gradlew.bat :app:assembleDebug
```

The command creates a debug APK if the Android project and dependencies are configured correctly.

### 5. Run checks

```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:lintDebug
```

Run these commands when the corresponding tasks are available in the checked-out Android project. UI flows and role-specific permissions should also be tested on an emulator/device.

## Architecture

The app follows **MVVM** with a feature-based package structure and a **Repository** abstraction between UI logic and the data source.

```text
Jetpack Compose screen
        ↓ user actions / observes state
ViewModel (StateFlow, validation, UI state)
        ↓
Repository interface
        ↓
Repository implementation
        ↓
Firebase Authentication / Cloud Firestore
(or a fake repository for previews and tests)
```

- **UI / Compose:** Displays state, collects user input, and uses shared Material 3 components.
- **ViewModel:** Owns screen state, handles UI events, performs validation, and calls repositories.
- **Repository:** Exposes data operations without coupling screens to Firebase APIs.
- **Data layer:** Maps remote documents to domain models and handles persistence.
- **Authorization:** Protects privileged operations using Firestore Security Rules and, where needed, trusted backend logic.

### Intended project structure

The exact package name and filenames will be finalized during project setup. The proposed structure is:

```text
app/
└── src/main/java/<package>/
    ├── core/                 # Shared utilities, states, and UI components
    ├── navigation/           # Routes and role-aware navigation
    ├── data/
    │   ├── model/            # Shared domain/data models
    │   └── repository/       # Interfaces and implementations
    ├── features/
    │   ├── auth/             # Sign-in, registration, password reset
    │   ├── profile/          # User profile
    │   ├── schools/          # Institutions and study programs
    │   ├── activities/       # Feed, details, management, enrollments
    │   ├── forum/            # Questions, answers, voting, reports
    │   └── admin/            # User management and moderation
    └── ui/
        ├── theme/            # Colors, typography, shapes
        └── components/       # Reusable Compose components

docs/
├── DESIGN_SYSTEM.md         # Visual rules and design tokens
└── WORKFLOW.md              # Team development and PR workflow

AGENTS.md                    # Shared rules for AI coding assistants
README.md
```

### Core data entities

- **User:** Account, role, status, and institution membership.
- **School / Program:** Institution and its study programs.
- **PromoActivity:** Promotional event, schedule, location, capacity, and cost.
- **Enrollment:** A college student's participation in an activity.
- **Question / Answer:** Forum discussions and replies.
- **AnswerVote / Report:** Helpful-answer voting and moderation reports.

The final schema, security rules, and any supporting backend functions should be reviewed before implementation to prevent unauthorized updates and duplicate or over-capacity enrollments.

## UI and design guidelines

The app uses a shared **Material 3 design system** to keep screens consistent across independently developed features. All screens should reuse the same theme, typography, spacing tokens, cards, form fields, and navigation components.

See [`docs/DESIGN_SYSTEM.md`](docs/DESIGN_SYSTEM.md) for the proposed palette, components, layout rules, and screen states. Do not introduce arbitrary colors or duplicate shared components in individual features.

## Development workflow

This is a four-person student project, with work organized by **features and small tasks**, not by permanently assigned frontend/backend roles.

1. Choose an unblocked task from the shared tracker/Jira backlog.
2. Create a branch such as `feature/ACT-04-create-activity` from the agreed integration branch.
3. Read `AGENTS.md` and, for UI tasks, `docs/DESIGN_SYSTEM.md`.
4. Implement only the task's scope, including relevant tests and UI states.
5. Review the diff, run Gradle checks, and test the feature in Android Studio.
6. Open a pull request for peer review; merge only after approval.
7. Mark the task complete after the merged code is verified.

See [`docs/WORKFLOW.md`](docs/WORKFLOW.md) for detailed instructions. AI coding assistants (such as Codex) may help with implementation, but developers are responsible for reviewing generated code and validating behavior.

## Academic project

Created for the **Android Programming course, academic year 2026/2027**. The project also includes UML diagrams, data modeling, design mockups, testing, and written documentation as part of the coursework.

The application is an educational prototype; production deployment, institution onboarding, and advanced integrations are outside the initial scope.
