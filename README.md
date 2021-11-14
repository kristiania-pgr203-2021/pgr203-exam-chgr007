[![Byggejobb](https://github.com/kristiania-pgr203-2021/pgr203-exam-chgr007/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/kristiania-pgr203-2021/pgr203-exam-chgr007/actions/workflows/maven.yml)

# PGR203 Avansert Java eksamen

## Linker
* https://github.com/kristiania-pgr203-2021/pgr203-exam-chgr007 Github Repo
* https://github.com/kristiania-pgr203-2021/pgr203-exam-chgr007/actions Github Actions 



## Funksjonalitet utenfor beskrivelse
* Serveren benytter JWT (JSON Web Tokens) for å autorisere bruker
* Serveren benytter Spring Security og Bcryp for å autentisere bruker
* Serializing til/fra server med JSON, Jackson-biblioteket er benyttet backend
  * Vi har ikke fått UTF-8 til å fungere med Jackson og JSON. UTF-8 fungerer imidlertid der hvor vi ikke har benyttet JSON, for eksempel når man oppretter questionnaire.
* Fancy random css farger :D
* Man kan endre tittel på spørreundersøkelse og spørsmål ved bruk av edit-ikonet ved siden av tittelen
* Answer vil bruke info fra opprettelsen av spørreundersøkelsen for å gi riktig svarmulighet


## Kilder:
* HttpServer https://github.com/kristiania-pgr203-2021/pgr203-innlevering-2-mariestigen Innlevering 2 av Marie og Christian 
* JDBC https://github.com/kristiania-pgr203-2021/pgr203-innlevering-3-mariestigen Innlevering 3 av Marie og Christian
* Decoding av Token i JavaScript: https://stackoverflow.com/a/38552302 av bruker Peheje
* Thread pool: http://tutorials.jenkov.com/java-multithreaded-servers/thread-pooled-server.html av Jakob Jenkov


## Beskriv hvordan programmet skal testes:
* Bygg programmet med `mvn package`
* Kjør den eksekverbare .jar filen med kommandoen `java -jar <sti til jar> [--port]`
  * Port argumentet er valgfritt. Dersom det unnlates vil port 8080 bli benyttet.
* Registrer en bruker
  * Det presiseres at dette er en HTTP-server, ikke https. Alt vil bli sendt i klartekst mellom klient og tjener.
    Vi har heller ikke bruk veldig mye tid på å forske på Spring Security, og må ta forbehold om feil i oppsett. Dette er ment som demo/POC.
    Derfor fraråder vi på det sterkeste å benytte reelle passord ved registrering av ny bruker.
    Nøkkelen vi har benyttet til signering av token ville heller aldri blitt opplastet på github om programmet skulle i produksjon.
* Opprett questionnaire
* Opprett spørsmål og type
* Legg inn svar
* For å logge ut, må token slettes (F12 -> Application -> Token)
* Deretter kan man opprette ny bruker for å teste at questionnaire og svar blir lagret i databasen på
  den innloggede brukeren. For å verifisere må man sjekke databasen og userId.


## Korreksjoner av eksamensteksten i Wiseflow:

**DET ER EN FEIL I EKSEMPELKODEN I WISEFLOW:**

* I `addOptions.html` skulle url til `/api/tasks` vært `/api/alternativeAnswers` (eller noe sånt)

**Det er viktig å være klar over at eksempel HTML i eksamensoppgaven kun er til illustrasjon. Eksemplene er ikke tilstrekkelig for å løse alle ekstraoppgavene og kandidatene må endre HTML-en for å være tilpasset sin besvarelse**

 

## Sjekkliste

## Vedlegg: Sjekkliste for innlevering

* [x] Dere har lest eksamensteksten
* [ ] Dere har lastet opp en ZIP-fil med navn basert på navnet på deres Github repository
* [x] Koden er sjekket inn på github.com/pgr203-2021-repository
* [x] Dere har committed kode med begge prosjektdeltagernes GitHub konto (alternativt: README beskriver arbeidsform)

### README.md

* [ ] `README.md` inneholder en korrekt link til Github Actions
* [ ] `README.md` beskriver prosjektets funksjonalitet, hvordan man bygger det og hvordan man kjører det
* [ ] `README.md` beskriver eventuell ekstra leveranse utover minimum
* [ ] `README.md` inneholder et diagram som viser datamodellen

### Koden

* [x] `mvn package` bygger en executable jar-fil
* [ ] Koden inneholder et godt sett med tester
* [x] `java -jar target/...jar` (etter `mvn package`) lar bruker legge til og liste ut data fra databasen via webgrensesnitt
* [x] Serveren leser HTML-filer fra JAR-filen slik at den ikke er avhengig av å kjøre i samme directory som kildekoden
* [x] Programmet leser `dataSource.url`, `dataSource.username` og `dataSource.password` fra `pgr203.properties` for å connecte til databasen
* [x] Programmet bruker Flywaydb for å sette opp databaseskjema
* [x] Server skriver nyttige loggmeldinger, inkludert informasjon om hvilken URL den kjører på ved oppstart

### Funksjonalitet

* [x] Programmet kan opprette spørsmål og lagrer disse i databasen (påkrevd for bestått)
* [x] Programmet kan vise spørsmål (påkrevd for D)
* [x] Programmet kan legge til alternativ for spørsmål (påkrevd for D)
* [x] Programmet kan registrere svar på spørsmål (påkrevd for C)
* [x] Programmet kan endre tittel og tekst på et spørsmål (påkrevd for B)

### Ekstraspørsmål (dere må løse mange/noen av disse for å oppnå A/B)

* [x] Før en bruker svarer på et spørsmål hadde det vært fint å la brukeren registrere navnet sitt. Klarer dere å implementere dette med cookies? Lag en form med en POST request der serveren sender tilbake Set-Cookie headeren. Browseren vil sende en Cookie header tilbake i alle requester. Bruk denne til å legge inn navnet på brukerens svar
* [x] Når brukeren utfører en POST hadde det vært fint å sende brukeren tilbake til dit de var før. Det krever at man svarer med response code 303 See other og headeren Location
* [x] Når brukeren skriver inn en tekst på norsk må man passe på å få encoding riktig. Klarer dere å lage en <form> med action=POST og encoding=UTF-8 som fungerer med norske tegn? Klarer dere å få æøå til å fungere i tester som gjør både POST og GET?
* [x] Å opprette og liste spørsmål hadde vært logisk og REST-fult å gjøre med GET /api/questions og POST /api/questions. Klarer dere å endre måten dere hånderer controllers på slik at en GET og en POST request kan ha samme request target?
* [x] Vi har sett på hvordan å bruke AbstractDao for å få felles kode for retrieve og list. Kan dere bruke felles kode i AbstractDao for å unngå duplisering av inserts og updates?
* [x] Dersom noe alvorlig galt skjer vil serveren krasje. Serveren burde i stedet logge dette og returnere en status code 500 til brukeren
* [x] Dersom brukeren går til http://localhost:8080 får man 404. Serveren burde i stedet returnere innholdet av index.html
* [x] Et favorittikon er et lite ikon som nettleseren viser i tab-vinduer for en webapplikasjon. Kan dere lage et favorittikon for deres server? Tips: ikonet er en binærfil og ikke en tekst og det går derfor ikke an å laste den inn i en StringBuilder
* [x] I forelesningen har vi sett på å innføre begrepet Controllers for å organisere logikken i serveren. Unntaket fra det som håndteres med controllers er håndtering av filer på disk. Kan dere skrive om HttpServer til å bruke en FileController for å lese filer fra disk?
* [x] Kan dere lage noen diagrammer som illustrerer hvordan programmet deres virker?
* [x] JDBC koden fra forelesningen har en feil ved retrieve dersom id ikke finnes. Kan dere rette denne?
* [x] I forelesningen fikk vi en rar feil med CSS når vi hadde `<!DOCTYPE html>`. Grunnen til det er feil content-type. Klarer dere å fikse det slik at det fungerer å ha `<!DOCTYPE html>` på starten av alle HTML-filer?
* [ ] Klarer dere å lage en Coverage-rapport med GitHub Actions med Coveralls? (Advarsel: Foreleser har nylig opplevd feil med Coveralls så det er ikke sikkert dere får det til å virke)
* [x] FARLIG: I løpet av kurset har HttpServer og tester fått funksjonalitet som ikke lenger er nødvendig. Klarer dere å fjerne alt som er overflødig nå uten å også fjerne kode som fortsatt har verdi? (Advarsel: Denne kan trekke ned dersom dere gjør det feil!)

 ## Bilder
 ![image](https://user-images.githubusercontent.com/23049454/141648969-f032951a-9794-4dfa-bd9c-d3d5034e531b.png)
 Kort overview av programmet
 
 
 
 ![UML-diagram](https://user-images.githubusercontent.com/23049454/141648351-6c689f0a-e65e-4c9f-ae68-69ccc6f89446.png)
 UML-diagram over ferdig kode. Plugin'en (UML Generator) var helt håpløs å jobbe med, men jeg tror vi fikk satt det opp så og si riktig.
 
 
 
 ![answer](https://user-images.githubusercontent.com/23049454/141648464-5cdbd69c-b9f7-49f6-9e8e-736b46badcaa.png)
 ER-diagram over databasen.
