package com.example.chessappgroupd.domain.appUser;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
//AuthCode
//represents a two-factor authentication Code
public class AuthCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private AppUser user;
    private String authCode;
    private LocalDateTime expiryTime;
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
    public void setUser(AppUser user) {
        this.user = user;
    }
    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }
}
