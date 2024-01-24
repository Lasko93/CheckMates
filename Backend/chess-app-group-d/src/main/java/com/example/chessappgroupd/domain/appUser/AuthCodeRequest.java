package com.example.chessappgroupd.domain.appUser;
//AuthCodeRequest
//represents a DTO of AuthCode
public record AuthCodeRequest(Long id, String authCode) {
    //Records sind seit Java 14 eine kompakte Möglichkeit, unveränderliche Datenklassen zu definieren.
    //Records sind public, final und enthalten automatisch einen public, no-arg-Konstruktor,
    //der alle finalen Felder setzt. Records enthalten auch lesende Zugriffsmethoden für alle
    // Felder sowie equals()-, hashCode()- und toString()-Methoden.
}
