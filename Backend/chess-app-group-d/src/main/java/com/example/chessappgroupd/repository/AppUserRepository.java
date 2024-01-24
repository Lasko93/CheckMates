package com.example.chessappgroupd.repository;
import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.appUser.AppUserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
//AppUser repo
public interface AppUserRepository extends JpaRepository<AppUser, String> {
    Optional<AppUser> findByUserName(String userName);
    Optional<AppUserDTO> findDtoByUserName(String userNme);
    Boolean existsByUserName(String userName);
    //if you use this method, you have to ensure the existence of that specific AppUser before
    @Query("SELECT au from AppUser au WHERE au.userName = :username")
    AppUser findAppUser(@Param("username") String username);
}