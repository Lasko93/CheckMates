package com.example.chessappgroupd.domain.chat;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.repository.AppUserRepository;
import com.example.chessappgroupd.repository.ClubRepository;
import com.example.chessappgroupd.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private ClubRepository clubRepository;

    public String createChat(List<String> subs, String chatId) {
        var existingChat = chatRepository.findByChatId(chatId);
        var existingClub = clubRepository.findByClubName(chatId);

        if (existingChat != null || existingClub != null) {
            return chatId;
        } else {
            Chat chat = Chat.builder()
                    .chatId(chatId)
                    .subs(subs)
                    .isClubchat(false)
                    .build();
            chatRepository.save(chat);
            return chatId;
        }
    }



    public void deleteChat(String user, String chatId) throws Exception {
        appUserService.findByUserName(user);
        var c = chatRepository.findByChatId(chatId);
        if(c != null) {
            if(c.isClubchat()) {throw new Exception("You can't delete a Clubchat");}
            if(c.getSubs().contains(user)) {
                chatRepository.delete(c);
            }
            else throw new Exception("You are not part of this Chat");
        }
        else throw new Exception("No such Chatroom exists");
    }

    public List<String> myChats(String user) {
        appUserService.findByUserName(user);
        return chatRepository.findIfInSubs(user);
    }

//    public void addSub(String user, String chatId, String sub) throws Exception{
//        AppUser a = appUserRepository.findAppUser(user);
//        if(a == null) {throw new Exception("No such Username exists");}
//        AppUser s = appUserRepository.findAppUser(sub);
//        if(s == null) {throw new Exception("No such Sub can be added");}
//        var c = chatRepository.findByChatId(chatId);
//        if(c != null) {
//            if(c.isClubchat()) {throw new Exception("You can't add someone to a Clubchat");}
//            if(c.getSubs().contains(s.getUsername())) {throw new Exception("Sub already in this Chat");}
//            if(c.getSubs().contains(user)) {
//                c.getSubs().add(sub);
//                chatRepository.save(c);
//            }
//            else throw new Exception("You are not part of this Chat");
//        }
//        else throw new Exception("No such Chatroom exists");
//    }
public void addSub(String chatId, String sub) throws Exception {
    Chat chat = chatRepository.findByChatId(chatId);
    if (chat == null) {
        throw new Exception("Chat not found");
    }

    // Add subscriber to chat if not already present
    if (!chat.getSubs().contains(sub)) {
        chat.getSubs().add(sub);
        chatRepository.save(chat);
    } else {
        throw new Exception("User already in chat");
    }
}
}
