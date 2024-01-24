package com.example.chessappgroupd.domain.chesspuzzle;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.game.GameRequest;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

//every appUser will be linked to one trophy. Here you can store what chesspuzzles have been played and succeeded and true if 3 or more correct
@Entity
@Table(name = "TAB_EXPERTTROPHY")
public class ExpertTrophy {

    //this should be simultaneous to APPUSER id (username)
    @Id
    @Column(name = "EXPERTTROPHY_ID", unique = false)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "EXPERTTROPHY_EXPERTSTATUS")
    private ExpertStatus expertStatus;

    //mappedBy = "expertTrophy", maybe insert and comment-in in chesspuzzles
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "EXPERTTROPHY_CHESSPUZZLES")
    private Set<ChessPuzzle> chessPuzzles = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "EXPERTTROPHY_APPUSER")
    private AppUser appUser;

    //constructor
    protected ExpertTrophy() {
        //JPA
    }

    //per default every Appuser shall have one Experttrophy, this will be initialised and or checked whenever User plays Chesspuzzle (either create this object or alter its properties!)
    public ExpertTrophy(AppUser appUser) {
        this.id = appUser.getUsername();
        this.appUser = appUser;
        this.expertStatus = ExpertStatus.NOOB;
        this.chessPuzzles = new HashSet<ChessPuzzle>();
    }

    //getter setter

    public String getId() {
        return id;
    }

    public ExpertStatus getExpertStatus() {
        return expertStatus;
    }

    public Set<ChessPuzzle> getChessPuzzles() {
        return chessPuzzles;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setExpertStatus(ExpertStatus expertStatus) {
        this.expertStatus = expertStatus;
    }

    public void setChessPuzzles(Set<ChessPuzzle> chessPuzzles) {
        this.chessPuzzles = chessPuzzles;
    }


    //methods


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpertTrophy that = (ExpertTrophy) o;
        return Objects.equals(id, that.id) && expertStatus == that.expertStatus && Objects.equals(chessPuzzles, that.chessPuzzles) && Objects.equals(appUser, that.appUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expertStatus, chessPuzzles, appUser);
    }

    @Override
    public String toString() {
        return "ExpertTrophy{" +
                "id='" + id + '\'' +
                ", expertStatus=" + expertStatus +
                ", chessPuzzles=" + chessPuzzles +
                ", appUser=" + appUser +
                '}';
    }
}
