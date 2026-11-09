# A šta sad? — DESIGN SYSTEM (MVP v1)

> **Status:** predloženi dizajn ugovor za tim. Odobrite boje i raspored u Figmi **pre** masovne implementacije ekrana. Posle odobravanja, promene idu kroz poseban PR. Izvor istine za kod su `ui/theme/` i `ui/components/`; ovaj dokument opisuje ugovor.

## 1. Vizuelni pravac

- Stil: prijateljski, moderan, čist **Material Design 3**, sa Reddit inspiracijom kroz feed kartice i forum, a ne kopiranjem Reddit UI-ja.
- Platforma: Android; **Kotlin + Jetpack Compose**. Nema mešanja XML layouta i Compose-a u MVP-u.
- Samo **light tema za MVP**; za sve ekrane isti `AstaTheme`. Isključiti dinamičke boje zasnovane na korisnikovom wallpaper-u (`dynamicColor = false`) kako bi brending bio isti na uređajima. Dark tema je zaseban budući task.
- Vizuelni prioritet: čitljivost, jednostavnost, jasne akcije, ne dekoracija.

## 2. Boje (semantički tokeni)

Unutar feature Composable-a **NE** pisati `Color(0xFF...)` ili ručno HEX vrednosti. Referenciraj `MaterialTheme.colorScheme.<token>` ili `AstaColors.<token>` za namenski brend akcenat. Svi HEX kodovi žive centralno u `ui/theme/Color.kt`.

| Uloga | HEX | Gde se koristi |
| --- | --- | --- |
| `primary` | `#5B3CC4` | glavno dugme, aktivni navigacioni element |
| `onPrimary` | `#FFFFFF` | tekst/ikona preko primary |
| `primaryContainer` | `#E8DEFF` | istaknuta neprimarna kartica, chip |
| `onPrimaryContainer` | `#24135F` | tekst na primaryContainer |
| `secondary` | `#56617A` | manje naglašeni elementi |
| `onSecondary` | `#FFFFFF` | tekst na secondary |
| `secondaryContainer` | `#DFE5F6` | sekundarni kontejneri |
| `onSecondaryContainer` | `#152034` | tekst na sekundarnim kontejnerima |
| `tertiary` | `#775719` | npr. dodatni akcenat unutar Material boja |
| `onTertiary` | `#FFFFFF` | tekst preko tertiary |
| `tertiaryContainer` | `#FFDEA2` | light gold pozadina za isticanje |
| `onTertiaryContainer` | `#271900` | tekst na gold kontejneru |
| `background` | `#F9F8FD` | ekran u pozadini |
| `onBackground` | `#1B1B21` | primarni tekst na pozadini |
| `surface` | `#FFFFFF` | kartice i forma |
| `onSurface` | `#1B1B21` | primarni tekst na karticama |
| `surfaceVariant` | `#ECEAF3` | neutralni alternativni panel |
| `onSurfaceVariant` | `#444650` | sekundarni tekst |
| `outline` | `#777681` | ivice, divider kada je potreban |
| `error` | `#B3261E` | validacija, greške |
| `onError` | `#FFFFFF` | tekst na error |
| `brandAccent` | `#E9B949` | mala značka Novo / istaknuta oznaka, **sa tamnim tekstom** |

Za disabled stanje koristi standardnu Material 3 semantiku, ne novu ručno izabranu sivu. Ne koristiti žutu za sitan tekst na beloj pozadini.

## 3. Tipografija

- Font: **Roboto / podrazumevani Android sans-serif**. Bez posebnih fontova u prvoj verziji.
- Naslov stranice: `MaterialTheme.typography.headlineSmall`.
- Naslov kartice/pitanja: `titleMedium`.
- Običan sadržaj: `bodyMedium`.
- Pomoćni opis: `bodySmall`.
- Dugmad: `labelLarge`.
- Maksimalno 2 nivoa naslova po kartici; ne improvizovati veličine `sp` za pojedinačne feature-e.
- Izbegavati all-caps u UI tekstu i preduge tekstove na dugmadima.

## 4. Spacing, dimenzije i oblici

- Dozvoljena spacing skala: `4, 8, 12, 16, 24, 32.dp`.
- Standardni horizontalni padding ekrana: `16.dp`.
- Razmak između većih sekcija: `24.dp`.
- Kartice: sadržaj `16.dp`, razmak između kartica `12.dp`.
- Dugmad i inputi: prate Material 3 podrazumevane visine i minimalnu interaktivnu površinu 48×48 dp; ne smanjivati klikabilni deo za ikonice.
- Zaobljenja: kartica `16.dp`, dugme `12.dp`, input `12.dp`, badge/chip prema Material 3 `AssistChip` / `FilterChip`.
- Senke: minimalne; preferirati `Card` sa tonalnim fill-om ili tankim outline-om.
- Ne crtati ručno različite `RoundedCornerShape` vrednosti po feature-u.

**Implementacija:** jedno mesto za `Color.kt`, `Theme.kt`, `Type.kt`, `Shape.kt`, `Dimens.kt` (ili dogovorena struktura), bez dupliranih tokens fajlova.

## 5. Zajedničke UI komponente

U `ui/components/` postaviti minimalan skup komponenti **pre paralelnog rada po feature-ima**:

| Komponenta | Uloga |
| --- | --- |
| `AstaAppScaffold` | jedinstveni sistem za top bar, bottom navigation i padding |
| `AstaTopBar` | naslov, back dugme i opcione akcije |
| `AstaPrimaryButton` | primarno dugme (iz M3 `Button`) |
| `AstaTextField` | standardno polje sa greškom i pomoćnim tekstom |
| `AstaActivityCard` | kartica aktivnosti u feed-u |
| `AstaQuestionCard` | kartica forum pitanja |
| `AstaRoleBadge` | mala oznaka škole/uloge, isti stil svuda |
| `AstaLoadingState` / `AstaEmptyState` / `AstaErrorState` | standardna stanja ekrana |

Ne praviti posebne custom komponente ako standardni M3 `Button`, `Card`, `OutlinedTextField`, `FilterChip` ili `NavigationBar` već rade posao. Wrapper koristiti kad postoji konkretno deljeno pravilo ili komponenta na više mesta.

## 6. Navigacija i struktura ekrana

Glavne funkcionalnosti posle prijave:

- `Aktivnosti` (feed i detalji)
- `Pitanja` (forum i odgovori)
- `Profil`

Opcije kao `Upravljaj aktivnostima`, `Administracija`, `Korisnici` ili `Statistika` prikazuju se zavisno od uloge, ali koriste **isti izgled aplikacije**, a ne potpuno zasebne šeme boja i navigacije. Admin i profesor mogu imati dodatni tab ili meni ako je korisno. Svaka privilegovana akcija mora se proveriti i na backendu (izvan UI-ja).

Ekrani poštuju istu hijerarhiju: `Scaffold` → (top bar / bottom bar) → content sa 16dp stranica → kartice / liste. Ne ubacivati ručno `Scaffold` i bottom nav u svaku pojedinačnu feature rutu ako postoji zajednički shell.

## 7. Smernice za pojedinačne feature-e

- **Aktivnosti:** ista `AstaActivityCard` u glavnom feed-u, korisnikovim prijavama i pregledima škole. Filtriranje preko M3 `FilterChip`; jasno istaknuti datum, mesto, kapacitet i status.
- **Forum:** isti `AstaQuestionCard` za listu i pretragu; odgovori na zasebnom detaljnom ekranu; jasno označiti prihvaćen odgovor, ne mešati ga sa statusom aktivnosti.
- **Autentifikacija:** jedan stil za login, registraciju i reset lozinke; ista polja i dugmad.
- **Admin/Profesor:** isti osnovni `Scaffold`; forme kreiranja/uređivanja koriste iste inpute i validacione poruke.
- **Profil:** ista komponenta detalja profila za sve role, uz ulogom uslovljene akcije.

## 8. Figma ugovor

Pre implementacije feature UI-ja ručno napraviti jednu malu Figma biblioteku:

1. Stranicu **Foundations** sa bojama, razmacima, tipografijom i zaobljenjima.
2. Stranicu **Components** sa dugmadima, poljima, chip-ovima, karticama, top barom i bottom nav.
3. Stranicu **Screens** sa 5 traženih skica (login, admin, profesor, student, dodavanje korisnika), plus Activities i Q&A ako imate vremena.

Zamrznuti izgled posle dogovora i popravke raditi preko dogovorenog PR-a; nemojte nasumično menjati Figma i Kotlin pojedinačno. AI može da pomogne u razumevanju koda, ali prema instrukcijama profesorke **AI-generisane slike nisu dozvoljene za zvanične dizajnerske predloge**.

## 9. Provera svakog UI taska

- [ ] Koristi `AstaTheme`, centralne boje, tipografiju, oblike i spacing.
- [ ] Koristi postojeće shared komponente gde je moguće.
- [ ] Prikazuje Loading, Empty, Error i stvarno popunjeno stanje.
- [ ] Ima `@Preview` sa fake/sample podacima bez mreže.
- [ ] Radi na emulatoru, bez preklapanja UI-ja i prekinutog teksta.
- [ ] Tekst je čitljiv, kontrast pregledan, touch targeti dovoljno veliki.
- [ ] Priložen screenshot i upoređen sa Figmom / postojećim ekranima.
- [ ] Novi token ili izmena shared UI-ja su zaseban dogovoren PR.
