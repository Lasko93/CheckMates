package com.example.chessappgroupd.service;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.appUser.AppUserDTO;
import com.example.chessappgroupd.domain.chat.Chat;
import com.example.chessappgroupd.domain.chat.ChatRepository;
import com.example.chessappgroupd.domain.club.Club;
import com.example.chessappgroupd.domain.club.ClubDTO;
import com.example.chessappgroupd.domain.club.ClubRequest;
import com.example.chessappgroupd.repository.AppUserRepository;
import com.example.chessappgroupd.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {
//  Ein Benutzer kann einen Schachclub mit einem eindeutigen Namen erstellen. Jeder Nutzer kann die
//  Liste aller erstellten Schachclubs einsehen. Weiterhin ist jeder Benutzer in der Lage einen Schachclub
//  beizutreten. Nutzer innerhalb eines Schachclubs können miteinander über einen Chat in Echtzeit
//  kommunizieren (siehe Chat). Außerdem soll im Profil eines jeden Nutzers ersichtlich sein, in welchen
//  Schachclubs er beigetreten ist.
    private final ClubRepository clubRepository;
    private final AppUserRepository appUserRepository;
    private final ChatRepository chatRepository;
    private static final String CLUB_NOT_EXISTS = "The club %s doesn't exist!";
    private static final String CLUB_MEMBERS_NOT_EXISTS = "There are zero Members in %s!";
    private static final String CLUB_DELETED = "%s was successfully deleted!";
    private static final String CREATED_SUCCESSFULLY = "%s created a new club called %s!";
    private static final String JOINED_SUCCESSFULLY = "%s joined %s!";
    private static final String USER_IN_OTHER_CLUB = "%s is still in club: %s! Please leave %s first!";
    private static final String LEFT_SUCCESSFULLY = "%s left %s!";
    private static final String CHAT_STILL_EXISTS = "Chat %s still exits in club %s!";
    private static final String DELETED_CLUB_AND_CHAT = "club %s and it's chat %s were deleted";

    //converts a Club to an ClubDTO, for data safety
    private ClubDTO convertToDTO(Club club) {
        return ClubDTO.getDetailsFromClubDTO(club);
    }
    //find all clubs
    public List<ClubDTO> findAll() {
        return clubRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    //find all club member
    public List<AppUserDTO> findAllMembersByClubName(String clubName) {
        if (clubRepository.existsByClubName(clubName)) {
            if (!clubRepository.findByClubName(clubName).getMembers().isEmpty()) {
                return convertToDTO(clubRepository.findByClubName(clubName)).getMembers();
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format(CLUB_MEMBERS_NOT_EXISTS,clubName));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format(CLUB_NOT_EXISTS,clubName));
    }
    //find club by clubname
    public ClubDTO findByClubName(String clubName) {
        if (clubRepository.existsByClubName(clubName)) {
            return convertToDTO(clubRepository.findByClubName(clubName));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format(CLUB_NOT_EXISTS,clubName));
    }
    //find club by member username
    public ClubDTO findByMember(String userName) {
        if (appUserRepository.existsByUserName(userName)) {
            for (Club club:clubRepository.findAll()) {
                for (AppUser user: club.getMembers()) {
                    if (user.getUsername().equals(userName)) {
                        return convertToDTO(club);
                    }
                }
            }
        }
        throw new UsernameNotFoundException(String.format(AppUserService.USER_NOT_FOUND_MSG, userName));
    }
    //delete club by clubname
    public String deleteByClubName(String clubName) {
        if (clubRepository.existsByClubName(clubName)) {
            clubRepository.deleteById(clubName);
            chatRepository.deleteById(clubName);
            return String.format(CLUB_DELETED,clubName);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format(CLUB_NOT_EXISTS,clubName));
    }
    //delete club by member username
    public String deleteByMember(String userName) {
        return deleteByClubName(findByMember(userName).getClubName());
    }
    //create club
    public String create(ClubRequest clubRequest) {
        List<AppUser> members = new ArrayList<>();
        members.add(appUserRepository.findAppUser(clubRequest.userName()));
        List<String>subs = new ArrayList<>();
        subs.add(clubRequest.userName());
        Chat chat = Chat.builder()
                .chatId(clubRequest.clubName())
                .subs(subs)
                .isClubchat(true)
                .build();
        Club club = new Club();
        club.setClubName(clubRequest.clubName());
        club.setMembers(members);
        club.setClubChat(chat);
        chatRepository.save(chat);
        clubRepository.save(club);
        return String.format(CREATED_SUCCESSFULLY,clubRequest.userName(), clubRequest.clubName());
    }
    //join club (automatisches austreten aus dem alten club oder es wird ein neues erstellt falls noch nirgends drin)
    public String join(ClubRequest clubRequest) {
        if (!appUserRepository.existsByUserName(clubRequest.userName())) {
            throw new UsernameNotFoundException(String.format(AppUserService.USER_NOT_FOUND_MSG, clubRequest.userName()));
        }
        try {
            leave(findByMember(clubRequest.userName()).getClubName(),clubRequest.userName());
        }catch (UsernameNotFoundException | ResponseStatusException ignored){
            //UsernameNotFoundException wird von findByMember geworfen, wenn ein User neu in ein club joint und in keinem club war
            //ResponseStatusException wird von leave geworfen, wenn ein User noch in einem andern Club angehört als in den parametern angegeben
        }
        if (!clubRepository.existsByClubName(clubRequest.clubName())) {
            return create(clubRequest);
        }
        if (!chatRepository.existsByChatId(clubRequest.clubName())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format(CHAT_STILL_EXISTS,clubRequest.clubName(),clubRequest.clubName()));
        }
        Club club = clubRepository.findByClubName(clubRequest.clubName());
        club.getMembers().add(appUserRepository.findAppUser(clubRequest.userName()));
        club.getClubChat().getSubs().add(clubRequest.userName());
        chatRepository.save(club.getClubChat());
        clubRepository.save(club);
        return String.format(JOINED_SUCCESSFULLY,clubRequest.userName(),clubRequest.clubName());
    }
    //leave club
    public String leave(String clubName, String userName) {
        if (!clubRepository.existsByClubName(clubName)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format(CLUB_NOT_EXISTS,clubName));
        }
        if (!appUserRepository.existsByUserName(userName)) {
            throw new UsernameNotFoundException(String.format(AppUserService.USER_NOT_FOUND_MSG, userName));
        }
        try{
            if (!findByMember(userName).getClubName().equals(clubName)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,String.format(USER_IN_OTHER_CLUB,userName,findByMember(userName).getClubName(),findByMember(userName).getClubName()));
            }
        }catch (UsernameNotFoundException ignored){
            //UsernameNotFoundException wird von findByMember geworfen, wenn ein User neu in ein club joint und in keinem club war
        }
        Club club = clubRepository.findByClubName(clubName);
        club.getMembers().remove(appUserRepository.findAppUser(userName));
        club.getClubChat().getSubs().remove(userName);
        chatRepository.save(club.getClubChat());
        clubRepository.save(club);
        if (findByClubName(club.getClubName()).getMembers().isEmpty()) {
            //club mit chat löschen, wenn niemand mehr im club ist
            deleteByClubName(clubName);
            return String.format(DELETED_CLUB_AND_CHAT,clubName,clubName);
        }
        return String.format(LEFT_SUCCESSFULLY,userName,clubName);
    }
}
