package com.example.chessappgroupd.controller;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.appUser.AppUserDTO;
import com.example.chessappgroupd.domain.friendslist.FriendRequest;
import com.example.chessappgroupd.repository.FriendRequestRepository;
import com.example.chessappgroupd.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/friend")
@CrossOrigin(origins = "http://localhost:3000")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @PutMapping("/change-friendlist-visibility")
    public ResponseEntity<?> changeFriendlistVisibility(@RequestParam String appUser) {
        friendRequestService.changeFriendlistVisibility(appUser);
        return ResponseEntity.ok(Map.of("message", "Visibility changed successfully"));
    }

    @PostMapping("/send-friendrequest/{soonFriend}")
    public ResponseEntity<FriendRequest> sendFriendrequest(@RequestParam("sender") String sender, @PathVariable("soonFriend") String soonFriend) {
        try {
            FriendRequest friendRequest = friendRequestService.createFriendRequest(sender,soonFriend);
            return new ResponseEntity<>(friendRequest, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/accept-friendrequest/{yesFriend}")
    public ResponseEntity<FriendRequest> acceptFriendrequest(@RequestParam("user") String user, @PathVariable("yesFriend") String yesFriend) {
        try {
            FriendRequest friendRequest = friendRequestService.acceptFriendRequest(user,yesFriend);
            return new ResponseEntity<>(friendRequest,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/friendlist")
    public ResponseEntity<List<AppUserDTO>> allfriends(@RequestParam("user") String appUser){
        List<AppUserDTO> friends = friendRequestService.showAllFriends(appUser);
        if (!friends.isEmpty()) {
            return new ResponseEntity<>(friends, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all-friendrequests")
    public ResponseEntity<List<FriendRequest>> allfriendRequests(@RequestParam("user") String appUser){
        List<FriendRequest> friendRequests = friendRequestService.showAllFriendRequests(appUser);
        if (!friendRequests.isEmpty()) {
            return new ResponseEntity<>(friendRequests, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all-sent-friendrequests")
    public ResponseEntity<List<FriendRequest>> allSentFriendRequests(@RequestParam("user") String appUser){
        List<FriendRequest> friendRequests = friendRequestService.showAllSentFriendRequests(appUser);
        if (!friendRequests.isEmpty()) {
            return new ResponseEntity<>(friendRequests, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/delete-friend/{exFriend}")
    public ResponseEntity<?> deletefriend(@RequestParam String appUser, @PathVariable("exFriend") String exFriend){
        try {
            friendRequestService.deleteFriend(appUser,exFriend);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }




    @DeleteMapping("/decline-friendrequest/{noFriend}")
    public ResponseEntity<FriendRequest> declineFriendrequest(@RequestParam String receiver, @PathVariable("noFriend") String noFriend){
        try {
            friendRequestService.declineFriendRequest(receiver,noFriend);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-friendrequest/{noFriend}")
    public ResponseEntity<?> deleteFriendrequest(@RequestParam String sender, @PathVariable("noFriend") String noFriend) {
        try {
            friendRequestService.deleteFriendRequest(noFriend,sender);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
