package com.example.chessappgroupd.service;
import com.example.chessappgroupd.domain.appUser.ProfilePicture;
import com.example.chessappgroupd.repository.AppUserRepository;
import com.example.chessappgroupd.repository.ProfilePictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
@Service
@RequiredArgsConstructor
//ProfilePictureService
//Contains classic find and delete methods for a ProfilePicture
//These methods return an ProfilePicture
//There is also a methode to safe and create an ProfilePicture
public class ProfilePictureService {
    private final ProfilePictureRepository profilePictureRepository;
    private final AppUserRepository appUserRepository;
    //returns an ProfilePicture by username
    public ProfilePicture findProfilePicByUser(String userName) {
        if (appUserRepository.existsByUserName (userName)) {
            return profilePictureRepository.findById(userName)
                    .orElseThrow(() -> new UsernameNotFoundException(String.format("User: '%s' doesn't have a profile picture!", userName)));
        }
        throw new UsernameNotFoundException(String.format(AppUserService.USER_NOT_FOUND_MSG, userName));
    }
    //creates an ProfilePicture of a User by username
    private ProfilePicture createProfilePicture(MultipartFile file, String userName) throws IOException {
        byte[] bytes = file.getBytes();
        ProfilePicture image = new ProfilePicture();
        image.setUserName(userName);
        image.setFileName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setImages(bytes);
        return image;
    }
    //saves an ProfilePicture by username
    public String saveProfilePic(MultipartFile file,String userName) throws IOException {
        if (file == null || file.isEmpty()) {
            return "";
        }
        if (appUserRepository.existsByUserName(userName)) {
            if (profilePictureRepository.existsByUserName(userName)) {
                profilePictureRepository.deleteById(userName);
                profilePictureRepository.save(createProfilePicture(file, userName));
                return String.format("User: '%s' uploaded a !!!NEW!!! profile picture: '%s' successfully! (old one was deleted)",userName, file.getOriginalFilename());
            }
            profilePictureRepository.save(createProfilePicture(file, userName));
            return String.format("User: '%s' uploaded it's profile picture: '%s' successfully!",userName, file.getOriginalFilename());
        }
        throw new UsernameNotFoundException(String.format(AppUserService.USER_NOT_FOUND_MSG, userName));
    }
    //deletes an ProfilePicture by username
    public void deleteProfilePicByUser(String userName) throws FileNotFoundException {
        if (appUserRepository.existsByUserName (userName)) {
            if (profilePictureRepository.existsByUserName(userName)) {
                profilePictureRepository.deleteById(userName);
            }
            throw new FileNotFoundException(String.format("User: '%s' didn't upload a profile picture yet!", userName));
        }else {
            throw new UsernameNotFoundException(String.format("User with username: '%s' doesn't exists!", userName));
        }
    }
}