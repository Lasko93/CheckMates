package com.example.chessappgroupd.domain.chat;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);


    private final ChatService chatService;
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;


    @PostMapping("/create-chat")
//     public ResponseEntity<String> createChatroom(@RequestBody ChatRequest chatRequest) {
//        try {
//            String chatId = chatService.createChat(chatRequest.subs(), chatRequest.chatId());
//            chatRequest.subs().forEach(user ->
//                    messagingTemplate.convertAndSendToUser(user, "/queue/chat", "New chat created: " + chatId));
//            return ResponseEntity.ok(chatId);
//        }
//        catch (Exception e) {
//            logger.error("Error creating chat room: {}", e.getMessage(), e);
//            e.printStackTrace(); // Log the exception to the console
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//
//    }
    public ResponseEntity<String> createChatroom(@RequestBody ChatRequest chatRequest) {
        try {
            String chatId = chatService.createChat(chatRequest.subs(), chatRequest.chatId());
            // Notify each user about the new chat creation
            chatRequest.subs().forEach(user ->
                    messagingTemplate.convertAndSendToUser(user, "/queue/chat", "New chat created: " + chatId));
            return ResponseEntity.ok(chatId);
        } catch (Exception e) {

            logger.error("Error creating chat room: {}", e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/read")
    public void readMessage(@RequestParam("user") String user, @RequestParam("messageId") Long messageId) {
        chatMessageService.readMessage(user, messageId);
    }


    @GetMapping("/{user}")
    public ResponseEntity<List<String>> showMyChatrooms(@PathVariable("user") String user) {
        try {
            return new ResponseEntity<>(chatService.myChats(user), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{chatId}/add/{sub}")
    public ResponseEntity<String> addSubToChatroom(@PathVariable("chatId") String chatId, @PathVariable("sub") String sub) {
        try {
            chatService.addSub(chatId, sub);
            return ResponseEntity.ok("Subscriber added to chat");
        } catch (Exception e) {
            logger.error("Error adding subscriber to chat: {}", e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/delete-chat/{chatId}")
    public ResponseEntity<String> deleteChatroom(@RequestParam("user") String user,
                                                 @PathVariable("chatId") String chatId) {
        try {
            chatService.deleteChat(user, chatId);
            return new ResponseEntity<>(chatId, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @MessageMapping("/chat.sendMessage/{chatId}")
    @SendTo("/topic/{chatId}")
    public ResponseEntity<ChatMessage> sendMessage(ChatMessageRequest chatMessageRequest) {
        try {
            ChatMessage chatMessage = chatMessageService.sendMessage(
                    chatMessageRequest.sender(),
                    chatMessageRequest.message(),
                    chatMessageRequest.chatId());
            return new ResponseEntity<>(chatMessage, HttpStatus.OK);
        }  catch (Exception e) {
            logger.error("Error in sendMessage: {}", e.getMessage(), e);

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editMessage(@RequestBody EditMessageRequest edit) {
        try {
            String updatedMessageContent = chatMessageService.editMessage(edit.sender(), edit.id(), edit.edit());
            System.out.println("Updated message content: " + updatedMessageContent);
            return new ResponseEntity<>(updatedMessageContent, HttpStatus.OK);
        } catch (Exception e) {
            // Log the error or return a specific error message
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<Boolean> deleteMessage(@RequestParam("user") String user, @PathVariable("messageId") Long messageId) {
        try {
            return new ResponseEntity<>(chatMessageService.deleteMessage(user,messageId), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/messages/{chatId}")
    public ResponseEntity<List<ChatMessage>> findMessages(@RequestParam("user") String user, @PathVariable("chatId") String chatId) {
        try {
            return ResponseEntity.ok(chatMessageService.showMessages(chatId, user));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/read/{messageId}")
    public void setMessageRead(@RequestParam("user") String user, @PathVariable("messageId") Long messageId) {
        chatMessageService.readMessage(user, messageId);
    }
}
