# UC-04 – Kontoauszug erhalten

| Feld | Beschreibung |
|---|---|
| **Name** | Kontoauszug erhalten |
| **Kurzbeschreibung** | Ein Kunde fordert am Bankschalter einen Kontoauszug für ein bestehendes Konto an. |
| **Akteure** | Kunde, Bankmitarbeiter |
| **Auslöser** | Der Kunde möchte seinen Kontostand und seine Kontobewegungen einsehen. |
| **Ergebnisse** | Ein Kontoauszug wurde für den gewünschten Zeitraum erstellt und dem Kunden ausgehändigt. |
| **Vorbedingung** | Das Konto existiert. Der Kunde ist verfügungsberechtigt und kann seine Identität nachweisen. Der Bankmitarbeiter ist am Banksystem angemeldet. |
| **Nachbedingung** | Der Kontoauszug wurde ausgegeben. Am Konto und an den Buchungen wurden keine Änderungen vorgenommen. |
| **Eingehende Daten** | Identitätsnachweis, Kontonummer oder IBAN, gewünschter Zeitraum und gewünschte Ausgabeform. |
| **Essenzieller Ablauf** | 1. Der Kunde fordert einen Kontoauszug an.<br>2. Der Bankmitarbeiter prüft Identität und Berechtigung.<br>3. Der Kunde nennt den gewünschten Zeitraum.<br>4. Das System ruft Kontostand und Buchungen ab.<br>5. Das System erstellt den Kontoauszug.<br>6. Der Bankmitarbeiter druckt den Auszug aus oder stellt ihn digital bereit.<br>7. Der Kunde erhält den Kontoauszug. |
| **Offene Punkte** | Wie weit darf der Zeitraum zurückreichen? Ist ein gedruckter Kontoauszug kostenpflichtig? |
| **Änderungshistorie** | Version 1.0 – Ersterstellung, 21.09.2026 |
| **Sonstiges / Anmerkungen** | Der Kontoauszug enthält vertrauliche Personendaten und darf nur berechtigten Personen ausgehändigt werden. |
