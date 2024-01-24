package com.example.chessappgroupd.service;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.appUser.AppUserDTO;
import com.example.chessappgroupd.domain.appUser.Emails;

import com.example.chessappgroupd.domain.friendslist.FriendRequest;
import com.example.chessappgroupd.repository.AppUserRepository;
import com.example.chessappgroupd.repository.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private JavaMailSender mailSender;

    //private final String ALREADY_SEND = "Friendrequest already send";
    //private final String ALREADY_ACCEPTED = "Friendrequest already accepted";
    private final String ALREADY_HERE = "Frienrequest already sent or accepted";
    private final String REQUEST_NOT_FOUND = "Friendrequest not found";
    private final String FRIEND_NOT_FOUND = "No Friend with this name";

    public void sendFriendRequestEmail(String sender, String receiver) {
        var rec = appUserService.findByUserName(receiver);
        var sen = appUserService.findByUserName(sender);
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(Emails.CHECKMATES_MAIL_ADDRESS);
        mail.setTo(rec.getEmail());
        mail.setSubject(Emails.FRIEND_REQUEST_EMAIL_SUBJECT);
        mail.setText(String.format(Emails.FRIEND_REQUEST_EMAIL_TEXT, rec.getUserName(),sen.getUserName()));
        mailSender.send(mail);
    }

   // public void checkFriendRequest
    public FriendRequest createFriendRequest(String sender, String receiver) throws Exception {
        AppUserDTO receiverUser = appUserService.findByUserName(receiver); //!!!!!
        List<FriendRequest> list = friendRequestRepository.findIfExists(sender,receiver);
        if(!list.isEmpty()) throw new Exception(ALREADY_HERE);
        FriendRequest friendRequest = new FriendRequest(sender,receiver,true);
        sendFriendRequestEmail(sender,receiver);
        friendRequestRepository.save(friendRequest);
        return friendRequest;
    }
    public void changeFriendlistVisibility(String appUser) {
        AppUser user =  appUserRepository.findAppUser(appUser);
        user.setFriendlistVisible(!user.isFriendlistVisible());
        appUserRepository.save(user);
    }

    public FriendRequest acceptFriendRequest(String receiver, String sender) throws Exception {
        appUserService.findByUserName(sender);
        FriendRequest friendRequest = friendRequestRepository.findPendingFriendRequest(receiver,sender);
        if(friendRequest != null) {
            friendRequest.setStatus(false);
        friendRequestRepository.save(friendRequest);}
            else {throw new Exception(REQUEST_NOT_FOUND);}
            return friendRequest;
    }

    public void deleteFriend(String appUser, String friend) throws Exception {
        appUserService.findByUserName(friend);
        appUserService.findByUserName(appUser);
        FriendRequest friendRequest = friendRequestRepository.findFriend(appUser,friend);
       if(friendRequest != null) {friendRequestRepository.delete(friendRequest);}
       else {throw new Exception(FRIEND_NOT_FOUND);}
    }

    public void deleteFriendRequest(String appUser, String friend) throws Exception {
        appUserService.findByUserName(friend);
        appUserService.findByUserName(appUser);
        FriendRequest friendRequest = friendRequestRepository.findFriendRequest(appUser,friend);
        if(friendRequest != null) {friendRequestRepository.delete(friendRequest);}
        else {throw new Exception(FRIEND_NOT_FOUND);}
    }

    public void declineFriendRequest(String appUser, String friend) throws Exception {
        appUserService.findByUserName(friend);
        appUserService.findByUserName(appUser);
        FriendRequest friendRequest = friendRequestRepository.findFriendRequest(appUser,friend);
        if(friendRequest != null) {friendRequestRepository.delete(friendRequest);}
        else {throw new Exception(FRIEND_NOT_FOUND);}
    }

    public List<AppUserDTO> showAllFriends(String appUser) {
        List<FriendRequest> listSender = friendRequestRepository.findAcceptedFriendRequestsBySenderUserName(appUser);
        List<FriendRequest> listReceiver = friendRequestRepository.findAcceptedFriendRequestsByReceiverUserName(appUser);
        List<AppUserDTO> friendlist = new ArrayList<>();
        listSender.forEach(friendRequest -> friendlist.add(appUserService.findByUserName(friendRequest.getReceiver())));
        listReceiver.forEach(friendRequest -> friendlist.add(appUserService.findByUserName(friendRequest.getSender())));
        return friendlist;
    }

    public List<FriendRequest> showAllFriendRequests(String appUser) {
        List<FriendRequest> list = friendRequestRepository.findPendingFriendRequests(appUser);
        return list;
    }

    public List<FriendRequest> showAllSentFriendRequests(String appUser) {
        List<FriendRequest> list = friendRequestRepository.findSendedFriendRequests(appUser);
        return list;
    }

}

