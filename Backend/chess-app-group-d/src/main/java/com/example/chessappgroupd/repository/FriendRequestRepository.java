package com.example.chessappgroupd.repository;

import com.example.chessappgroupd.domain.friendslist.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
@Transactional
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.sender = :username AND fr.pending = false")
    List<FriendRequest> findAcceptedFriendRequestsBySenderUserName(@Param("username") String username);

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.receiver = :username AND fr.pending = false")
    List<FriendRequest> findAcceptedFriendRequestsByReceiverUserName(@Param("username") String username);

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.sender = :username AND fr.pending = true")
    List<FriendRequest> findSendedFriendRequests(@Param("username") String username);

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.receiver = :username AND fr.pending = true ")
    List<FriendRequest> findPendingFriendRequests(@Param("username") String username);

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.receiver = :receiver AND fr.sender = :sender AND fr.pending = true")
    FriendRequest findPendingFriendRequest(@Param("receiver") String receiver, @Param("sender") String sender);

    @Query("SELECT fr FROM FriendRequest fr WHERE (fr.receiver = :receiver AND fr.sender = :sender) OR (fr.receiver = :sender AND fr.sender = :receiver) AND fr.pending = false")
   FriendRequest findFriend(@Param("sender") String receiver, @Param("receiver") String sender);

    @Query("SELECT fr FROM FriendRequest fr WHERE (fr.receiver = :receiver AND fr.sender = :sender) OR (fr.receiver = :sender AND fr.sender = :receiver)")
    List<FriendRequest> findIfExists(@Param("sender") String receiver, @Param("receiver") String sender);

    @Query("SELECT  fr FROM FriendRequest fr WHERE fr.receiver = :receiver AND fr.sender = :sender AND fr.pending = true")
    FriendRequest findFriendRequest(@Param("receiver") String receiver, @Param("sender") String sender);
}
