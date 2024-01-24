import React, {useEffect, useState} from "react";
import "./CustomRequest.css"
import {useNavigate} from 'react-router-dom';
import PlayerRequestComponent from "./PlayerRequestComponent";
import {
    useAcceptGameRequest,
    useDeclineGameRequest,
    useFetchPendingGameRequestsByBlackUsername
} from "../../../services/GameRequestService";
import {toast} from "react-toastify";
import {
    useAcceptFriendRequest,
    useDeleteFriendRequest,
    useFetchAllFriendRequests
} from "../../../services/FriendService";
import {checkUserInGamesAndGetId, useDeleteGame, useFetchGameById} from "../../../services/GameService";

const CustomRequest = ({UserWithRequests}) => {


    const inviteGameText = "has invited you to a match!";
    const inviteFriendText ="wants to be your friend!";

    const navigate = useNavigate();

    const { mutate: deleteGame } = useDeleteGame();

    //Getting all pending game requests
    const { data: pendingRequests, isLoading, isError, error } = useFetchPendingGameRequestsByBlackUsername(UserWithRequests);
    const declineMutation = useDeclineGameRequest();
    const acceptMutation = useAcceptGameRequest();

    //Getting all friend requests
    const { data: allFriendRequests, isLoading: isLoadingFriends, isError: isErrorFriends, error: errorFriends } = useFetchAllFriendRequests(UserWithRequests);
    const acceptFriendMutation = useAcceptFriendRequest();
    const declineFriendMutation = useDeleteFriendRequest();


    const [activeGameId, setActiveGameId] = useState(null);
    const { data: gameData, isLoading: isGameLoading } = useFetchGameById(activeGameId)

    useEffect(() => {
        const fetchGameId = async () => {
            const gameId = await checkUserInGamesAndGetId(UserWithRequests);
            setActiveGameId(gameId);
        };

        fetchGameId();
    }, [UserWithRequests]);


/*
       // TODo handling loadings
    if (isLoadingFriends) {
        return <div>Loading game requests...</div>;
    }

    if (isErrorFriends) {
        return <div>Error fetching game requests: {error.message}</div>;
    }

*/

    const handleDeleteGame = async () => {
        if (activeGameId && gameData?.white?.username === UserWithRequests) {
            return new Promise((resolve, reject) => {
                if (gameData?.white?.username === UserWithRequests) {
                        deleteGame(activeGameId, {
                            onSuccess: () => {
                                console.log('Game deleted successfully');
                                resolve();
                            },
                            onError: (error) => {
                                console.error('Failed to delete the game', error);
                                reject(error);
                            }
                        });


                }

            });
        }
    };

    return(
        <div className="RequestContainer" >
            {pendingRequests && pendingRequests.map(request => (
                <PlayerRequestComponent
                    key={request.id}
                    InviteText={inviteGameText}
                    InvitingPlayer={request.white?.username}
                    acceptButton={async () => {
                        try {
                            //First Delete the game before accepting the request
                            await handleDeleteGame();

                            acceptMutation.mutate(
                                { requestId: request.id, blackUsername: UserWithRequests },
                                {
                                    onSuccess: () => {
                                        navigate(`/lobby/${request?.game?.id}`);

                                    },
                                    onError: (error) => {
                                        toast.error("Lobby full, closed or game started");
                                        navigate(`/`);
                                    }
                                }
                            );
                        } catch (error) {
                            toast.error("Error handling the game deletion");
                            navigate(`/`);
                        }
                    }}
                    declineButton={() => declineMutation.mutate(
                        {requestId: request.id, blackUsername: UserWithRequests }

                        )}
                />
            ))}
            {allFriendRequests && allFriendRequests?.map(request => (
                <PlayerRequestComponent
                    key={request?.id}
                    InviteText={inviteFriendText}
                    InvitingPlayer={request?.sender}
                    acceptButton={() => acceptFriendMutation.mutate(
                        { user: request?.receiver, yesFriend: request?.sender }
                    )}
                    declineButton={() => declineFriendMutation.mutate(
                        { receiver: request?.receiver, noFriend: request?.sender }
                    )}
                />
            ))}
        </div>

    );
}


export default CustomRequest;