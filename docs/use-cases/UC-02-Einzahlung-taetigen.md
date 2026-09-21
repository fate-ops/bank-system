# UC-02 – Einzahlung tätigen

| Feld | Beschreibung |
|---|---|
| **Name** | Einzahlung tätigen |
| **Kurzbeschreibung** | Ein Kunde zahlt am Bankschalter einen Geldbetrag auf ein bestehendes Konto ein. |
| **Akteure** | Kunde, Bankmitarbeiter |
| **Auslöser** | Der Kunde möchte Bargeld auf ein Konto einzahlen. |
| **Ergebnisse** | Der Betrag wurde dem Konto gutgeschrieben und die Einzahlung wurde protokolliert. Der Kunde erhält eine Bestätigung. |
| **Vorbedingung** | Das Zielkonto existiert. Der Bankmitarbeiter ist am Banksystem angemeldet und der Kunde übergibt den einzuzahlenden Betrag. |
| **Nachbedingung** | Der Kontostand ist um den eingezahlten Betrag erhöht. Die Buchung ist im Kontoauszug sichtbar. |
| **Eingehende Daten** | Kontonummer oder IBAN, Einzahlungsbetrag, Bargeld und gegebenenfalls Kundendaten. |
| **Essenzieller Ablauf** | 1. Der Kunde nennt das Zielkonto und den Betrag.<br>2. Der Bankmitarbeiter sucht das Konto im System.<br>3. Das System bestätigt, dass das Konto existiert.<br>4. Der Bankmitarbeiter nimmt das Bargeld entgegen und prüft den Betrag.<br>5. Die Einzahlung wird im System verbucht.<br>6. Das System aktualisiert den Kontostand.<br>7. Der Kunde erhält eine Einzahlungsbestätigung. |
| **Offene Punkte** | Gibt es Einzahlungslimiten oder Gebühren? Muss sich der Einzahler immer identifizieren? |
| **Änderungshistorie** | Version 1.0 – Ersterstellung, 21.09.2026 |
| **Sonstiges / Anmerkungen** | Bei einem falschen oder gesperrten Konto wird die Einzahlung nicht durchgeführt. |

