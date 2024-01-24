import {useMutation, useQuery, useQueryClient} from 'react-query';
import config from "../config";
import {toast} from "react-toastify";


// Fetch all game requests
const fetchGameRequests = async () => {
    const response = await fetch(`${config.apiUrl}/game-request/find-all`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Fetch game requests by white user's username
const fetchGameRequestsByWhiteUsername = async (username) => {
    const response = await fetch(`${config.apiUrl}/game-request/find-game-request-white-id/${username}`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Fetch game requests by black user's username
const fetchGameRequestsByBlackUsername = async (username) => {
    const response = await fetch(`${config.apiUrl}/game-request/find-game-request-black-id/${username}`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Fetch pending game requests by black user's username
const fetchPendingGameRequestsByBlackUsername = async (username) => {
    const response = await fetch(`${config.apiUrl}/game-request/find-game-request-black-pending/${username}`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const data = await response.json();
    if (data === null || (Array.isArray(data) && data.length === 0)) {
        console.log('No pending game requests found for', username);
        return [];
    }
    return data;
};

// Fetch a game request by ID
const fetchGameRequestById = async (id) => {
    const response = await fetch(`${config.apiUrl}/game-request/find-by-id/${id}`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};


// Create a new game request
const createGameRequest = async ({ whiteId, blackId, gameId }) => {
    const url = new URL(`${config.apiUrl}/game-request/create-game-request`);

    url.searchParams.append('whiteId', whiteId);
    url.searchParams.append('blackId', blackId);
    url.searchParams.append('gameId', gameId);

    try {
        const response = await fetch(url, { method: 'POST' });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        toast.success("Game request created successfully!");
        return data;
    } catch (error) {
        toast.error("Invite already sent!");
    }
};


// Accept a game request
const acceptGameRequest = async ({ requestId, blackUsername }) => {
    try {
        const url = new URL(`${config.apiUrl}/game-request/accept-game-request/${requestId}`);
        url.searchParams.append('blackUsername', blackUsername);

        const response = await fetch(url, { method: 'POST' });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        toast.success("Game request accepted!");
        return data;
    } catch (error) {
        toast.error(`Error: ${error.message}`);
    }
};


// Decline a game request
const declineGameRequest = async ({ requestId, blackUsername }) => {
    try {
        const url = new URL(`${config.apiUrl}/game-request/decline-game-request/${requestId}`);
        url.searchParams.append('blackUsername', blackUsername);

        const response = await fetch(url, { method: 'POST' });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        toast.success("Game request declined!");
        return data;
    } catch (error) {
        toast.error(`Error: ${error.message}`);
    }
};



// Delete a game request by ID
const deleteGameRequest = async (id) => {
    const response = await fetch(`${config.apiUrl}/game-request/delete-by-id/${id}`, { method: 'DELETE' });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
};

// Custom hook to use the fetchGameRequests function
export const useFetchGameRequests = () => useQuery('gameRequests', fetchGameRequests);

// Custom hooks to use the fetchGameRequestsBy*Username functions
export const useFetchGameRequestsByWhiteUsername = (username) => useQuery(['gameRequests', 'white', username], () => fetchGameRequestsByWhiteUsername(username));
export const useFetchGameRequestsByBlackUsername = (username) => useQuery(['gameRequests', 'black', username], () => fetchGameRequestsByBlackUsername(username));
export const useFetchPendingGameRequestsByBlackUsername = (username) => {
    return useQuery(
        ['gameRequests', 'black', 'pending', username],
        () => fetchPendingGameRequestsByBlackUsername(username)
    );
};
// Custom hook to use the fetchGameRequestById function
export const useFetchGameRequestById = (id) => useQuery(['gameRequest', id], () => fetchGameRequestById(id));

// Custom hook to use the createGameRequest function
export const useCreateGameRequest = () => {
    const queryClient = useQueryClient();
    return useMutation(createGameRequest, {
        onSuccess: () => {
            queryClient.invalidateQueries('gameRequests');
        },
    });
};

// Custom hook to use the acceptGameRequest function
export const useAcceptGameRequest = () => {
    const queryClient = useQueryClient();
    return useMutation(acceptGameRequest, {
        onSuccess: () => {
            queryClient.invalidateQueries('gameRequests');
        },
    });
};

// Custom hook to use the declineGameRequest function
export const useDeclineGameRequest = () => {
    const queryClient = useQueryClient();
    return useMutation(declineGameRequest, {
        onSuccess: () => {
            queryClient.invalidateQueries('gameRequests');
        },
    });
};

// Custom hook to use the deleteGameRequest function
export const useDeleteGameRequest = () => {
    const queryClient = useQueryClient();
    return useMutation(deleteGameRequest, {
        onSuccess: () => {
            queryClient.invalidateQueries('gameRequests');
        },
    });
};
