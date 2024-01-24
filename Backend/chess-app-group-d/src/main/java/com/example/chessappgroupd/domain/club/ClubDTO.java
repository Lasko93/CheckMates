package com.example.chessappgroupd.domain.club;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.appUser.AppUserDTO;
import com.example.chessappgroupd.domain.chat.Chat;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Data
public class ClubDTO {
    String clubName;
    Chat chat;
    List<AppUserDTO> members;

    public ClubDTO (String clubName,
                    Chat chat,
                    List<AppUser> appUserMembers) {
        this.clubName = clubName;
        this.chat = chat;
        this.members = convertMembersInListToDTO(appUserMembers);
    }
    private List<AppUserDTO> convertMembersInListToDTO(List<AppUser> x) {
        List<AppUserDTO> result = new ArrayList<>();
        for (AppUser appUser:x) {
            result.add(AppUserDTO.getSafeDetailsFromUserDTO(appUser));
        }
        return result;
    }

    public static ClubDTO getDetailsFromClubDTO(Club club) {
        return new ClubDTO(club.getClubName(),
                            club.getClubChat(),
                            club.getMembers());
    }
}
