# A šta sad? — implementation backlog

> Source: `A_sta_sad_Tracker_sa_Firebase.xlsx` (35 main tasks + 21 Firebase subtasks) and the original `JIRA_BACKLOG.md` (acceptance criteria and implementation briefs).
> Updated for the current repository on 2026-11-25: `CORE-01` and `CORE-02` are DONE according to the project implementation and commit reported in chat. Verify tracker status separately.

## Product scope

A Kotlin/Jetpack Compose Android app for discovering school and academy activities and asking/answering education-related questions. The experience is an activities feed plus Reddit-like Q&A forum.

- Roles: `ADMIN`, `PROFESSOR`, `STUDENT` (academy), and `HIGHSCHOOL_STUDENT`.
- Professors manage activities only for their school; students can answer questions; high-school students can ask questions.
- Video upload/playback is **nice-to-have**, outside the MVP.
- Access checks must be enforced in trusted backend/Firestore Security Rules, never only by hiding Android UI elements.

## How Codex should use this file

1. Read `../AGENTS.md`, `README.md`, and `docs/WORKFLOW.md`; read `docs/DESIGN_SYSTEM.md` when touching UI.
2. Inspect existing code and the selected task's dependencies. **Implement only one task per change/PR**, using the existing Kotlin/Compose/Material 3/MVVM/repository architecture.
3. Use the **Goal**, **Implementation brief**, **Constraints**, and **Acceptance criteria** in the selected task as its AI specification. Do not paste generalized boilerplate from the old Jira export that conflicts with the task's scope.
4. Propose a short file-level plan and wait for approval before changes when requested. Reuse existing implementations; do not recreate completed CORE foundations.
5. Add loading/empty/error states where data/UI operations actually require them. Add unit tests and Firebase Rules tests **only when relevant** to the task; do not invent a backend or rule changes for a placeholder UI task.
6. Run available build, unit tests, and lint; report changed files, manual verification, failures/warnings, and unresolved dependencies. Do not commit/push unless explicitly requested.
7. Treat the spreadsheet as a human-facing tracker. This file is the repository-side specification; synchronize task status after merging work.

**Ready-to-use Codex prompt:** `Read AGENTS.md and docs/BACKLOG.md. Inspect the current project, then propose a plan for TASK-ID including dependencies, files, and verification. Do not change files until I approve.` Replace `TASK-ID` with the exact ID below.

## Status and dependencies

- `DONE`: implemented and reported committed. `TODO`: not marked completed in this project conversation. `BLOCKED`: only if explicit prerequisites are missing; not an automatic label.
- Main-task dependencies come from the **latest Excel tracker**, not assumptions based on task numbering. Tasks within the same wave may run in parallel if their dependencies are satisfied.
- The original Jira brief lists `CORE-03` as an additional dependency of `CORE-04`, while the newer Excel tracker lists only `CORE-01`. This backlog follows the **Excel dependency**. Check Firebase-specific assumptions before freezing Firestore contracts; `FB-04` formalizes the database schema.
- Firebase tasks (`FB-*`) are **subtasks that refine existing main Jira tasks**. Do not double-count their estimated hours with the parent tasks.
- Start by using `CORE-03`, `CORE-04`, or `CORE-06` as appropriate; verify Firebase setup before implementing Firebase-dependent features.

## Main tasks (35)

## Wave T01

### CORE-01 — Android projekat i struktura modula

- **Status:** `DONE` | **Area:** Osnova projekta | **Priority:** Kritično | **Estimate:** 4 h
- **Depends on:** None | **Related workstream:** A · Zajednički temelj
- **Goal:** Repo se kompajlira i svi mogu da pokrenu aplikaciju.
- **Implementation brief:** Kreiraj Kotlin/Compose projekat, Git repozitorijum, dogovorenu package strukturu, početni navigation host i osnovni README; uvedi AGENTS.md kao pravilo za ceo tim.
- **Constraints:** Ne implementirati auth, feed ili forum logiku.
- **Acceptance criteria:** Clean clone se builduje; postoje placeholder rute Login, Feed, Forum, Profile; README navodi postupak pokretanja.
- **Repository reality:** Existing Android project, NavHost, Login/Activities/Forum/Profile placeholders and working Gradle build were delivered in CORE-01. Do not recreate them.

## Wave T02

### CORE-02 — Tema i skeleton pet obaveznih ekrana

- **Status:** `DONE` | **Area:** Osnova projekta | **Priority:** Kritično | **Estimate:** 4 h
- **Depends on:** `CORE-01` | **Related workstream:** A · Zajednički temelj
- **Goal:** Tim ima zajednički vizuelni i navigacioni okvir.
- **Implementation brief:** Podesi Material 3 theme, tipografiju, boje, shared button/text field/card/error composables i skeleton ekrane welcome/login, admin, profesor, student, dodavanje korisnika; dogovoriti ručne Figma skice.
- **Constraints:** Ostavi samostalne ekran-komponente koje feature developeri mogu zameniti.
- **Acceptance criteria:** Pet placeholder ekrana postoji; UI je konzistentan; nema AI-generisanih slika za zvanične skice; nema biznis logike.
- **Repository reality:** Existing Material 3 tokens, shared `AstaButton`/`AstaTextField`/`AstaCard`/`AstaErrorMessage`, and role-based placeholder previews were delivered in CORE-02. No role authorization was implemented. Confirm any separately required hand-drawn Figma sketches before academic submission.

### CORE-03 — Firebase razvojno okruženje

- **Status:** `TODO` | **Area:** Osnova projekta | **Priority:** Kritično | **Estimate:** 3 h
- **Depends on:** `CORE-01` | **Related workstream:** A · Zajednički temelj
- **Goal:** Android projekat je povezan na kontrolisani dev Firebase projekat.
- **Implementation brief:** Dodata Auth/Firestore konfiguracija, Firebase emulator uputstvo ili alternativni fake repository, lokalni config setup i bezbedno upravljanje kredencijalima.
- **Constraints:** Ne konfigurisati produkciju i ne ostavljati javno upisivo pravilo Firestore-a.
- **Acceptance criteria:** Aplikacija startuje u dev režimu; u README piše kako povezati Firebase; nikakav service account key nije u Git-u.

### CORE-04 — Dogovoreni modeli i Repository interfejsi

- **Status:** `TODO` | **Area:** Osnova projekta | **Priority:** Kritično | **Estimate:** 4 h
- **Depends on:** `CORE-01` | **Related workstream:** A · Zajednički temelj
- **Goal:** Feature timovi koriste iste tipove i potpise API-ja.
- **Implementation brief:** Napravi UserProfile, School, Program, PromoActivity, Enrollment, Question, Answer, AnswerVote, ContentReport, njihove enum-e i Repository interfejse bez implementacije konkretnih ekrana.
- **Constraints:** Za PromoActivity ne koristiti klasu Activity; role, schoolId i status nisu obična korisnička edit polja.
- **Acceptance criteria:** Modeli imaju stabilne id, uloge/status/polja i vremenske oznake; postoji dokument koji mapira Firestore kolekcije; sve prolazi build.
- **Coordination:** Define stable contracts for all feature teams before adding concrete Firestore implementations. Align collection fields with `FB-04`.

### CORE-06 — Git workflow i CI

- **Status:** `TODO` | **Area:** Osnova projekta | **Priority:** Visok | **Estimate:** 2 h
- **Depends on:** `CORE-01` | **Related workstream:** A · Zajednički temelj
- **Goal:** Pull request otkriva build greške pre merge-a.
- **Implementation brief:** Definiši branch/PR konvencije, PR template i GitHub Actions za Gradle assemble i test; dokumentuj verzije JDK/Android SDK na runneru.
- **Constraints:** Ne dodavati deploy kredencijale ili nepotrebne spoljne servise.
- **Acceptance criteria:** Otvoren test PR automatski izvršava build i test; README opisuje review i grane.

## Wave T03

### CORE-05 — Fake repository i demo primeri

- **Status:** `TODO` | **Area:** Osnova projekta | **Priority:** Visok | **Estimate:** 2 h
- **Depends on:** `CORE-04` | **Related workstream:** A · Zajednički temelj
- **Goal:** Svi mogu da razvijaju UI dok Firebase backend još nije gotov.
- **Implementation brief:** Daj local fake implementacije za 4 uloge, škole, bar pet aktivnosti i nekoliko Q&A tema, uz lako prebacivanje između fake i live izvora.
- **Constraints:** Fake repository nije produkcioni backend i ne sme simulirati uspešan upis bez objašnjenja.
- **Acceptance criteria:** Feed i Q&A preview dobijaju realistične testne podatke; fake podaci su jasno označeni; bez zavisnosti od mreže.

### ID-01 — Login i logout svih uloga

- **Status:** `TODO` | **Area:** Korisnici i škole | **Priority:** Kritično | **Estimate:** 4 h
- **Depends on:** `CORE-02`, `CORE-03`, `CORE-04` | **Related workstream:** B1 · Nalozi
- **Goal:** Sve četiri uloge mogu da pristupe aplikaciji preko istog login ekrana.
- **Implementation brief:** Login forma, validacija email/password, Firebase Auth prijava i odjava, izlaganje session state; preusmeravanje nakon prijave preko postojećeg navigation host-a.
- **Constraints:** Ne autorizovati korisnika samo preko hardkodovanog email-a.
- **Acceptance criteria:** Validni korisnici se loguju; pogrešan password daje poruku; logout briše sesiju i vraća na login.

### ACT-01 — Feed aktivnosti za sve uloge

- **Status:** `TODO` | **Area:** Aktivnosti | **Priority:** Kritično | **Estimate:** 5 h
- **Depends on:** `CORE-02`, `CORE-04` | **Related workstream:** C1 · Feed
- **Goal:** Svi vide tekstualne promotivne aktivnosti u Reddit-stilu.
- **Implementation brief:** Compose feed kartice sa naslovom, školom, tipom, vremenom, opisom, statusom i akcijom za detalje; repozitorijum read, paging po potrebi.
- **Constraints:** Prikazuj samo dozvoljene/objavljene aktivnosti.
- **Acceptance criteria:** Sve 4 uloge vide isti read feed; uredan empty/loading/error; nema video upload komponenata.

### FOR-01 — Q&A lista i pretraga pitanja

- **Status:** `TODO` | **Area:** Q&A forum | **Priority:** Kritično | **Estimate:** 4 h
- **Depends on:** `CORE-02`, `CORE-04` | **Related workstream:** D1 · Osnovni forum
- **Goal:** Svi čitaju i pretražuju forum.
- **Implementation brief:** Question list kartice: title, tag/category, school, author displayName, answer count, createdAt, search i filter tag/school.
- **Constraints:** MVP je tekst, bez video snimaka i privatnih poruka.
- **Acceptance criteria:** Sve 4 uloge čitaju objavljena pitanja; pretraga radi; empty/loading/error postoje.

## Wave T04

### ID-02 — Registracija i zaboravljena lozinka

- **Status:** `TODO` | **Area:** Korisnici i škole | **Priority:** Visok | **Estimate:** 4 h
- **Depends on:** `ID-01` | **Related workstream:** B1 · Nalozi
- **Goal:** Novi srednjoškolac može bezbedno napraviti nalog i resetovati lozinku.
- **Implementation brief:** Registration ekran za HIGHSCHOOL_STUDENT nalog, validacija, Firebase reset password email flow, korisnička stanja i linkovi iz login-a.
- **Constraints:** Privilegovane naloge stvara/odobrava admin, ne otvorena registracija.
- **Acceptance criteria:** Validna registracija kreira minimalni profil; reset šalje email; samoregistracija nikada ne daje ADMIN/PROFESSOR/STUDENT ulogu.

### ID-03 — Uloge i role-based navigacija

- **Status:** `TODO` | **Area:** Korisnici i škole | **Priority:** Kritično | **Estimate:** 3 h
- **Depends on:** `ID-01`, `CORE-04` | **Related workstream:** B1 · Nalozi
- **Goal:** Korisnik vidi samo odgovarajući panel, bez duplog layouta.
- **Implementation brief:** Route guard na osnovu UserProfile.role i status; početni panel Admin/Profesor/Student/Srednjoškolac uz zajedničke Feed, Forum, Profile ekrane.
- **Constraints:** Sakriveno dugme nije zamena za backend autorizaciju.
- **Acceptance criteria:** 4 test naloga otvaraju odgovarajući panel; neprijavljen/banovan ne pristupa privatnim ekranima; direktna duboka navigacija ne zaobilazi guard.

### ACT-02 — Pretraga i filtriranje aktivnosti

- **Status:** `TODO` | **Area:** Aktivnosti | **Priority:** Visok | **Estimate:** 3 h
- **Depends on:** `ACT-01` | **Related workstream:** C1 · Feed
- **Goal:** Feed je lako pretraživati po školi i datumu.
- **Implementation brief:** Search polje i filteri school/type/date/status; jasno dugme reset; dogovoriti Firestore indekse ako su potrebni.
- **Constraints:** Ne graditi generički search engine, minimalni jednostavni filteri su dovoljni.
- **Acceptance criteria:** Filtriranje po školi i datumu radi i bez rezultata; svi imaju jednak read access.

### ACT-03 — Detalj promotivne aktivnosti

- **Status:** `TODO` | **Area:** Aktivnosti | **Priority:** Visok | **Estimate:** 3 h
- **Depends on:** `ACT-01` | **Related workstream:** C1 · Feed
- **Goal:** Korisnik vidi kompletnu objavu i dostupne akcije za svoju rolu.
- **Implementation brief:** Detail UI: title, author/school, opis, vreme, lokacija, trajanje, broj mesta, učesnici/kontakti uz zaštitu privatnosti i kontekstualna CTA dugmad.
- **Constraints:** Samo minimalni kontakt podaci bez nepotrebnog prikaza ostalih učesnika srednjoškolcima.
- **Acceptance criteria:** Sve 4 uloge otvaraju detalj; student vidi prijavu ako može; profesor vidi edit samo za svoju školu; zabranjeni podaci nisu izloženi.

### FOR-03 — Stranica pitanja i lista odgovora

- **Status:** `TODO` | **Area:** Q&A forum | **Priority:** Kritično | **Estimate:** 4 h
- **Depends on:** `FOR-01` | **Related workstream:** D1 · Osnovni forum
- **Goal:** Svako može da pročita diskusiju i status prihvaćenog odgovora.
- **Implementation brief:** Question detail, lista odgovora, sortiranje po datumu ili korisnosti, empty state za bez odgovora, accepted badge.
- **Constraints:** Ne kopirati Reddit UX 1:1; zadrži edukativno prepoznatljiv i jednostavan dizajn.
- **Acceptance criteria:** Svi vide pitanje i dostupne odgovore; loading/error rade; autor i vreme su vidljivi.

## Wave T05

### ID-04 — Moj profil i izmena svojih podataka

- **Status:** `TODO` | **Area:** Korisnici i škole | **Priority:** Visok | **Estimate:** 4 h
- **Depends on:** `ID-03` | **Related workstream:** B1 · Nalozi
- **Goal:** Korisnik može da vidi i izmeni samo dozvoljene podatke.
- **Implementation brief:** Profile read/edit Compose ekrani, validacija displayName, phone, address i sličnih dozvoljenih polja, repository update.
- **Constraints:** Bez čuvanja plain text lozinke u Firestore.
- **Acceptance criteria:** Sve četiri uloge vide profil; upis svojih podataka radi; role, status i schoolId ne mogu se menjati preko UI-ja niti rules.

### ID-05 — Škole i smerovi – katalog i admin CRUD

- **Status:** `TODO` | **Area:** Korisnici i škole | **Priority:** Visok | **Estimate:** 5 h
- **Depends on:** `CORE-04`, `ID-03` | **Related workstream:** B2 · Škole i pravila
- **Goal:** Sistem ima škole i smerove povezane sa korisnicima i aktivnostima.
- **Implementation brief:** List/detail škola i smerova; admin može create/update/delete; drugi samo pregled; validirati reference koje koriste activity/forum.
- **Constraints:** Izbegni brisanje škole sa vezanim aktivnostima ili prikaži jasan blokirajući uslov.
- **Acceptance criteria:** Admin dodaje školu/smer; student i srednjoškolac samo čitaju; profesor može pristupati podacima svoje škole bez izmena kataloga.

### FOR-02 — Postavljanje pitanja srednjoškolca

- **Status:** `TODO` | **Area:** Q&A forum | **Priority:** Kritično | **Estimate:** 4 h
- **Depends on:** `FOR-01`, `ID-03` | **Related workstream:** D1 · Osnovni forum
- **Goal:** Srednjoškolac otvara novu javnu Q&A temu.
- **Implementation brief:** Create question forma sa title, body, tags, opcionim schoolId; validacija obaveznih polja i upis authorId.
- **Constraints:** Ne dozvoliti klijentu da upiše tuđ authorId.
- **Acceptance criteria:** HIGHSCHOOL_STUDENT kreira pitanje; druge uloge ne mogu; autor i vreme se čuvaju; prazno pitanje se odbija.

### FOR-07 — Prijavljivanje neprimerenog sadržaja

- **Status:** `TODO` | **Area:** Q&A forum | **Priority:** Srednji | **Estimate:** 3 h
- **Depends on:** `FOR-03` | **Related workstream:** D2 · Forum dodaci
- **Goal:** Sve uloge mogu prijaviti pitanje ili odgovor.
- **Implementation brief:** Report form sa razlogom i targetType/targetId, repository create, potvrda prijave i sprečavanje spama/duplih prijava gde može.
- **Constraints:** Prijava nije isto što i automatsko brisanje sadržaja.
- **Acceptance criteria:** Sve 4 uloge šalju report; report čuva reporterId; UI ne dozvoljava lažiranje drugih korisnika.

## Wave T06

### ID-06 — Admin: korisnici, uloge i status

- **Status:** `TODO` | **Area:** Korisnici i škole | **Priority:** Visok | **Estimate:** 5 h
- **Depends on:** `ID-03`, `ID-05` | **Related workstream:** B2 · Škole i pravila
- **Goal:** Admin upravlja korisnicima bez mogućnosti samododele privilegija.
- **Implementation brief:** Admin user list/search/detail, kreiranje poziva ili poverljivog kreiranja privilegovanih naloga, promena statusa aktivan/banovan, osnovno editovanje korisničkih podataka.
- **Constraints:** Ne pozivaj Firebase Admin SDK iz Android klijenta; ako trusted endpoint nije postavljen, isporuči UI i jasno definisan backend contract kao blokadu.
- **Acceptance criteria:** Admin vidi sve naloge; profesor i studenti ne mogu menjati tuđe naloge; kreiranje privilegovanog naloga ide kroz trusted backend/admin proces.

### ID-07 — Firestore Security Rules za identitet i škole

- **Status:** `TODO` | **Area:** Korisnici i škole | **Priority:** Kritično | **Estimate:** 4 h
- **Depends on:** `ID-03`, `ID-05` | **Related workstream:** B2 · Škole i pravila
- **Goal:** Backend sprečava promenu uloga i cross-school pristup.
- **Implementation brief:** Security Rules i emulator testovi za users, schools i programs; pravila privileged field immutable, admin-only updates, profesor school scope i status blokadu.
- **Constraints:** Pravila podataka moraju biti testirana protiv direktnih klijentskih pokušaja.
- **Acceptance criteria:** Testovi dokazuju: korisnik ne može role/status/schoolId; profesor ne postaje admin; običan korisnik ne menja školu; banovanom nisu dozvoljeni upisi.

## Wave T07

### ACT-04 — Kreiranje aktivnosti – admin i profesor škole

- **Status:** `TODO` | **Area:** Aktivnosti | **Priority:** Kritično | **Estimate:** 5 h
- **Depends on:** `ACT-03`, `ID-05`, `ID-07` | **Related workstream:** C2 · CRUD aktivnosti
- **Goal:** Ovlašćeni korisnici mogu da objave promotivnu aktivnost.
- **Implementation brief:** Create form za title, type, description, schoolId, start/end, location, capacity i cost; repository create, validacije i odgovarajući defaults.
- **Constraints:** Profesorov schoolId određuje server/trusted profil, ne proizvoljan dropdown.
- **Acceptance criteria:** Admin kreira za bilo koju školu; profesor samo za svoju; student i srednjoškolac ne mogu ni preko API-ja; start < end, capacity > 0, cost >= 0.

### ACT-06 — Studentska prijava i kapacitet

- **Status:** `TODO` | **Area:** Aktivnosti | **Priority:** Kritično | **Estimate:** 6 h
- **Depends on:** `ACT-03`, `CORE-05`, `ID-07` | **Related workstream:** C3 · Prijave
- **Goal:** Student Akademije može zauzeti slobodno mesto bez dupliranja.
- **Implementation brief:** Apply/enroll dugme, write operacija, Enrollment model, status prijave, broj slobodnih mesta, validacija duple prijave i kapaciteta.
- **Constraints:** Za pouzdanu zaštitu kapaciteta koristi trusted operaciju/transakciju i pravila; bez toga označi backend blocker, ne tvrdi da je UI proverom rešeno.
- **Acceptance criteria:** STUDENT može prijavu na otvorenu aktivnost sa mestima; ponovljen klik ne pravi duplikat; druge 3 uloge ne mogu self-enroll; stanje je konzistentno.

### FOR-04 — Odgovaranje studenata i profesora

- **Status:** `TODO` | **Area:** Q&A forum | **Priority:** Kritično | **Estimate:** 5 h
- **Depends on:** `FOR-03`, `ID-07` | **Related workstream:** D1 · Osnovni forum
- **Goal:** Studenti i profesori javno odgovaraju na srednjoškolska pitanja.
- **Implementation brief:** Create/edit/delete own answer UI+repo, validacija sadržaja, authorId i timestamps; ažuriraj prikaz thread-a.
- **Constraints:** Admin ima moderaciju, ne učestvuje kao odgovarajući korisnik osim ako se matrica ne promeni.
- **Acceptance criteria:** STUDENT i PROFESSOR mogu odgovoriti, uređuju samo svoj odgovor; ADMIN i HIGHSCHOOL_STUDENT ne mogu kreirati odgovor u ovom MVP modelu.

### FOR-08 — Admin moderacija pitanja i odgovora

- **Status:** `TODO` | **Area:** Q&A forum | **Priority:** Visok | **Estimate:** 5 h
- **Depends on:** `FOR-07`, `ID-07` | **Related workstream:** D2 · Forum dodaci
- **Goal:** Admin može da pregleda prijave i sakrije/ukloni neprimeren sadržaj.
- **Implementation brief:** Admin reports queue, approve/dismiss/moderate akcije, soft hide pitanja i odgovora, statusne poruke i server permissions.
- **Constraints:** Poštuj audit polja i ne menjaj korisničke role u forum modulu.
- **Acceptance criteria:** Samo admin moderira; prijavljen sadržaj može ostati javno vidljiv do odluke; uklonjen nije javno prikazan.

## Wave T08

### ACT-05 — Izmena, otkazivanje i brisanje aktivnosti

- **Status:** `TODO` | **Area:** Aktivnosti | **Priority:** Visok | **Estimate:** 5 h
- **Depends on:** `ACT-04` | **Related workstream:** C2 · CRUD aktivnosti
- **Goal:** Admin i profesor upravljaju postojećim objavama po ovlašćenjima.
- **Implementation brief:** Edit activity, status CANCELLED, eventualni soft delete, promene termina/lokacije/kapaciteta i poruke učesnicima na ekranu.
- **Constraints:** Prednost soft-delete zbog postojećih prijava i istorije.
- **Acceptance criteria:** Admin menja sve; profesor samo svoju školu; student/srednjoškolac odbijeni; otkazana aktivnost ne prima prijave.

### ACT-07 — Otkazivanje i potvrda završetka učešća

- **Status:** `TODO` | **Area:** Aktivnosti | **Priority:** Visok | **Estimate:** 4 h
- **Depends on:** `ACT-06` | **Related workstream:** C3 · Prijave
- **Goal:** Student uređuje samo svoju prijavu kroz njen životni ciklus.
- **Implementation brief:** Prikaz Mojih prijava; cancel uz obavezan razlog; mark completed uz komentar; prikaz statusa, termini i istorija.
- **Constraints:** Validirati dozvoljene prelaze statusa i ne dozvoliti completion pre kraja aktivnosti.
- **Acceptance criteria:** Student otkazuje samo svoju prijavu i unosi razlog; može potvrditi završetak uz komentar samo kad je aktivnost održana; ostale uloge ne mogu menjati tuđe studentske prijave.

### ACT-08 — Profesor/admin upravlja učesnicima

- **Status:** `TODO` | **Area:** Aktivnosti | **Priority:** Visok | **Estimate:** 5 h
- **Depends on:** `ACT-06`, `ID-05` | **Related workstream:** C3 · Prijave
- **Goal:** Odgovorni za aktivnost dodaju/uklanjaju studente uz evidenciju.
- **Implementation brief:** Lista prijavljenih, add/remove student, prikaz statusa i napomene, provere school scope i kapaciteta, admin globalno.
- **Constraints:** Ne prikazivati listu ličnih podataka učesnika drugim ulogama.
- **Acceptance criteria:** Profesor upravlja samo za svoju školu, admin za sve; student ne može menjati druge; nema duplih prijava.

### FOR-05 — Glasanje za korisne odgovore

- **Status:** `TODO` | **Area:** Q&A forum | **Priority:** Srednji | **Estimate:** 3 h
- **Depends on:** `FOR-04` | **Related workstream:** D2 · Forum dodaci
- **Goal:** Sve uloge mogu jedanput da pozitivno ocene odgovor.
- **Implementation brief:** Like/upvote toggle sa unique answerId+uid, prikaz broja glasova, idempotentno ponašanje.
- **Constraints:** Bez negativnih glasova dok MVP nije završen.
- **Acceptance criteria:** Sve 4 uloge glasaju; dvostruki klik ne kreira duple glasove; korisnik može ukloniti svoj glas.

### FOR-06 — Prihvaćen odgovor – autor pitanja

- **Status:** `TODO` | **Area:** Q&A forum | **Priority:** Visok | **Estimate:** 3 h
- **Depends on:** `FOR-04` | **Related workstream:** D2 · Forum dodaci
- **Goal:** Srednjoškolac označava najbolji odgovor na svoje pitanje.
- **Implementation brief:** Action accept answer, acceptedAnswerId na pitanju, UI badge i mogućnost promene izbora.
- **Constraints:** Ne mešati upvote i accepted answer – ovo su odvojene funkcionalnosti.
- **Acceptance criteria:** Samo autor pitanja bira/prerađuje accepted odgovor; ne može se prihvatiti odgovor drugog pitanja; svi vide oznaku.

### QA-03 — Demo nalozi i realistični seed podaci

- **Status:** `TODO` | **Area:** Završnica | **Priority:** Visok | **Estimate:** 3 h
- **Depends on:** `CORE-05`, `ID-05`, `ACT-04`, `FOR-04` | **Related workstream:** E · Završnica
- **Goal:** Odbrana se može izvesti sa unapred pripremljenim slučajevima.
- **Implementation brief:** Pripremi minimum dva admina, pet profesora, trideset studenata Akademije, nekoliko srednjoškolaca, više škola, deset promotivnih aktivnosti, Q&A pitanja i odgovore.
- **Constraints:** Bez stvarnih ličnih podataka i passworda u Git-u.
- **Acceptance criteria:** Demo scenario pokriva 4 uloge i jedan potpuni put Activity+Q&A; svi podaci su izmišljeni; seed je ponovljiv.

## Wave T09

### ACT-09 — Statistika aktivnosti i troškova

- **Status:** `TODO` | **Area:** Aktivnosti | **Priority:** Srednji | **Estimate:** 5 h
- **Depends on:** `ACT-05`, `ACT-08` | **Related workstream:** C4 · Statistika
- **Goal:** Profesor vidi učinak svoje škole, admin svih škola.
- **Implementation brief:** Mali dashboard: broj budućih/prošlih događaja, prijava, realizovanih aktivnosti, poseta i zbir troškova uz osnovne filtere.
- **Constraints:** Bez napredne BI platforme, 3–5 jednostavnih KPI kartica dovoljno.
- **Acceptance criteria:** Profesor vidi samo svoje školske aktivnosti; admin globalno; prazan skup prikazuje nule; brojevi potiču iz podataka, ne iz hardkoda.

## Wave T10

### QA-01 — Testiranje matrice ovlašćenja sve četiri uloge

- **Status:** `TODO` | **Area:** Završnica | **Priority:** Kritično | **Estimate:** 4 h
- **Depends on:** `ID-07`, `ACT-09`, `FOR-08` | **Related workstream:** E · Završnica
- **Goal:** Matrica prava je stvarno sprovedena, ne samo nacrtana.
- **Implementation brief:** Napravi test scenarije za 4 uloge × aktivnosti/Q&A/admin/profile; testiraj dozvoljene i zabranjene operacije uključujući direktni repository/security rules pristup.
- **Constraints:** Obavezno proveriti school scope i owner scope.
- **Acceptance criteria:** Postoji tabela testova sa pass/fail; nijedna zabranjena akcija nije izvršiva; pronađeni bugovi imaju Jira tickets.

### QA-02 — UX završnica i oporavak od grešaka

- **Status:** `TODO` | **Area:** Završnica | **Priority:** Visok | **Estimate:** 4 h
- **Depends on:** `ID-06`, `ACT-09`, `FOR-08` | **Related workstream:** E · Završnica
- **Goal:** Aplikacija je stabilna za demonstraciju pred komisijom.
- **Implementation brief:** Prođi kroz loading, empty, error, offline, keyboard/ime, scroll, back navigation, nevažeći datumi i vrlo duge tekstove u svim ekranima.
- **Constraints:** Ne uvoditi novi UI framework u završnici.
- **Acceptance criteria:** Nema crash-eva u ključnim tokovima; korisnik dobija povratnu informaciju o grešci i uspešnoj akciji; ekrani odgovaraju brendingu.

## Wave T11

### QA-04 — Tehnička dokumentacija i UML finalizacija

- **Status:** `TODO` | **Area:** Završnica | **Priority:** Visok | **Estimate:** 5 h
- **Depends on:** `QA-01`, `QA-02` | **Related workstream:** E · Završnica
- **Goal:** Pisani izveštaj pokriva zahteve, razvoj i testove.
- **Implementation brief:** Ažuriraj use case, model klase/tabela i matricu uloga; napiši poglavlja: problem, postojeća rešenja, branding, arhitektura/baza, razvoj, komponente, veze, testiranje, bezbednost, budući razvoj, literatura.
- **Constraints:** Skice dizajna raditi ručno, bez AI-generisanih slika za zvaničnu predaju.
- **Acceptance criteria:** Dokumentacija ima najmanje oko 20 strana sa slikama, dijagramima i pravilno citiranim izvorima; svako piše svoj feature deo.

## Wave T12

### QA-05 — Demo scenariji i odbrana

- **Status:** `TODO` | **Area:** Završnica | **Priority:** Visok | **Estimate:** 3 h
- **Depends on:** `QA-03`, `QA-04` | **Related workstream:** E · Završnica
- **Goal:** Svako od četvoro ume da prikaže funkcionalnost i objasni kod.
- **Implementation brief:** Kreiraj walkthrough za login 4 uloge, kreiranje aktivnosti, prijavu studenta, pitanje srednjoškolca, odgovor studenta/profesora, moderaciju; usaglasiti delove odbrane.
- **Constraints:** Bez novih MVP feature-a tokom pripreme odbrane.
- **Acceptance criteria:** Demo može proći bez resetovanja baze; svaki član predstavlja svoju funkcionalnost, svi znaju gde je AGENTS.md i kako rade rules.

## Firebase implementation subtasks (21)

These refine the parent tasks above. Use the **Firebase dependencies** and **external prerequisites** shown with each subtask. Production deployment and privileged-account creation must not be assumed safe or available by default.

### FB-01 — Firebase projekat i Android aplikacija

- **Status:** `TODO` | **Area:** Infrastruktura | **Priority:** Srednji | **Estimate:** 2 h
- **Firebase dependencies:** None | **External prerequisite:** `CORE-01`
- **Refines Jira tasks:** `CORE-03` | **Related workstream:** Firebase setup
- **Implementation brief:** Napravi Firebase razvojni projekat za A šta sad?. Registruj Android app sa postojećim applicationId, izaberi region za Firestore i uključi Email/Password Authentication. Dokumentuj korake za članove tima, pravila pristupa konzoli i Firebase project ID. Ne koristi produkcione podatke.
- **Acceptance criteria:** Firebase dev projekat postoji; app package ID ispravan; Auth/Firestore omogućeni; pristup tima ograničen.
- **Plan/deployment note:** Spark

### FB-02 — Firebase SDK, BoM i Gradle integracija

- **Status:** `TODO` | **Area:** Infrastruktura | **Priority:** Srednji | **Estimate:** 3 h
- **Firebase dependencies:** `FB-01` | **External prerequisite:** None
- **Refines Jira tasks:** `CORE-03` | **Related workstream:** Firebase setup
- **Implementation brief:** U postojećem Kotlin/Compose projektu dodaj Google Services Gradle plugin, Firebase Android BoM i firebase-auth/firestore (po potrebi functions). Poštuj versions catalog. Postavi google-services.json za lokalni razvoj i proveri da konfiguracija nije tajna ali nema service-account JSON u repo-u. Pokreni Gradle sync i assembleDebug.
- **Acceptance criteria:** Gradle sync i assembleDebug prolaze; Google Services plugin aktivan; koristi main Firebase module, bez zastarelih -ktx.
- **Plan/deployment note:** Spark

### FB-03 — Firebase CLI + lokalni emulatori

- **Status:** `TODO` | **Area:** Infrastruktura | **Priority:** Srednji | **Estimate:** 3 h
- **Firebase dependencies:** `FB-01` | **External prerequisite:** None
- **Refines Jira tasks:** `CORE-03` | **Related workstream:** Firebase setup
- **Implementation brief:** Postavi Firebase CLI konfiguraciju, Auth i Firestore Local Emulator Suite, opcionalno Functions emulator. Dodaj firestore.rules i firestore.indexes.json sa podrazumevanim deny-by-default pravilima. Omogući debug-only vezu ka emulatorima preko 10.0.2.2 za Android Emulator; ne dozvoli da release build koristi lokalni emulator.
- **Acceptance criteria:** firebase.json i .firebaserc rade; Auth/Firestore emulatori startuju; Android debug ide na 10.0.2.2; produkcija ne koristi emulator.
- **Plan/deployment note:** Spark

### FB-04 — Zaključavanje Firestore šeme i indeksa

- **Status:** `TODO` | **Area:** Model i ugovori | **Priority:** Srednji | **Estimate:** 4 h
- **Firebase dependencies:** None | **External prerequisite:** `CORE-04`
- **Refines Jira tasks:** `CORE-04` | **Related workstream:** Baza i ugovori
- **Implementation brief:** Na osnovu postojećeg AGENTS.md i Kotlin modela definiši konačnu Firestore šemu: users, schools, programs, activities, enrollments, questions, answers, answerVotes, reports. Odredi document ID strategiju (npr. UID, deterministic vote ID, enrollment uniqueness), tipove, statuse, read/write upite i compound indexes. Uskladi Repository interfejse; ne implementiraj UI.
- **Acceptance criteria:** Dokumentovane kolekcije, obavezna polja, identifikatori i server timestamps; upiti i indeksi pobrojani.
- **Plan/deployment note:** Spark

### FB-05 — Seed: demo škole, aktivnosti i 4 uloge

- **Status:** `TODO` | **Area:** Podaci i testiranje | **Priority:** Srednji | **Estimate:** 3 h
- **Firebase dependencies:** `FB-03`, `FB-04` | **External prerequisite:** None
- **Refines Jira tasks:** `QA-03` | **Related workstream:** Testni podaci
- **Implementation brief:** Napravi idempotentnu skriptu/fixture za Auth emulator i Firestore emulator: 2 admina, 5 profesora, 30 studenata, nekoliko srednjoškolaca, 10 aktivnosti, pitanja i odgovore. Seed samo za emulator/dev; bez ugrađivanja kredencijala u Git.
- **Acceptance criteria:** Ponovljivi import lokalnih demo podataka; admin/prof/student/srednjoškolac; bez stvarnih ličnih podataka.
- **Plan/deployment note:** Spark

### FB-06 — AuthRepository: login/logout/reset + session

- **Status:** `TODO` | **Area:** Autentifikacija | **Priority:** Srednji | **Estimate:** 4 h
- **Firebase dependencies:** `FB-02`, `FB-04` | **External prerequisite:** None
- **Refines Jira tasks:** `ID-01`, `ID-02` | **Related workstream:** Identity
- **Implementation brief:** Implementiraj FirebaseAuthRepository preko već definisanog AuthRepository interfejsa. Podrži Email/Password login, logout, reset lozinke i auth-state listener kao Flow/StateFlow. Ne izmišljaj novu UI navigaciju; obuhvati loading, error, successful session. Dodaj unit testove sa fake implementacijom.
- **Acceptance criteria:** Sign-in, sign-out i password reset rade; greške su mapirane; session state posmatran; UI ne zna Firebase detalje.
- **Plan/deployment note:** Spark

### FB-07 — Privilegovane uloge i siguran bootstrap admina

- **Status:** `TODO` | **Area:** Autorizacija | **Priority:** Visok | **Estimate:** 5 h
- **Firebase dependencies:** `FB-03`, `FB-04` | **External prerequisite:** None
- **Refines Jira tasks:** `ID-06`, `ID-07` | **Related workstream:** Identity / sigurnost
- **Implementation brief:** Dizajniraj i implementiraj sigurno kreiranje ADMIN i PROFESSOR naloga: Cloud Functions/Admin SDK ili drugi pouzdani backend, provera pozivaoca i schoolId, onemogućavanje privilege escalation. Početnog admina kreiraj jednokratnim administrativnim postupkom van klijenta. Proveri potrebu za Blaze planom pre deploy-a. Dodaj test odbijanja neovlašćenog poziva.
- **Acceptance criteria:** Ne može sam dodeliti ADMIN/PROFESSOR niti menjati schoolId/status; admin bootstrap dokumentovan; testira se server-side.
- **Plan/deployment note:** Blaze za deploy

### FB-08 — Registracija običnih naloga i profili

- **Status:** `TODO` | **Area:** Autentifikacija | **Priority:** Srednji | **Estimate:** 4 h
- **Firebase dependencies:** `FB-06`, `FB-07` | **External prerequisite:** None
- **Refines Jira tasks:** `ID-02`, `ID-03`, `ID-04` | **Related workstream:** Identity
- **Implementation brief:** Implementiraj registraciju samo dozvoljenog javnog tipa naloga (npr. HIGHSCHOOL_STUDENT) i kreiranje njegovog users/{uid} dokumenta, uz bezbedno podrazumevanu ulogu. Poveži učitavanje profila/role sa postojećom role-based navigacijom. Omogući samo dozvoljene izmene profila, nikad role/status/schoolId.
- **Acceptance criteria:** Običan korisnik kreira dozvoljen nalog; nema samododele privilegija; profil vezan za auth.uid.
- **Plan/deployment note:** Spark

### FB-09 — Pravila za users, schools i programs

- **Status:** `TODO` | **Area:** Security Rules | **Priority:** Visok | **Estimate:** 4 h
- **Firebase dependencies:** `FB-04`, `FB-07` | **External prerequisite:** None
- **Refines Jira tasks:** `ID-07` | **Related workstream:** Rules / identitet
- **Implementation brief:** Napiši Firestore Security Rules v2 za users, schools i programs, uključujući validaciju tipova/polja i school-scoped upravljanje. Admin može potrebne izmene, ali privilegovane operacije idu pouzdanim backendom. Pripremi testove za sve 4 uloge i unauthenticated. Ne koristi allow read, write: if true.
- **Acceptance criteria:** Role/status/schoolId neizmenjivi običnom korisniku; javno čitanje samo potrebnih podataka; deny-by-default.
- **Plan/deployment note:** Spark

### FB-10 — Repozitorijumi škola i smerova

- **Status:** `TODO` | **Area:** Firestore repository | **Priority:** Srednji | **Estimate:** 4 h
- **Firebase dependencies:** `FB-02`, `FB-04` | **External prerequisite:** None
- **Refines Jira tasks:** `ID-05` | **Related workstream:** Katalog
- **Implementation brief:** Implementiraj FirebaseSchoolRepository i FirebaseProgramRepository po postojećim interfejsima. Dodaj listanje/pretragu, detalje i admin CRUD bez zaobilaženja pravila pristupa. Podržane prazne liste i greške. Ne menjaj ekrane drugih feature-a.
- **Acceptance criteria:** SchoolRepository i ProgramRepository rade sa stvarnim Firestore; upiti paginirani koliko je potrebno.
- **Plan/deployment note:** Spark

### FB-11 — Repozitorijum aktivnosti, pretraga i indeksi

- **Status:** `TODO` | **Area:** Firestore repository | **Priority:** Srednji | **Estimate:** 5 h
- **Firebase dependencies:** `FB-02`, `FB-04` | **External prerequisite:** None
- **Refines Jira tasks:** `ACT-01`, `ACT-02`, `ACT-03`, `ACT-04`, `ACT-05` | **Related workstream:** Aktivnosti
- **Implementation brief:** Implementiraj FirebaseActivityRepository sa feed-om, detaljem, filtriranjem po školi/datumu/statusu i CRUD metodama kroz već postojeći ugovor. Napravi indekse u firestore.indexes.json za stvarne upite. Ne oslanjaj se na Security Rules kao filter; ne dodaj novi UI.
- **Acceptance criteria:** Feed, detalj, filter po školi/datumu i osnovni CRUD kroz repository; server-side filtriranje indeksi.
- **Plan/deployment note:** Spark

### FB-12 — Pravila aktivnosti po školi

- **Status:** `TODO` | **Area:** Security Rules | **Priority:** Visok | **Estimate:** 4 h
- **Firebase dependencies:** `FB-04`, `FB-09` | **External prerequisite:** None
- **Refines Jira tasks:** `ACT-04`, `ACT-05` | **Related workstream:** Rules / aktivnosti
- **Implementation brief:** Proširi firestore.rules za activities: ADMIN upravlja svim aktivnostima; PROFESSOR samo svojom školom; STUDENT i HIGHSCHOOL_STUDENT imaju samo dozvoljena čitanja. Validiraj schoolId, authorId, capacity, datume, troškove, zaštićena polja i status. Dodaj negative testove.
- **Acceptance criteria:** Admin menja sve, profesor samo svoje škole; student/srednjoškolac ne kreiraju aktivnosti; protected fields validirana.
- **Plan/deployment note:** Spark

### FB-13 — Prijave: kapacitet i duplikati (atomski)

- **Status:** `TODO` | **Area:** Server-side logika | **Priority:** Visok | **Estimate:** 6 h
- **Firebase dependencies:** `FB-03`, `FB-04`, `FB-07` | **External prerequisite:** None
- **Refines Jira tasks:** `ACT-06`, `ACT-07`, `ACT-08` | **Related workstream:** Upisi na aktivnosti
- **Implementation brief:** Implementiraj server-side callable functions za prijavu, otkazivanje i potvrdu učešća uz atomsku transakciju. Proveri STUDENT ulogu, status aktivnosti, slobodan kapacitet, školski scope za professors/admin. Koristi determinističke ključeve ili indeks uniqueness i testiraj konkurentne pozive. Za funkcije na cloud-u zahtevati odluku o Blaze/budžetu.
- **Acceptance criteria:** Nema duplih aktivnih prijava; kapacitet ne može preći limit ni pri istovremenim zahtevima; autorizacija stvarna.
- **Plan/deployment note:** Blaze za deploy

### FB-14 — Android povezivanje prijava na aktivnosti

- **Status:** `TODO` | **Area:** Firestore repository | **Priority:** Srednji | **Estimate:** 4 h
- **Firebase dependencies:** `FB-11`, `FB-13` | **External prerequisite:** None
- **Refines Jira tasks:** `ACT-06`, `ACT-07`, `ACT-08` | **Related workstream:** Upisi na aktivnosti
- **Implementation brief:** Napravi FirebaseEnrollmentRepository koji poziva postojeće callable functions za enrollment operacije i čita korisnikove prijave. Poveži sa postojećim ViewModelima; ne dupliraj logiku kapaciteta u UI-ju. Obuhvati loading, greške i idempotentnost.
- **Acceptance criteria:** Prijava/otkazivanje u Androidu prolazi preko sigurnog API-ja; nema direktnog nedozvoljenog upisa.
- **Plan/deployment note:** Spark / funkcije Blaze

### FB-15 — Forum: pitanja i odgovori

- **Status:** `TODO` | **Area:** Firestore repository | **Priority:** Srednji | **Estimate:** 5 h
- **Firebase dependencies:** `FB-02`, `FB-04` | **External prerequisite:** None
- **Refines Jira tasks:** `FOR-01`, `FOR-02`, `FOR-03`, `FOR-04` | **Related workstream:** Forum
- **Implementation brief:** Implementiraj FirebaseQuestionRepository i FirebaseAnswerRepository po ugovorima. Srednjoškolac kreira pitanje; profesori/studenti odgovaraju; svi mogu pregledati dozvoljen sadržaj. Podrži paginaciju/limit, server timestamp, updatedAt i osnovno sortiranje. Ne menjaj ekran bez potrebe.
- **Acceptance criteria:** Lista pitanja, detalj, kreiranje pitanja i odgovora rade; sortiranje i limitirani upiti.
- **Plan/deployment note:** Spark

### FB-16 — Forum prava: questions i answers

- **Status:** `TODO` | **Area:** Security Rules | **Priority:** Visok | **Estimate:** 4 h
- **Firebase dependencies:** `FB-04`, `FB-09` | **External prerequisite:** None
- **Refines Jira tasks:** `FOR-02`, `FOR-04`, `FOR-06` | **Related workstream:** Rules / forum
- **Implementation brief:** Napiši i testiraj Security Rules za questions i answers. Ograniči create prema ulozi, update prema autoru, statusu i dozvoljenim poljima, acceptedAnswerId može menjati samo autor pitanja i mora biti validan odgovor za to pitanje (ili proveru poveriti backendu). Admin moderacija nema privilege escalation.
- **Acceptance criteria:** Samo srednjoškolac otvara pitanje; student/prof odgovaraju; samo vlasnik menja vlastiti sadržaj; admin moderira.
- **Plan/deployment note:** Spark

### FB-17 — Glasovi, prijave sadržaja i prihvaćen odgovor

- **Status:** `TODO` | **Area:** Firestore repository | **Priority:** Srednji | **Estimate:** 5 h
- **Firebase dependencies:** `FB-15`, `FB-16` | **External prerequisite:** None
- **Refines Jira tasks:** `FOR-05`, `FOR-06`, `FOR-07` | **Related workstream:** Forum
- **Implementation brief:** Implementiraj vote/reports repozitorijume i prihvatanje odgovora uz deterministic vote IDs i validaciju. Ne dozvoli više glasova istog korisnika na isti odgovor ili izmenu tuđih izveštaja. Dodaj Rules testove i UI integration metodu kroz postojeće ViewModele.
- **Acceptance criteria:** Jedan glas po korisniku/odgovoru; prijava sadržaja vezana za reportera; accepted answer samo autora pitanja.
- **Plan/deployment note:** Spark

### FB-18 — Moderacija, admin read modeli i statistika

- **Status:** `TODO` | **Area:** Admin i izveštaji | **Priority:** Srednji | **Estimate:** 5 h
- **Firebase dependencies:** `FB-07`, `FB-12`, `FB-16` | **External prerequisite:** None
- **Refines Jira tasks:** `FOR-08`, `ACT-09` | **Related workstream:** Admin / izveštaji
- **Implementation brief:** Poveži Firestore read modele za prijave neprimerenog sadržaja, moderatorske odluke i preglede aktivnosti/troškova. Obavezno proveri authorization prema role/schoolId u pravilima ili trusted backendu. Ako se koristе agregati, ne veruj brojačima koje klijent može proizvoljno menjati.
- **Acceptance criteria:** Admin vidi prijave i moderira; profesor vidi statistiku samo svoje škole; osetljivi agregati nisu klijentski izmenjivi.
- **Plan/deployment note:** Blaze ako funkcije

### FB-19 — Automatski testovi Security Rules za 4 uloge

- **Status:** `TODO` | **Area:** Testiranje | **Priority:** Srednji | **Estimate:** 6 h
- **Firebase dependencies:** `FB-09`, `FB-12`, `FB-13`, `FB-16`, `FB-17`, `FB-18` | **External prerequisite:** None
- **Refines Jira tasks:** `QA-01` | **Related workstream:** QA / pravila
- **Implementation brief:** Napraviti kompletne emulator Security Rules unit testove za ADMIN, PROFESSOR, STUDENT, HIGHSCHOOL_STUDENT i signed-out. Pokrij promenu role/schoolId, aktivnosti tuđe škole, enrollment, pitanje/odgovor, glasanje, prijavljivanje i moderaciju. Testovi moraju padati na zabranjene pokušaje; CI komanda dokumentovana.
- **Acceptance criteria:** Test matrica: dozvoljeni i zabranjeni čitanje/upis, promene uloga, cross-school edit, autorstvo i duplikati.
- **Plan/deployment note:** Spark / emulator

### FB-20 — End-to-end smoke test Android + Firebase

- **Status:** `TODO` | **Area:** Testiranje | **Priority:** Srednji | **Estimate:** 4 h
- **Firebase dependencies:** `FB-05`, `FB-08`, `FB-10`, `FB-11`, `FB-14`, `FB-17` | **External prerequisite:** None
- **Refines Jira tasks:** `QA-02`, `QA-05` | **Related workstream:** QA / mobilni tokovi
- **Implementation brief:** Testiraj Android Studio build i ručni/automatizovani smoke test protiv Firebase emulatora sa 4 uloge: prijava, profil, feed, kreiranje aktivnosti, studentska prijava, pitanje, odgovor, moderacija. Zabeleži logove, regresije i ograničenja; bez produkcionih test podataka.
- **Acceptance criteria:** Četiri testna naloga; svi ključni tokovi rade na emulatoru; error/empty/network stanja proverena.
- **Plan/deployment note:** Spark / emulator

### FB-21 — Deploy pravila, indeksa, funkcija i dokumentacija

- **Status:** `TODO` | **Area:** Release | **Priority:** Srednji | **Estimate:** 4 h
- **Firebase dependencies:** `FB-19`, `FB-20` | **External prerequisite:** None
- **Refines Jira tasks:** `CORE-03`, `QA-04` | **Related workstream:** Release / bezbednost
- **Implementation brief:** Pripremi bezbedno izdavanje na Firebase dev: provera project alias, deploy samo relevantnih rules/indexes/functions, lista potrebnih indexa, ograničeni pristupi, monitoring/budžetska upozorenja i dopuna README. Nikada ne objavljuj insecure rules ni secrets. Ako se Cloud Functions ne deploy-uju bez Blaze, blokadu dokumentuj jasno.
- **Acceptance criteria:** Dev deploy reproduktibilan; rules/indexes verzionisani; plan/budžet objašnjen; README uključuje setup.
- **Plan/deployment note:** Blaze za funkcije

## Draft Firestore collection model (confirm in FB-04)

This is a planning reference from the tracker, **not an implemented schema or proof of authorization**. Final field types, indexes, validators, and rules are decided in `CORE-04`/`FB-04` and tested by the relevant security tasks.

| Collection | Document ID | Main fields | Access/integrity notes |
| --- | --- | --- | --- |
| `users` | `{uid}` | displayName, email, role, status, schoolId?, programId?, createdAt | Read: Sopstveni profil; admin i ograničena javna polja. Write: Korisnik menja samo dozvoljena polja; role/status/schoolId samo trusted backend. UID iz Firebase Auth; bez lozinki u Firestore |
| `schools` | `{schoolId}` | name, city, description, website | Read: Sve uloge. Write: Samo admin, uz eventualna usko delegirana prava. Profesor vezan za jednu schoolId |
| `programs` | `{programId}` | schoolId, title, description | Read: Sve uloge. Write: Samo admin / ovlašćeni profesor sopstvene škole po odluci tima. schoolId validan i neizmenjiv |
| `activities` | `{activityId}` | schoolId, authorId, title, description, startsAt, endsAt, capacity, cost, status | Read: Sve prijavljene uloge. Write: Admin sve; profesor samo sopstvene škole. startsAt < endsAt; capacity > 0; cost >= 0; indexi za filtere |
| `enrollments` | `{activityId_uid}` | activityId, studentId, status, reason?, completionNote?, createdAt | Read: Student svoje; profesor svoje škole; admin sve. Write: Atomski trusted endpoint + bez direktnog nebezbednog upisa. Bez duplih aktivnih prijava i overbooking-a |
| `questions` | `{questionId}` | authorId, schoolId?, title, body, tags, status, acceptedAnswerId? | Read: Sve uloge. Write: Srednjoškolac postavlja; autor dozvoljene izmene; admin moderira. acceptedAnswerId mora pripadati pitanju; moderacija statusa |
| `answers` | `{answerId}` | questionId, authorId, body, createdAt | Read: Sve uloge. Write: Student/profesor pišu; autor dozvoljene izmene; admin moderira. Bez cross-question acceptedAnswerId |
| `answerVotes` | `{answerId_uid}` | answerId, userId, value, createdAt | Read: Po potrebi agregati / sopstveni glas. Write: Prijavljeni korisnik samo sopstveni glas. Deterministički ID sprečava dupli glas |
| `reports` | `{reportId}` | reporterId, targetType, targetId, reason, status | Read: Admin; prijavljivač može videti sopstvenu prijavu. Write: Prijavljeni korisnik kreira; samo admin menja status. Ne otkrivati identitet prijavljivača ostalim korisnicima |

## Definition of Done

A task can be set to `DONE` only when the scoped implementation and acceptance criteria have been reviewed; the relevant build/tests/lint run; UI or data-layer changes have a clear manual verification path; security-critical operations have server-side checks and negative tests; and the change is committed/merged according to `WORKFLOW.md`.

Do not imply that placeholder screens are connected to authentication, or that Firebase Security Rules are effective merely because Android hides an action. Report blockers instead of weakening authorization.
