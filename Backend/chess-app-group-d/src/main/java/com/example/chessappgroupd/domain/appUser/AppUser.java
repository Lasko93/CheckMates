package com.example.chessappgroupd.domain.appUser;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDate;
import java.util.Collection;
@Entity
@Table(name = "APPUSER")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
//AppUser
//One User has a username as an id,
//a firstname, a lastname
//a birthday, an email address
//a password and game score
//also a boolean to modify the friend list visibility
public class AppUser implements UserDetails {
    @Id
    @Column(name = "APPUSER_USERNAME")
    private String userName; //id
    @Column(name = "APPUSER_FIRST_NAME")
    private String firstName;
    @Column(name = "APPUSER_LAST_NAME")
    private String lastName;
    @Column(name = "APPUSER_BIRTH_DAY")
    private LocalDate birthDay;
    @Column(name = "APPUSER_EMAIL")
    private String email;
    @Column(name = "APPUSER_PASSWORD")
    private String password;
    @Column(name = "APPUSER_SCORE")
    private Integer score = 500;
    @Column(name = "FRIENDLIST_VISIBLE")
    private boolean friendlistVisible = true;
    public AppUser(
                    String userName,
                    String firstName,
                   String lastName,
                   LocalDate dateOfBirth,
                   String email,
                   String password,
                    boolean friendlistVisible) {
        this.userName=userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.birthDay = dateOfBirth;
        this.friendlistVisible = friendlistVisible;
    }
    //UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    @Override
    public String getUsername() {
        return userName;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
