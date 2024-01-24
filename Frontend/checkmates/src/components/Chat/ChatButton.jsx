import React, {useEffect, useRef, useState} from 'react';
import './ChatButtonStyle.css';

const ChatButton = ({ gameId, currentUser }) => {
    const [isOpen, setIsOpen] = useState(true);
    const [chatType, setChatType] = useState('private'); // 'private', 'group', 'public'
    const [messages, setMessages] = useState([]);
    const [inputValue, setInputValue] = useState('');
    const ws = useRef(null);

    useEffect(() => {
        // You would need to modify this URL and mechanism to match how your server handles WebSockets
        const url = `ws://your-websocket-server.com/${chatType}/${gameId}`;
        ws.current = new WebSocket(url);

        ws.current.onmessage = (event) => {
            const message = JSON.parse(event.data);
            setMessages((prevMessages) => [...prevMessages, message]);
        };

        ws.current.onclose = () => {
            console.log(`WebSocket disconnected from the ${chatType} chat`);
        };

        return () => {
            ws.current.close();
        };
    }, [chatType, gameId]);

    const toggleChat = () => {
        setIsOpen(!isOpen);
    };

    const sendMessage = () => {
        if (!inputValue.trim()) return;
        const message = {
            type: 'message',
            content: inputValue,
            sender: currentUser,
            chatType: chatType,
            timestamp: new Date().toISOString()
        };
        ws.current.send(JSON.stringify(message));
        setInputValue('');
    };

    const handleChange = (e) => {
        setInputValue(e.target.value);
    };
    const handleEditRequest = (messageId) => {
        // Find the message by id and set it to edit mode
        // You might also need to inform the backend here
    };

    const handleDelete = (messageId) => {
        // Remove the message from the state and inform the backend
        // The backend should then inform other clients that the message has been deleted
    };
    // ChatMessage.jsx
    const ChatMessage = ({ msg, isCurrentUser }) => (
        <div className={`message ${isCurrentUser ? 'sent' : 'received'}`}>
            <span className="sender-name">{msg.sender}</span>
            <span className="message-content">{msg.content}</span>
        </div>
    );

    return (
        <div className="chat-container">
            <button className="chat-toggle" onClick={toggleChat}>
                {isOpen ? 'Close Chat' : 'Chat'}
            </button>
            {isOpen && (
                <div className="chat-box">
                    <div className="chat-messages">
                        {messages.map((msg) => (
                            <ChatMessage
                                key={msg.id}
                                msg={msg}
                                isCurrentUser={msg.sender === currentUser}
                                onEditRequest={handleEditRequest}
                                onDelete={handleDelete}
                            />
                        ))}
                    </div>
                    <input
                        type="text"
                        className="chat-input"
                        placeholder="Type a message..."
                        value={inputValue}
                        onChange={handleChange}
                    />
                    <button className="send-button" onClick={sendMessage}>Send</button>
                </div>
            )}
        </div>
    );
};

export default ChatButton;
