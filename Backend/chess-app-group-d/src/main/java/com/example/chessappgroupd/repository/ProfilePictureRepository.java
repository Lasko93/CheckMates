package com.example.chessappgroupd.repository;
import com.example.chessappgroupd.domain.appUser.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
//ProfilePicture repo
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, String> {
    Boolean existsByUserName(String userName);
}