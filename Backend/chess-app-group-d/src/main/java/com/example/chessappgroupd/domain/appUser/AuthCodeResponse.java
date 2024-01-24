package com.example.chessappgroupd.domain.appUser;
//AuthCodeResponse
//represents the return value of login(), (id -> AuthCode)
public record AuthCodeResponse(Long authCodeId) {
    //Records sind seit Java 14 eine kompakte Möglichkeit, unveränderliche Datenklassen zu definieren.
    //Records sind public, final und enthalten automatisch einen public, no-arg-Konstruktor,
    //der alle finalen Felder setzt. Records enthalten auch lesende Zugriffsmethoden für alle
    // Felder sowie equals()-, hashCode()- und toString()-Methoden.
}
