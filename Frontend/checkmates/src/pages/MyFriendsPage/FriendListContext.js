import React, {createContext, useState} from 'react';

export const FriendListContext = createContext();

export const FriendListProvider = ({ children }) => {
    const [friendsList, setFriendsList] = useState([]);

    const addToFriendsList = (newFriend) => {
        setFriendsList(prev => [...prev, newFriend]);
    };

    return (
        <FriendListContext.Provider value={{ friendsList, addToFriendsList }}>
            {children}
        </FriendListContext.Provider>
    );
};