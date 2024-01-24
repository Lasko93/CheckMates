import React, {useState} from "react";
import './RequestComponent.css';
import RequestData from "../../components/RequestComponent/RequestData"
import {useAcceptFriendRequest, useDeclineFriendRequest, useFetchAllFriendRequests} from "../../services/FriendService";


const RequestComponent = ({ userName, text }) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const { data: friendsData = [], isLoading, isError, refetch } = useFetchAllFriendRequests(userName);
    const acceptMutation = useAcceptFriendRequest();
    const declineMutation = useDeclineFriendRequest();

    if (isLoading) {
        return <div>Loading...</div>; // Ladeanzeige
    }

    if (isError) {
        return <div>Error loading friend requests</div>; // Fehleranzeige
    }
    const acceptRequest = async (username) => {
        await acceptMutation.mutateAsync({ user: userName, yesFriend: username });
        refetch();
    };

    const declineRequest = async (username) => {
        await declineMutation.mutateAsync({ receiver: userName, noFriend: username });
        refetch();
    };

    const showModal = () => {
        setIsModalOpen(true);
    };

    const hideModal = () => {
        setIsModalOpen(false);
    };

    return (
        <div>
            <div className="button2">
                <button className="GeneralButton" onClick={showModal}>{text}</button>
                <div className={`modal ${isModalOpen ? 'modal-open' : ''}`}>
                    {isModalOpen && (
                        <div className="modal-content">
                            {friendsData.map((friend, index) => (
                                <RequestData
                                    key={index}
                                    friend={friend}
                                    rejectRequest={() => declineRequest(friend.username)}
                                    acceptRequest={() => acceptRequest(friend.username)}
                                />
                            ))}
                            <button className="GeneralButton" onClick={hideModal}>Close</button>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default RequestComponent;