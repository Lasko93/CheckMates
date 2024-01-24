package com.example.chessappgroupd.friend;

import com.example.chessappgroupd.controller.FriendRequestController;
import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.friendslist.FriendRequest;
import com.example.chessappgroupd.repository.AppUserRepository;
import com.example.chessappgroupd.repository.FriendRequestRepository;
import com.example.chessappgroupd.service.AppUserService;
import com.example.chessappgroupd.service.FriendRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = FriendRequestController.class)
@ExtendWith(MockitoExtension.class)
public class FriendTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private FriendRequestService friendRequestService;

    private FriendRequestRepository friendRequestRepository;

    private AppUserService appUserService;

    private final AppUserRepository appUserRepository;


    private AppUser KaJa, YuKru;

    public FriendTest(FriendRequestRepository friendRequestRepository,
                      AppUserRepository appUserRepository,
                      FriendRequestService friendRequestService,
                      AppUserService appUserService) {
        this.friendRequestRepository = friendRequestRepository;
        this.appUserRepository = appUserRepository;
        this.friendRequestService = friendRequestService;
        this.appUserService = appUserService;
    }

    @BeforeEach
    public void create() {
        KaJa = new AppUser("KaJa", "Kamil", "Jancen", null, "Email","qwertz",true);
        YuKru = new AppUser("YuKru", "Yuu", "Kruth", null, "Email2", "1234",true);
        appUserRepository.save(KaJa);
        appUserRepository.save(YuKru);
    }
    @Test
    public void testSendfriendRequest() throws Exception {

        create();
        friendRequestService.createFriendRequest(KaJa.getUsername(), YuKru.getUsername());
        //Optional<FriendRequest> friendRequest = friendRequestRepository.findFriend(KaJa.getUsername(), YuKru.getUsername());
       // System.out.println(friendRequest.getReceiver());
    }

}
