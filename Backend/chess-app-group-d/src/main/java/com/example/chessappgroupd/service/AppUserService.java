package com.example.chessappgroupd.service;
import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.appUser.AppUserDTO;
import com.example.chessappgroupd.domain.gamehistory.GameHistoryRepository;
import com.example.chessappgroupd.domain.gamehistory.GameHistoryService;
import com.example.chessappgroupd.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
//AppUserService
//Contains classic find and delete methods for an AppUser
//These methods return an DTO of an AppUser in order to only send secure data to the frontend
//There is also a converter: AppUser -> AppUserDTO
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final ProfilePictureRepository profilePictureRepository;
    private final AuthCodeRepository authCodeRepository;
    private final ClubRepository clubRepository;
    private final ClubService clubService;
    private final GameRepository gameRepository;
    private final GameHistoryRepository gameHistoryRepository;
    private final GameHistoryService gameHistoryService;
    @Value("${app.user.username.placeholder}")
    private String USER_PLACEHOLDER;
    public static final String USER_NOT_FOUND_MSG = "User with username %s not found!";
    private static final String USER_ALREADY_EXISTS_MSG = "User with username %s already exists!";

    //converts an AppUser to an AppUserDTO, for data safety
    private AppUserDTO convertToDTO(AppUser appUser) {
        return AppUserDTO.getSafeDetailsFromUserDTO(appUser);
    }
    //returns all registered Users as a List of AppUserDTO
    public List<AppUserDTO> findAll() {
        return appUserRepository.findAll().stream()
                .filter(appUser -> !appUser.getUsername().equals(USER_PLACEHOLDER))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    //returns an AppUserDTO by username
    public AppUserDTO findByUserName(String userName) {
        return appUserRepository.findDtoByUserName(userName).filter(appUser -> !appUser.getUserName().equals(USER_PLACEHOLDER))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    //deletes an User by username
    public void deleteByUserName(String userName){
        if(userName.equals(USER_PLACEHOLDER)) {
            return;
        }
        if (profilePictureRepository.existsByUserName(userName)) {
            profilePictureRepository.deleteById(userName);

        }
        if (authCodeRepository.existsByUserName(userName)) {
            authCodeRepository.deleteByUserName(userName);
        }
        try{
            if (clubRepository.existsByClubName(clubService.findByMember(userName).getClubName())) {
                clubService.leave(clubService.findByMember(userName).getClubName(),userName);
            }
        }catch (UsernameNotFoundException | ResponseStatusException ignored){
            //UsernameNotFoundException wird von findByMember geworfen, wenn ein User neu in ein club joint und in keinem club war
            //ResponseStatusException wird von leave geworfen, wenn ein User noch in einem andern Club angehört als in den parametern angegeben
        }
        if (gameRepository.existsByPlayerUserName(userName)) {
            gameRepository.deleteByPlayerUserName(userName);
        }
        //YUU GAME HISTORY
        if (gameHistoryRepository.existsByPlayerUserName(userName)) {
            gameHistoryService.deleteOnlyGameHistoryByPlayerUserName(userName);
        }
        //
        appUserRepository.deleteById(userName);
    }
    //UserDetailsService method, loads user data
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException { //by id
        return appUserRepository.findByUserName(userName).orElseThrow(()->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, userName)));
    }
    public UserDetailsService userDetailsService () throws UsernameNotFoundException {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
                return appUserRepository.findByUserName(userName).orElseThrow(()->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, userName)));
            }
        };
    }
    //zum TESTEN stehen gelassen!
    public void addAppUser(AppUser appUser) {
        if (appUserRepository.existsByUserName(appUser.getUsername())) {
            // Throw an exception or handle it in a way that your application expects
            throw new IllegalStateException(String.format(USER_ALREADY_EXISTS_MSG, appUser.getUsername()));
        }
        appUserRepository.save(appUser);
    }
}
