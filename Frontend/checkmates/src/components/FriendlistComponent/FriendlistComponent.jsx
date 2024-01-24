import React, {useEffect, useState} from 'react';
import './FriendlistComponent.css';
import FriendlistBox from "./FriendlistBox";
import {useFetchAllFriends} from '../../services/FriendService';
import ChatRoomComponent from "../Chat/ChatRoomComponent";
import webSocketService from "../../services/WebSocketService";
import GroupChatCreationModal from './GroupChatCreationModal';


const FriendlistComponent = ({ userName }) => {
    const { data: friendsList, isLoading, isError } = useFetchAllFriends(userName);
    const [selectedFriend, setSelectedFriend] = useState(null);
    const [isChatOpen, setIsChatOpen] = useState(false);
    const [groupFriends, setGroupFriends] = useState([]);
    const [currentGroupId, setCurrentGroupId] = useState(null);
    const [shouldOpenChat, setShouldOpenChat] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [chatrooms, setChatrooms] = useState([]);



    useEffect(() => {
        const notificationTopic = `/user/queue/chat`;

        const handleOpenChatNotification = (notification) => {
            console.log("1");
            const chatId = notification.chatId;
            setCurrentGroupId(chatId);
            setIsChatOpen(true);
            setShouldOpenChat(true);
            webSocketService.subscribeToChatTopic(chatId);
        };

        const subscribeToNotifications = () => {
            console.log("2");
            if (!webSocketService.isSubscribed(notificationTopic)) {
                webSocketService.registerSubscription(notificationTopic, (msg) => {
                    const notification = JSON.parse(msg.body);
                    handleOpenChatNotification(notification);
                });
            }
        };

        if (webSocketService.isConnected()) {
            subscribeToNotifications();
        } else {
            webSocketService.connect(subscribeToNotifications, (error) => {
                console.error('Error connecting to WebSocket:', error);
            });
        }

        return () => {
            webSocketService.removeSubscription(notificationTopic);
        };
    }, [webSocketService]);

    useEffect(() => {

        const fetchChatrooms = async () => {
            try {
                const response = await fetch(`http://localhost:8080/chat/${userName}`);
                if (!response.ok) {
                    throw new Error('Failed to fetch chatrooms');
                }
                const chatroomList = await response.json();
                setChatrooms(chatroomList);
            } catch (error) {
                console.error('Error fetching chatrooms:', error);
            }
        };

        fetchChatrooms();
    }, [userName]);

    const openChatroom = (chatroomId) => {
        setCurrentGroupId(chatroomId);
        setIsChatOpen(true);
    };

    const onStartPrivateChat = async (friend) => {
        setSelectedFriend(friend);
        const myUsername = sessionStorage.getItem("userName");
        const chatId = generateChatId(myUsername, friend.userName);

        const chatRequestPayload = {
            chatId: chatId,
            subs: [myUsername, friend.userName]
        };

        try {
            const response = await fetch("http://localhost:8080/chat/create-chat", {
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(chatRequestPayload),
            });
            console.log("Chat creation response:", response);

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            console.log(response);

            setCurrentGroupId(chatId);
            setIsChatOpen(true);

        } catch (error) {
            console.error('Error creating chat:', error);
        }
    };

    const handleOpenChat = (chatId) => {
        setCurrentGroupId(chatId);
        setIsChatOpen(true);
        setShouldOpenChat(true);
    };

    const onAddToGroupChat = (friend) => {
        if (!groupFriends.includes(friend)) {
            setGroupFriends([...groupFriends, friend]);
        }
    };


    const generateChatId = (username1, username2) => {
        // Sort the usernames alphabetically
        const sortedUsernames = [username1, username2].sort();
        return `chat_${sortedUsernames[0]}_${sortedUsernames[1]}`;
    };


    const generateGroupChatId = () => {
        const timeStamp = new Date().getTime();
        return `group_${userName}_${timeStamp}`;
    };
    const handleCreateGroupChat = async (selectedFriends) => {
        if (selectedFriends.length === 0) {
            alert("Please add friends to the group chat.");
            return;
        }

        const groupId = `group_${userName}_${Date.now()}`;
        setCurrentGroupId(groupId);
        setIsChatOpen(true);

        const chatRequestPayload = {
            chatId: groupId,
            subs: [userName, ...selectedFriends.map(friend => friend.userName)]
        };

        try {
            const response = await fetch("http://localhost:8080/chat/create-chat", {
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(chatRequestPayload),
            });
            console.log(response);

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

        } catch (error) {
            console.error('Error creating group chat:', error);
        }
    };


    if (isLoading) return <div>Loading friends...</div>;
    if (isError || !friendsList) return <div>Error loading friends list.</div>;

    return (
        <div className="friendlist">
            {friendsList && friendsList.length > 0 ? (
                <ul>
                    {friendsList.map((friend, index) => (
                        <FriendlistBox
                            key={index}
                            friend={friend}
                            onStartPrivateChat={onStartPrivateChat}
                            onAddToGroupChat={onAddToGroupChat}
                        />
                    ))}
                </ul>
            ) : (
                <p>No friends found.</p>
            )}
            <button onClick={() => setShowModal(true)}>Start Group Chat</button>

            {showModal && (
                <GroupChatCreationModal
                    friendsList={friendsList}
                    onSubmit={handleCreateGroupChat}
                    onClose={() => setShowModal(false)}
                    userName={userName}
                />
            )}
            <div className="chatroom-list">
                <h3>My Chatrooms</h3>
                {chatrooms.length > 0 ? (
                    chatrooms.map(chatroomId => (
                        <button key={chatroomId} onClick={() => openChatroom(chatroomId)}>
                            Chatroom {chatroomId}
                        </button>
                    ))
                ) : (
                    <p>You are not a member of any chats.</p>
                )}
            </div>
            <div>
                {isChatOpen && (
                    <ChatRoomComponent
                        friend={selectedFriend}
                        groupChatId={currentGroupId}
                        groupFriends={groupFriends}
                        onClose={() => setIsChatOpen(false)}
                        onOpenChat={handleOpenChat}
                        shouldOpenChat={shouldOpenChat}
                    />
                )}
            </div>
        </div>
    );
};

export default FriendlistComponent;
