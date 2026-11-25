# A šta sad? — praktičan razvojni workflow (tim od 4)

## 0. Jednokratna priprema (core)

1. Napravi GitHub repo i podesi `main` (stabilno) i `dev` (integracija) ili samo `main` + PR ako tim želi jednostavnije. Ne commitovati direktno na zaštićenu granu.
2. Android Studio: Empty Activity → Kotlin → Jetpack Compose → Material 3. Proveri da clean build i emulator rade.
3. Dodaj `AGENTS.md`, `docs/DESIGN_SYSTEM.md`, ovaj fajl i `.gitignore`.
4. Zaključaj package name, verzije, dependencies, modele, repository interfejse i navigacioni ugovor, pa ih usaglasite pre paralelnog rada.
5. Napravi `ui/theme/` i **minimalne** zajedničke UI komponente / `AstaAppScaffold`. Uskladi sa odobrenom Figmom.
6. Dodaj sample/fake podatke kako bi se svi feature ekrani mogli implementirati i pregledati bez čekanja Firebase-a.
7. Napravi PR za osnovu, sačuvaj referentne screenshotove i tek onda pustite paralelan razvoj.

## 1. Instalacija i izbor alata

- Glavni IDE: **Android Studio** za Gradle sync, emulator, Logcat, `@Preview` i debugging.
- Agent: **Codex** u AI Chat (kada JetBrains AI Assistant / IDE verzija podržava integraciju) ili **Codex CLI** iz integrisanog Terminal-a.
- Po pravilu izaberi **Workspace access** a ne Full access, proveri diff pre commita. Ne daj Codexu lične tajne i servisne ključeve.

Prijavi se ponuđenom metodom. Za JetBrains integraciju: `Settings/Plugins` → instaliraj/aktiviraj `AI Assistant` (ako postoji podrška u tvojoj verziji), otvori **AI Chat** i izaberi **Codex**. Dostupnost zavisi od konkretne verzije Android Studio-a i plugin kompatibilnosti; CLI radi nezavisno od te integracije.

## 2. Uzmi jedan task, ne deset

1. U tracker-u pronađi task sa statusom **SPREMNO** i proveri njegove zavisnosti.
2. Ako je task deo povezanog niza, poželjno je da ga nastavi osoba koja je radila prethodni task. To je preporuka, ne obavezna dodela.
3. Proveri da niko drugi već ne radi task; popuni vlasnika/ko radi u tracker-u ili ga označi `IN PROGRESS`.
4. Ažuriraj `dev` pre pravljenja grane:

```bash
git switch dev
git pull --ff-only
git switch -c feature/ACT-04-create-activity
```

5. Codexu daj tačan prompt iz Jira taska, uz pozivanje na `AGENTS.md` i, za UI, `docs/DESIGN_SYSTEM.md`.

## 3. Rad sa Codexom

**Prvi upit: plan, bez izmene koda.**

> Pročitaj AGENTS.md, docs/DESIGN_SYSTEM.md (pošto je UI task), relevantne modele i postojeće komponente. Analiziraj Jira task ACT-04. Predloži plan od 3–7 koraka, listu fajlova za izmenu i proveri potencijalne konflikte. Za sada ne menjaj kod.

**Drugi upit: implementacija fokusiranog taska.**

> Implementiraj samo ACT-04 prema kriterijumima prihvatanja. Reuse postojeći AstaTheme i zajedničke Compose komponente. Bez novih boja u feature kodu, bez menjanja nepovezanih funkcionalnosti. Dodaj UI preview sa sample podacima i testove ako su primenljivi. Na kraju pokreni dostupni build/test i prijavi rezultate i tačno koje si fajlove menjao.

**Poslednji upit: review.**

> Uradi review git diff-a prema AGENTS.md i DESIGN_SYSTEM.md. Pronađi regresije, duplirane UI komponente, hardkodovane boje, zaobilaženje prava pristupa i rupe u testiranju. Ne menjaj kod dok ne objasniš nalaze.

Ne dati AI-ju neograničen zadatak „napravi celu aplikaciju“. Posle svake izmene pregledaj diff i pokreni relevantni Compose Preview / emulator.

## 4. Kako izbeći preklapanje

- `ui/theme/`, `ui/components/`, `navigation/`, `data/model/` i zajednički repository ugovori su **shared core**. Menjaju se retko, kroz izdvojen PR i dogovor.
- `features/auth/`, `features/schools/`, `features/activities/`, `features/forum/`, `features/admin/` su relativno nezavisni. Jedan feature PR ne sme prepravljati druge feature-e „usput“.
- Novi shared API ili UI komponenta: prvo mali PR za shared core, zatim feature PR koji ga koristi.
- Koristite fake repository ili unapred dogovorene interfejse za paralelan UI rad dok Firestore implementacija nije spremna.
- Promene Firebase Rules, kolekcija i dozvola idu u jasno review-ovanom PR-u; UI skrivanje dugmadi nije autorizacija.

## 5. Svakodnevni ciklus za jedan task

```text
Excel/Jira: SPREMNO → rezerviši task
    ↓
Git: update dev → feature/XXX-kratak-opis
    ↓
Codex Plan → proveri plan → Codex Implement
    ↓
Android Studio: Compose Preview → emulator → test
    ↓
Git diff pregled + screenshot + `./gradlew test`
    ↓
Commit → push → PR → kolega pregleda → merge u dev
    ↓
Excel/Jira: ZAVRŠENO (tek kada je PR merged i verifikovan)
```

Pre svakog PR-a pokreni `./gradlew :app:assembleDebug`, `./gradlew :app:testDebugUnitTest` kada testovi postoje, i `./gradlew :app:lintDebug` kad je primenljivo. Ako naredbe/varijante ne postoje, prilagodi stvarnom Gradle projektu. Ne označavati task kao završen ako samo „AI kaže da je gotovo“.

## 6. Definition of Done — svaki PR

- Implementiran samo dogovoreni Jira scope.
- Barem ručno testirani success/empty/error ekran (za UI feature).
- Nema hardkodovanih boja ni lokalnog dupliranja shared komponenti.
- Proverene role i server-side autorizacija, ako task menja dozvole.
- Build i relevantni testovi prolaze ili je blokada jasno dokumentovana.
- Screenshot/Preview za UI promenu.
- Jedan od ostala tri člana pregleda PR pre merge-a.
- Kratak opis promena, koraci ručnog testiranja i eventualni rizici.

## 7. Praktična kontrola scope-a

MVP: login, 4 uloge, škole/smerovi, aktivnosti i prijave, Q&A forum, osnovna moderacija, profil. Video nije deo MVP-a. Finalna dokumentacija pokriva i dizajn, arhitekturu, podatke i bezbednost.
