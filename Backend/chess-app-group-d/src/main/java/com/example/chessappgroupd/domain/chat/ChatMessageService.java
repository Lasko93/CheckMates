package com.example.chessappgroupd.domain.chat;

import com.example.chessappgroupd.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class ChatMessageService {
    private static final Logger logger = LoggerFactory.getLogger(ChatMessageService.class);


    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public ChatMessage sendMessage(String user, String message, String chatId) throws Exception{
        appUserRepository.findAppUser(user);
        logger.info("Sending message from {} in chat {}", user, chatId);

        var c = chatRepository.findByChatId(chatId);
        if(c != null) {
           ChatMessage chatMessage = ChatMessage.builder()
                   .chatId(chatId)
                   .message(message)
                   .timeStamp(new Date())
                   .sender(user)
                   .read(false)
                   .build();
           chatMessageRepository.save(chatMessage);
           return chatMessage;
        }
        else throw new Exception("No such Chat exists");
    }

    public List<ChatMessage> showMessages(String chatId, String user) throws Exception {
        appUserRepository.findAppUser(user);
        var chat = chatRepository.findByChatId(chatId);
        if(chat != null) {
            if(!chat.getSubs().contains(user)) {
                throw new Exception("You are not part of this Chat");
            }
            List<ChatMessage> list = chatMessageRepository.findByChatId(chatId);
            for(ChatMessage cm : list) {
                if(!(cm.getSender().equals(user))){
                    cm.setRead(true);
                    chatMessageRepository.save(cm);
                }
            }
            return list;
        }
        else throw new Exception("No such Chat exists");
    }

    public String editMessage(String sender, Long messageId, String edit) throws Exception {
        var message = chatMessageRepository.findByMessageId(messageId);
        if (message != null) {
            if (message.isRead()) {
                throw new Exception("Message was read already and can't be edited");
            } else if (message.getSender().equals(sender)) {
                message.setMessage(edit);
                chatMessageRepository.save(message);
                return message.getMessage();
            } else {
                throw new Exception("You are not the Sender and therefore can't edit the message");
            }
        } else {
            throw new Exception("No such Message exists");
        }
    }


    //public void messageRead(String receiver, Long messageId) {
//    appUserRepository.findAppUser(receiver);
//    var message = chatMessageRepository.findByMessageId(messageId);
//    if(message != null) {
//        message.setRead(true);
//        chatMessageRepository.save(message);
//    }
//}
    public boolean deleteMessage(String sender, Long messageId) throws Exception {
        var message = chatMessageRepository.findByMessageId(messageId);
        if(message != null) {
            if(message.isRead()) {
                throw new Exception("Message was read already and can't be edited");
            }
            else if(message.getSender().equals(sender)) {
                chatMessageRepository.deleteById(messageId);
                return true;
            }
            else throw new Exception("You are not the Sender and therefore can't delete the message");
        }
        else throw new Exception("No such Message exists");
    }
    public void readMessage(String user, Long messageId) {
        var au = appUserRepository.findAppUser(user);
        var cm = chatMessageRepository.findByMessageId(messageId);
        if(!cm.getSender().equals(au.getUsername())) {
            cm.setRead(true);
            chatMessageRepository.save(cm);
        }

    }
}
