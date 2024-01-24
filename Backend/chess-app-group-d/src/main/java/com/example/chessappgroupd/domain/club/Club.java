package com.example.chessappgroupd.domain.club;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.appUser.AppUserDTO;
import com.example.chessappgroupd.domain.chat.Chat;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Club {
//  Ein Benutzer kann einen Schachclub mit einem eindeutigen Namen erstellen. Jeder Nutzer kann die
//  Liste aller erstellten Schachclubs einsehen. Weiterhin ist jeder Benutzer in der Lage einen Schachclub
//  beizutreten. Nutzer innerhalb eines Schachclubs können miteinander über einen Chat in Echtzeit
//  kommunizieren (siehe Chat). Außerdem soll im Profil eines jeden Nutzers ersichtlich sein, in welchen
//  Schachclubs er beigetreten ist.
    @Id
    private String clubName;
    @OneToOne
    private Chat clubChat;
    @ManyToMany
    private List<AppUser> members = new ArrayList<AppUser>();
    public void deleteMemberFromList(String userName) {
        members.removeIf(user -> user.getUsername().equals(userName));
    }

}
