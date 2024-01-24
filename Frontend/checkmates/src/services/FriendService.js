import {useMutation, useQuery, useQueryClient} from 'react-query';
import config from "../config";
import {toast} from "react-toastify";

// Function to change friendlist visibility
const changeFriendlistVisibility = async (appUser, newVisibility) => {
    try {
        const url = `${config.apiUrl}/friend/change-friendlist-visibility`;
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `appUser=${encodeURIComponent(appUser)}&newVisibility=${newVisibility}`,
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        return response.json();
    } catch (error) {
        console.error('Error changing friendlist visibility:', error);
    }
};




// Function to send a friend request
const sendFriendRequest = async ({ sender, soonFriend }) => {
    const response = await fetch(`${config.apiUrl}/friend/send-friendrequest/${soonFriend}?sender=${encodeURIComponent(sender)}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};


// Function to accept a friend request
const acceptFriendRequest = async ({ user, yesFriend }) => {
    try {
        const url = new URL(`${config.apiUrl}/friend/accept-friendrequest/${yesFriend}`);
        url.searchParams.append('user', user);

        const response = await fetch(url, { method: 'POST' });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        return data;
    } catch (error) {
    }
};



// Function to get all friends
const fetchAllFriends = async (appUser) => {
    const response = await fetch(`${config.apiUrl}/friend/friendlist?user=${appUser}`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Function to get all friend requests
const fetchAllFriendRequests = async (appUser) => {
    const url = new URL(`${config.apiUrl}/friend/all-friendrequests`);

    url.searchParams.append('user', appUser);

    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        return data;
    } catch (error) {

    }
};


// Function to get all sent friend requests
const fetchAllSentFriendRequests = async (appUser) => {
    const response = await fetch(`${config.apiUrl}/friend/all-sent-friendrequests?user=${appUser}`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Function to delete a friend
const deleteFriend = async ({ appUser, exFriend }) => {
    const response = await fetch(`${config.apiUrl}/friend/delete-friend/${exFriend}`, {
        method: 'DELETE',
        body: JSON.stringify({ appUser }),
        headers: {
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
};

// Function to decline a friend request
const declineFriendRequest = async ({ receiver, noFriend }) => {
    const response = await fetch(`${config.apiUrl}/friend/decline-friendrequest/${noFriend}`, {
        method: 'DELETE',
        body: JSON.stringify({ receiver }),
        headers: {
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
};

// Function to delete a friend request
const deleteFriendRequest = async ({ receiver, noFriend }) => {
    const url = new URL(`${config.apiUrl}/friend/decline-friendrequest/${noFriend}`);
    url.searchParams.append('receiver', receiver);


    const response = await fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }


};


// Custom hook to use the changeFriendlistVisibility function
export const useChangeFriendlistVisibility = ({ onSuccess }) => {
    const queryClient = useQueryClient();
    return useMutation(({ appUser, newVisibility }) => changeFriendlistVisibility(appUser, newVisibility), {
        onSuccess: () => {
            toast.success("Friendlist visibility changed successfully!");
            if (onSuccess) onSuccess();
        },
    });
};


// Custom hook to use the sendFriendRequest function
export const useSendFriendRequest = () => {
    const queryClient = useQueryClient();
    return useMutation(sendFriendRequest, {
        onSuccess: () => {
            toast.success("Friend request sent successfully!");
            queryClient.invalidateQueries('allFriendRequests');
        },
        onError: (error) => {
            toast.error("User doesnt exist or already sent a friend request to this user");
        }
    });
};

// Custom hook to use the acceptFriendRequest function
export const useAcceptFriendRequest = () => {
    const queryClient = useQueryClient();
    return useMutation(acceptFriendRequest, {
        onSuccess: () => {
            toast.success("Friend request accepted!");
            queryClient.invalidateQueries(['allFriendRequests']);
        },
        onError: (error) => {
            toast.error(`Error: ${error.message}`);
        }
    });
};


// Custom hook to use the fetchAllFriends function
export const useFetchAllFriends = (appUser) => useQuery(['allFriends', appUser], () => fetchAllFriends(appUser));

// Custom hook to use the fetchAllFriendRequests function
export const useFetchAllFriendRequests = (appUser) => useQuery(['allFriendRequests', appUser], () => fetchAllFriendRequests(appUser));

// Custom hook to use the fetchAllSentFriendRequests function
export const useFetchAllSentFriendRequests = (appUser) => useQuery(['allSentFriendRequests', appUser], () => fetchAllSentFriendRequests(appUser));

// Custom hook to use the deleteFriend function
export const useDeleteFriend = () => {
    const queryClient = useQueryClient();
    return useMutation(deleteFriend, {
        onSuccess: () => {
            toast.success("Friend deleted successfully!");
            queryClient.invalidateQueries('allFriendRequests');
        },
        onError: (error) => {
            toast.error(`Error: ${error.message}`);
        }
    });
};

// Custom hook to use the declineFriendRequest function
export const useDeclineFriendRequest = () => {
    const queryClient = useQueryClient();
    return useMutation(declineFriendRequest, {
        onSuccess: () => {
            toast.success("Friend request declined!");
            queryClient.invalidateQueries('allFriendRequests');
        },
        onError: (error) => {
            toast.error(`Error: ${error.message}`);
        }
    });
};

// Custom hook to use the deleteFriendRequest function
export const useDeleteFriendRequest = () => {
    const queryClient = useQueryClient();
    return useMutation(deleteFriendRequest, {
        onSuccess: () => {
            toast.success("Friend request deleted!");
            queryClient.invalidateQueries('allFriendRequests');
        },
        onError: (error) => {
            toast.error(`Error: ${error.message}`);
        }
    });
};

export default {
    useChangeFriendlistVisibility,
    useSendFriendRequest,
    useAcceptFriendRequest,
    useFetchAllFriends,
    useFetchAllFriendRequests,
    useFetchAllSentFriendRequests,
    useDeleteFriend,
    useDeclineFriendRequest,
    useDeleteFriendRequest

}