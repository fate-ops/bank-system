# UC-03 – Auszahlung tätigen

| Feld | Beschreibung |
|---|---|
| **Name** | Auszahlung tätigen |
| **Kurzbeschreibung** | Ein Kunde lässt sich am Bankschalter einen Geldbetrag von seinem Konto auszahlen. |
| **Akteure** | Kunde, Bankmitarbeiter |
| **Auslöser** | Der Kunde verlangt die Auszahlung eines bestimmten Geldbetrags. |
| **Ergebnisse** | Der Betrag wurde vom Konto abgebucht und dem Kunden ausbezahlt. Die Auszahlung wurde protokolliert. |
| **Vorbedingung** | Das Konto existiert und ist nicht gesperrt. Der Kunde ist verfügungsberechtigt, weist sich aus und verfügt über genügend Guthaben beziehungsweise einen erlaubten Kreditrahmen. |
| **Nachbedingung** | Der Kontostand ist um den ausbezahlten Betrag reduziert. Die Buchung ist im Kontoauszug sichtbar. |
| **Eingehende Daten** | Identitätsnachweis, Kontonummer oder IBAN und gewünschter Auszahlungsbetrag. |
| **Essenzieller Ablauf** | 1. Der Kunde nennt das Konto und den gewünschten Betrag.<br>2. Der Bankmitarbeiter prüft die Identität und Berechtigung des Kunden.<br>3. Das System prüft Kontostatus, Guthaben und Limiten.<br>4. Der Bankmitarbeiter bestätigt die Auszahlung.<br>5. Das System bucht den Betrag ab und aktualisiert den Kontostand.<br>6. Der Bankmitarbeiter übergibt das Bargeld.<br>7. Der Kunde erhält eine Auszahlungsbestätigung. |
| **Offene Punkte** | Welche Auszahlungslimiten gelten? Müssen grössere Beträge im Voraus bestellt werden? |
| **Änderungshistorie** | Version 1.0 – Ersterstellung, 21.09.2026 |
| **Sonstiges / Anmerkungen** | Bei ungenügendem Guthaben, fehlender Berechtigung oder einem gesperrten Konto wird die Auszahlung abgelehnt. |

