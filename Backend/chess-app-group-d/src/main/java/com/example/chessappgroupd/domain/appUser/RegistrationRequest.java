package com.example.chessappgroupd.domain.appUser;
import java.time.LocalDate;
//RegistrationRequest
//represents a DTO of the input at registration
public record RegistrationRequest(String userName, String firstName, String lastName,
                                  LocalDate birthDay, String email, String password) {
    //Records sind seit Java 14 eine kompakte Möglichkeit, unveränderliche Datenklassen zu definieren.
    //Records sind public, final und enthalten automatisch einen public, no-arg-Konstruktor,
    //der alle finalen Felder setzt. Records enthalten auch lesende Zugriffsmethoden für alle
    // Felder sowie equals()-, hashCode()- und toString()-Methoden.
}