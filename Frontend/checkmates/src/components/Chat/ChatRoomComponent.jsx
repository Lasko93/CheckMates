import React, {useEffect, useState} from 'react';
import webSocketService from '../../services/WebSocketService';
import './ChatRoomComponent.css';

const ChatRoomComponent = ({ groupChatId,friend, groupFriends, onClose,onOpenChat, shouldOpenChat }) => {
    const [message, setMessage] = useState('');
    const [messages, setMessages] = useState([]);
    const [subscription, setSubscription] = useState(null);
    const [selectedMessageId, setSelectedMessageId] = useState(null);
    const currentUserUsername = sessionStorage.getItem('userName');

    useEffect(() => {
        const loadOldMessages = async () => {
            try {
                const response = await fetch(`http://localhost:8080/chat/messages/${groupChatId}?user=${currentUserUsername}`);

                if (!response.ok) {
                    throw new Error('Failed to fetch old messages');
                }
                const oldMessages = await response.json();
                console.log(oldMessages);
                setMessages(oldMessages);
                subscribeToRealTimeUpdates();
            } catch (error) {
                console.error("Error loading old messages:", error);
            }
        };

        const subscribeToRealTimeUpdates = () => {
            const chatTopic = `/topic/${groupChatId}`;
            console.log(`Subscribing to topic: ${chatTopic}`);

            const onMessageReceived = async (msg) => {
                console.log(`Message received on topic ${chatTopic}:`, msg);
                let newMessage;
                try {
                    newMessage = typeof msg.body === 'string' ? JSON.parse(msg.body) : msg.body;
                } catch (error) {
                    console.error("Error parsing message:", error);
                    return;
                }

                setMessages(prevMessages => {
                    const messageExists = prevMessages.some(prevMsg => prevMsg.id === newMessage.id);
                    return messageExists ? prevMessages : [...prevMessages, newMessage];
                });
                if (newMessage.sender !== currentUserUsername) {
                    await markMessageAsRead(newMessage.id);
                }
            };

            if (!webSocketService.isSubscribed(chatTopic)) {
                webSocketService.subscribe(chatTopic, onMessageReceived);
            }
        };
        const markMessageAsRead = async (messageId) => {
            try {
                const response = await fetch(`http://localhost:8080/chat/read/${messageId}?user=${encodeURIComponent(currentUserUsername)}`, {
                    method: 'POST',
                });


                if (!response.ok) {
                    throw new Error('Failed to mark message as read');
                }
            } catch (error) {
                console.error("Error marking message as read:", error);
            }
        };

        if (groupChatId) {
            loadOldMessages();
        }

        return () => {
            const chatTopic = `/topic/${groupChatId}`;
            webSocketService.removeSubscription(chatTopic);
        };
    }, [groupChatId, currentUserUsername]);

    // useEffect(() => {
    //     const messageUpdateTopic = "/topic/message-updates";
    //     webSocketService.subscribe(messageUpdateTopic, (message) => {
    //         const updatedMessage = message.body;
    //         setMessages(prevMessages =>
    //             prevMessages.map(msg =>
    //                 msg.id === updatedMessage.id ? updatedMessage : msg
    //             )
    //         );
    //     });
    //
    //     return () => {
    //         webSocketService.unsubscribe(messageUpdateTopic);
    //     };
    // }, []);




    const handleCloseChat = () => {
        onClose();
    };
    const handleSendMessage = () => {
        if (message.trim() !== '' && groupChatId) {
            const messageData = {
                chatId: groupChatId,
                sender: currentUserUsername,
                message: message
            };

            try {
                console.log("Sending message:", messageData);
                webSocketService.sendMessage(`/app/chat.sendMessage/${groupChatId}`, JSON.stringify(messageData));
                setMessage('');
            } catch (error) {
                console.error("Error sending message:", error);
            }
        }
    };
    // const markMessageAsSeen = async (messageId) => {
    //     try {
    //         const response = await fetch(`/api/messages/${messageId}/markSeen`, {
    //             method: 'PUT'
    //         });
    //
    //         if (response.ok) {
    //             setMessages(prevMessages => prevMessages.map(msg =>
    //                 msg.id === messageId ? { ...msg, seen: true } : msg
    //             ));
    //         } else {
    //             console.error('Failed to mark message as seen');
    //         }
    //     } catch (error) {
    //         console.error("Error marking message as seen:", error);
    //     }
    // };
    const editMessage = async (messageId) => {
        const messageToEdit = messages.find(msg => msg.id === messageId);

        if (messageToEdit && messageToEdit.sender === currentUserUsername && !messageToEdit.seen) {
            const newContent = prompt('Edit your message:', messageToEdit.message);

            if (newContent && newContent !== messageToEdit.message) {
                try {
                    const response = await fetch(`http://localhost:8080/chat/edit`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({
                            sender: currentUserUsername,
                            id: messageId,
                            edit: newContent
                        })
                    });

                    if (response.ok) {
                        const updatedMessageContent = await response.text();
                        setMessages(messages.map(msg =>
                            msg.id === messageId ? { ...msg, message: updatedMessageContent } : msg
                        ));
                    } else {
                        alert('Cannot edit message: It may have been read by others.');
                    }
                } catch (error) {
                    console.error('Error:', error);
                }
            }
        } else {
            console.log("You can't edit this message.");
        }
    };

    const deleteMessage = async (messageId) => {
        const messageToDelete = messages.find(msg => msg.id === messageId);
        if (messageToDelete && messageToDelete.sender === currentUserUsername) {
            try {
                const response = await fetch(`http://localhost:8080/chat/delete/${messageId}?user=${encodeURIComponent(currentUserUsername)}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                    }
                });

                if (response.ok) {
                    setMessages(messages.filter(msg => msg.id !== messageId));
                } else {
                    const errorData = await response.json();
                    alert(`Cannot delete message: ${errorData.message || 'Server error'}`);
                }
            } catch (error) {
                console.error('Error:', error);
            }
        } else {
            console.log("Can't delete the message, it may have been read by others.");
        }
    };

    return (
        <div className="chat-container">
            <div>
                <button onClick={onClose}>Close Chat</button>
            </div>

            <div className="messages">
                {messages.map((msg) => (
                    <div key={msg.id} className="message">
                        <span className="sender">{msg.sender}:</span>
                        <span className="text">{msg.message}</span>
                        {msg.sender === currentUserUsername && !msg.read && (
                            <div className="message-actions">
                                <button onClick={() => editMessage(msg.id)} className="edit-btn">✏️</button>
                                <button onClick={() => deleteMessage(msg.id)} className="delete-btn">🗑️</button>
                            </div>
                        )}
                    </div>
                ))}
            </div>
            <div className="message-input">
                <input type="text" value={message} onChange={(e) => setMessage(e.target.value)} />
                <button onClick={handleSendMessage}>Send</button>
            </div>
        </div>
    );
};

export default ChatRoomComponent;
