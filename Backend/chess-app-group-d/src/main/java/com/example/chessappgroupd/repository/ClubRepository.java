package com.example.chessappgroupd.repository;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.club.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ClubRepository extends JpaRepository<Club, String> {
    Club findByClubName(String clubName);
    @Query("SELECT c.members FROM Club c WHERE c.clubName = :clubName")
    List<AppUser> findMembersByClubName(String clubName);
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Club c WHERE c.clubName = :clubName")
    Boolean existsByClubName(String clubName);
}
