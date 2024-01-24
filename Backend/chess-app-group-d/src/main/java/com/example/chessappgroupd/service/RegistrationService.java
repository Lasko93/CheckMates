package com.example.chessappgroupd.service;
import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.appUser.RegistrationRequest;
import com.example.chessappgroupd.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.Map;
@Service
@RequiredArgsConstructor
//RegistrationService
public class RegistrationService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.user.username.placeholder}")
    private String USER_PLACEHOLDER;
    private static final String ALREADY_REGISTERED = "Username is already taken!";
    //registers a user if the username does not yet exist
    public Map<String,String> register(RegistrationRequest request) {
        //YUU GAME HISTORY
        createPlaceholder();
        //
        if (appUserRepository.existsByUserName(request.userName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ALREADY_REGISTERED);
        }
        //create new user
        var appUser = new AppUser();
        appUser.setUserName(request.userName());
        appUser.setFirstName(request.firstName());
        appUser.setLastName(request.lastName());
        appUser.setBirthDay(request.birthDay());
        appUser.setEmail(request.email());
        appUser.setPassword(passwordEncoder.encode(request.password()));
        try {
            appUserRepository.save(appUser);
            Map<String, String> response = new HashMap<>();
            response.put("message", String.format("User with username: '%s' was successfully registered!", appUser.getUsername()));
            return response;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to register user due to an internal error.");
        }
    }
    //YUU GAME HISTORY
    private void createPlaceholder() {
        if (!appUserRepository.existsByUserName(USER_PLACEHOLDER)) {
            var placeholder = new AppUser();
            placeholder.setUserName(USER_PLACEHOLDER);
            placeholder.setFirstName("null");
            placeholder.setLastName("null");
            placeholder.setBirthDay(null);
            placeholder.setEmail("null");
            placeholder.setPassword("null");
            placeholder.setScore(0);
            placeholder.setFriendlistVisible(false);
            try {
                appUserRepository.save(placeholder);
            } catch (Exception ignored){}
        }
    }
    //
}