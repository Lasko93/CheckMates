package com.example.chessappgroupd.domain.appUser;
import lombok.Data;
import lombok.Getter;
import java.time.LocalDate;
@Getter
@Data
//AppUserDTO
//A DTO of an AppUser
public class AppUserDTO {
    String userName;
    String firstName;
    String lastName;
    LocalDate birthDay;
    String email;
    int score;
    boolean friendlistVisible;
    public AppUserDTO(String userName, String firstName, String lastName, LocalDate birthDay, String email, int score, boolean friendlistVisible) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.email = email;
        this.score = score;
        this.friendlistVisible = friendlistVisible;
    }
    //converts an AppUser into an AppUserDTO without a password
    public static AppUserDTO getSafeDetailsFromUserDTO(AppUser appUser) {
        return new AppUserDTO(
                appUser.getUsername(),
                appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getBirthDay(),
                appUser.getEmail(),
                appUser.getScore(),
                appUser.isFriendlistVisible()
        );
    }
}
