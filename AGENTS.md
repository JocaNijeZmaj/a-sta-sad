# A šta sad? — pravila za AI agente i tim

> **Status:** Predlog ugovora za tim. Osnivač projekta potvrđuje stack i strukturu pre početka feature rada. Ovo je izvor istine za generisani kod; ako je repo drugačije već organizovan, najpre pitaj/vodi računa o postojećoj arhitekturi umesto da paralelno uvodiš novu.


## Obavezan dizajn ugovor (MVP)

- **Izvor istine za UI:** `docs/DESIGN_SYSTEM.md`. Za UI taskove pročitaj ovaj dokument i ponovo koristi postojeće `ui/theme/` i `ui/components/` elemente. Za zadatke bez UI-ja nije potrebno učitavati dizajn dokument.
- **Material 3 + Jetpack Compose** su jedini UI sistem za MVP; ne mešati XML layout, Material 2 i slučajne biblioteke komponenti u okviru feature-a.
- Nema novih HEX boja, fontova, padding skala ili zaobljenja unutar feature ekrana. Koristi `MaterialTheme.colorScheme`, `MaterialTheme.typography`, zajedničke dimenzije i komponente. Ako token nedostaje, predloži PR za design system pre korišćenja.
- Za sve ekrane koristiti zajednički `AstaTheme` (naziv uskladiti sa repoom), zajedničku navigacionu školjku i standardna loading/empty/error/success stanja.
- Za svaki UI PR priložiti `@Preview` ili screenshot sa test podacima. Uporedi izgled sa Figmom i ostalim ekranima; bez AI-generisanih slika u zvaničnim dizajnerskim predlozima za fakultet.
- **Dizajn ne sme biti novi izum za svaki Jira task.** Ekran prati sistem, a ne lični ukus AI agenta.
- Praktičan način rada i PR procedura: `docs/WORKFLOW.md`.

## 1. Proizvod i MVP

„A šta sad?“ je Android aplikacija inspirisana Redditom: **feed promotivnih aktivnosti škola** i **Q&A forum** gde srednjoškolci pitaju, a studenti Akademije i profesori odgovaraju. Video upload/reprodukcija, DM, push notifikacije, napredna analitika i plaćanja nisu deo MVP-a. Ne implementiraj ih bez posebnog taska.

### Uloge

- `ADMIN`: upravlja svim korisnicima, školama, smerovima, aktivnostima, prijavama, statistikama i moderacijom.
- `PROFESSOR`: kreira/menja aktivnosti **isključivo svoje škole**, upravlja njihovim terminima, kapacitetom i učesnicima, odgovara na forum pitanja; statistika svoje škole.
- `STUDENT`: student Akademije; čita sve aktivnosti, prijavljuje se, otkazuje uz napomenu, potvrđuje završetak uz komentar; odgovara na forumu.
- `HIGHSCHOOL_STUDENT`: srednjoškolac; čita sve aktivnosti i forum, postavlja pitanja, bira prihvaćen odgovor na **svoje** pitanje.

**Zajedničko za sve četiri uloge:** prijava/odjava/reset lozinke, čitanje aktivnosti i detalja, pretraga i filtriranje aktivnosti, čitanje foruma, ažuriranje *dozvoljenih* polja svog profila, glasanje za koristan odgovor i prijava neprimerenog sadržaja.

**Obavezno:** pravila pristupa moraju važiti na serveru/Firebase Security Rules (ili odgovarajućem backendu), a ne samo skrivanjem dugmeta. `schoolId`, `role` i `status` korisnik ne sme samostalno da menja. Kreiranje privilegovanog naloga ne sme biti javno dostupno.

## 2. Predloženi tehnološki dogovor

- Android: **Kotlin + Jetpack Compose + Material 3**.
- Arhitektura: **MVVM**, StateFlow/ViewModel, jednostavni Repository interfejsi, coroutines.
- Backend za studentski MVP: **Firebase Authentication + Cloud Firestore**, sa Security Rules, lokalnim emulatorom ako je izvodljivo.
- Navigacija: Navigation Compose; jedna Activity, ekrani po feature paketima.
- Dependency injection: prvo jednostavan `AppContainer` ili postojeće rešenje; ne dodavati Hilt bez odluke tima.
- Ne zaključavati verzije biblioteka u AI zadatku: poštovati `build.gradle.kts`, postojeći BOM i versions catalog iz repoa.

Ovo su **predlozi** dok vođa tima ne potvrdi. Ako se odluči Room/SQLite ili drugi backend, ažurirati AGENTS.md pre implementacije.

## 3. Predložena organizacija koda

```text
app/src/main/java/<package>/
  core/            # common UI, state, errors, util
  navigation/      # routes + role-based routing
  data/model/      # modeli, enum-i
  data/repository/ # ugovori i Firebase implementacije
  features/auth/
  features/profile/
  features/schools/
  features/activities/
  features/forum/
  features/admin/
  ui/theme/
```

**Konvencije:** Kotlin klase PascalCase, funkcije/varijable camelCase, svi stringovi u `strings.xml` kad to ima smisla, jedan javni ekran po fajlu, mali composable elementi, logika van UI-ja, UI stanje `Loading/Success/Empty/Error` ili ekvivalent; bez hardkodovanih lozinki/tajni.

## 4. Minimalni model podataka (uskladiti pre pisanja repozitorijuma)

| Kolekcija/model | Važna polja |
| --- | --- |
| `users/{uid}` | displayName, email, role, status, schoolId?, programId?, phone?, createdAt |
| `schools/{schoolId}` | name, city, description, website? |
| `programs/{programId}` | schoolId, title, description |
| `activities/{activityId}` | schoolId, authorId, title, description, type, startsAt, endsAt, location, capacity, cost, visitCount?, status, createdAt, updatedAt |
| `enrollments/{enrollmentId}` | activityId, studentId, status, cancellationReason?, completionNote?, createdAt, updatedAt |
| `questions/{questionId}` | authorId, schoolId?, title, body, tags, status, acceptedAnswerId?, createdAt, updatedAt |
| `answers/{answerId}` | questionId, authorId, body, createdAt, updatedAt |
| `answerVotes/{answerId_uid}` | answerId, userId, value, createdAt |
| `reports/{reportId}` | reporterId, targetType, targetId, reason, status, createdAt |

`PromoActivity` je naziv Kotlin modela da se ne meša sa `android.app.Activity`. Upisi u `enrollments` treba da spreče duple aktivne prijave. Brojanje kapaciteta i privilegovano kreiranje/banovanje naloga zahtevaju **trusted backend** ili rigorozno validirane atomske transakcije i Security Rules; UI provere same nisu dovoljne. Za demo obeležiti ograničenja ako server enforcement nije završen.

## 5. Dozvole i domen

1. **Aktivnost** je tekstualna objava stvarnog promotivnog događaja (sajam, obilazak škole, radionica). Nije isto što i forum pitanje.
2. Admin upravlja svim aktivnostima; profesor samo aktivnostima `activity.schoolId == currentUser.schoolId`.
3. Student može da menja samo **svoju** prijavu (prijava / otkazivanje / završetak); profesor može da upravlja učesnicima svojih aktivnosti.
4. Srednjoškolac postavlja pitanje, uređuje svoje pitanje ako to funkcionalnost podržava, i bira prihvaćen odgovor samo za svoje pitanje.
5. Student i profesor pišu odgovore, a samo autor odgovora menja sopstveni odgovor (admin može da ukloni sadržaj).
6. Sve uloge mogu da čitaju pitanja i odgovore, glasaju (jedan glas po korisniku/odgovoru) i prijave sadržaj.
7. Admin može da moderira prijave i sadržaj; svaka osetljiva operacija mora biti autorizovana.

## 6. Pravila rada po tasku (obavezno za svakog AI agenta)

Pre rada:
1. Pročitaj `AGENTS.md` i relevantne modele, interfejse i ekrane.
2. Pročitaj **samo zadati Jira task** i njegove zavisnosti. Ne implementiraj druge feature-e „usput“.
3. Proveri postojeću strukturu, verzije, naming i ugovore. Ako nešto nedostaje, dodaj minimalan ugovor ili jasno istakni blokadu.

Tokom rada:
1. Implementiraj mali, kompletan vertikalni slice: UI + ViewModel/state + repository + validacija + pravila pristupa gde treba.
2. Ne menjaj tuđe feature fajlove bez nužnog razloga i dokumentovanja. Ne refaktoriši ceo projekat.
3. Bez placeholder logike koja lažno tvrdi uspeh; kad backend nije spreman, koristi eksplicitni fake repository za preview/demo.
4. Za korisničke akcije obezbedi loading, empty, error, success i razumljive poruke.
5. Ne upisuj tajne, API private keys, service account JSON, korisničke lozinke ili lične podatke stvarnih osoba u repo.
6. Piši jednostavan, čitljiv Kotlin sa kratkim objašnjenjima tamo gde logika nije trivijalna.

Pre predaje:
1. Pokreni dostupni build, unit test i lint; prijavi tačno šta je prošlo, šta nije i zašto.
2. Uz PR dodaj: sažetak, izmenjene fajlove, ručne korake provere, screenshot ako je UI, eventualne migracije/rules/indexes i poznata ograničenja.
3. Završen task = ispunjeni kriterijumi prihvatanja + osnovna provera 4 uloge, ako task dira autorizaciju.

## 7. Git i integracija

- Svaki Jira task ide u posebnu granu: `feature/<TASK-ID>-kratak-opis`.
- Pull request u `dev` ili zajednički integration branch, po dogovoru; obavezna provera najmanje jednog kolege.
- Ne commitovati direktno na `main`.
- Jedan task treba da bude spojiv bez čekanja nedovršenog ogromnog PR-a.
- Feature su zasebni po vlasništvu, ali zajedničke promene u modelima i pravilima proverava vođa projekta.

## 8. Definicija završetka (DoD)

- Implementirana funkcionalnost odgovara tasku i matrici ovlašćenja.
- Ekran radi u normalnom, praznom i greškom opterećenom stanju.
- Proverene dozvoljene i zabranjene akcije, ne samo prikaz dugmeta.
- Build/test komande uspešne ili dokumentovan razlog što nisu.
- Kratak opis testiranja, eventualno screenshot i ažurirana dokumentacija.

## 9. Predaja za Android programiranje

Za dizajn pripremiti pet skica: welcome/login, admin panel, profesor panel, student panel, dodavanje korisnika. **AI-generisane slike nisu dozvoljene za zvanične skice**; raditi ih ručno u Figmi/draw.io. Na kraju dokumentacija približno 20+ strana sa zahtevima, arhitekturom, modelom baze, ekranima, implementacijom, testovima, bezbednošću, mogućim poboljšanjima i izvorima.

## 10. Nice-to-have / posle MVP-a

Upload i reprodukcija videa; notifikacije; praćenje škola; napredni ranking; privatne poruke. Ne planirati kao obavezne taskove.
