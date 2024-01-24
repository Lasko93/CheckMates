import React, {useState} from 'react';

const GroupChatCreationModal = ({ friendsList, onSubmit, onClose }) => {
    const [selectedFriends, setSelectedFriends] = useState([]);

    const handleSelectFriend = (friend) => {
        if (selectedFriends.includes(friend)) {
            setSelectedFriends(selectedFriends.filter(f => f !== friend));
        } else {
            setSelectedFriends([...selectedFriends, friend]);
        }
    };

    const isFriendSelected = (friend) => {
        return selectedFriends.includes(friend);
    };

    const handleSubmit = () => {
        onSubmit(selectedFriends);
        onClose();
    };

    return (
        <div className="modal">
            <h2>Select Friends for Group Chat</h2>
            {friendsList && friendsList.length > 0 ? (
                <ul>
                    {friendsList.map(friend => (
                        <li key={friend.userName}>
                            <span>{friend.userName}</span>
                            <button onClick={() => handleSelectFriend(friend)}>
                                {isFriendSelected(friend) ? 'Selected' : '+'}
                            </button>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No friends found.</p>
            )}
            <button onClick={handleSubmit}>Create Group Chat</button>
            <button onClick={onClose}>Cancel</button>
        </div>
    );
};

export default GroupChatCreationModal;
