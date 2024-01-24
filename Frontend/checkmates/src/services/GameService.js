import {useMutation, useQuery, useQueryClient} from 'react-query';
import config from '../config';
import {toast} from "react-toastify";


// Fetch all games
const fetchGames = async () => {
    const response = await fetch(`${config.apiUrl}/game/find-all`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Fetch a game by ID
const fetchGameById = async (gameId) => {
    if (gameId === undefined || gameId === null || isNaN(gameId)) {
        return null;
    }
    const response = await fetch(`${config.apiUrl}/game/find-by-id/${gameId}`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Create a new game
const createGame = async ({ username, gameName, timer }) => {
    const response = await fetch(`${config.apiUrl}/game/create-game`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: username,
            gameName: gameName,
            timer: timer
        })
    });

    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};
//remove Black from game
const updateGameRemoveBlack = async (gameId) => {
    const response = await fetch(`${config.apiUrl}/game/update-game-remove-black/${gameId}`, { method: 'PUT' });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

const updateTime = async ({gameId, time }) => {
    try {

        const url = new URL(`${config.apiUrl}/game/update-time/${gameId}?time=${time}`);

        const response = await fetch(url, { method: 'PUT' });

        // Check if the response is okay
        if (!response.ok) {
            // Extract more detailed error information if available
            const errorDetail = await response.text();
            throw new Error(`Network response was not ok: ${errorDetail}`);
        }

        // Parse and return the response data
        return await response.json();
    } catch (error) {
        // Handle errors
        toast.error(`Error: ${error.message}`);
    }
};




// Start a game by ID
const startGame = async (gameId) => {
    const response = await fetch(`${config.apiUrl}/game/start-game/${gameId}`, { method: 'POST' });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Pause a game by ID
const pauseGame = async (gameId) => {
    const response = await fetch(`${config.apiUrl}/game/pause-game/${gameId}`, { method: 'POST' });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Finish a game by ID
const finishGame = async (gameId) => {
    const response = await fetch(`${config.apiUrl}/game/finish-game/${gameId}`, { method: 'POST' });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Delete a game by ID
const deleteGame = async (gameId) => {
    const response = await fetch(`${config.apiUrl}/game/delete-by-id/${gameId}`, { method: 'DELETE' });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
};

// Get Assistance by Bot

export const getAssistance = async (fenPosition, difficulty, gameId, color) => {
    try {
        const url = new URL(`${config.apiUrl}/stockfish/get-assistance`);
        const params = {fenPosition, difficulty, gameId, color};
        url.search = new URLSearchParams(params).toString();

        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.status === 406) {
            toast.error("You used all your assistance for this game!");
        }

        if (response) {
            return await response.json();
        }
        else {
            throw new Error('Unexpected response status: ' + response.status);
        }
    }
    catch (error) {

    }

}

// Custom hook to use the fetchGames function
export const useFetchGames = () => useQuery('games', fetchGames);

// Custom hook to use the fetchGameById function
export const useFetchGameById = (gameId) => useQuery(
    ['games', gameId],
    () => fetchGameById(gameId),

);

// Custom hook to use the createGame function
export const useCreateGame = () => {
    const queryClient = useQueryClient();
    return useMutation(createGame, {
        onSuccess: () => {
            queryClient.invalidateQueries('games');
        },
    });
};

// Custom hook to use the startGame function
export const useStartGame = () => {
    const queryClient = useQueryClient();
    return useMutation(startGame, {
        onSuccess: () => {
            queryClient.invalidateQueries('games');
        },
    });
};

// Custom hook to use the pauseGame function
export const usePauseGame = () => {
    const queryClient = useQueryClient();
    return useMutation(pauseGame, {
        onSuccess: () => {
            queryClient.invalidateQueries('games');
        },
    });
};

// Custom hook to use the finishGame function
export const useFinishGame = () => {
    const queryClient = useQueryClient();
    return useMutation(finishGame, {
        onSuccess: () => {
            queryClient.invalidateQueries('games');
        },
    });
};

// Export the function
export const useUpdateGameRemoveBlack = () => {
    const queryClient = useQueryClient();
    return useMutation(updateGameRemoveBlack, {
        onSuccess: () => {
            queryClient.invalidateQueries('games');
        },
    });
};

// Custom hook to use the deleteGame function
export const useDeleteGame = () => {
    const queryClient = useQueryClient();
    return useMutation(deleteGame, {
        onSuccess: () => {
            queryClient.invalidateQueries('games');
        },
    });
};

export const checkUserInGamesAndGetId = async (username) => {
    const gamesAsWhite = await fetch(`${config.apiUrl}/game/find-game-white-username/${username}`);
    const gamesAsBlack = await fetch(`${config.apiUrl}/game/find-game-black-username/${username}`);

    const whiteGames = await gamesAsWhite.json();
    const blackGames = await gamesAsBlack.json();

    const activeGames = [...whiteGames, ...blackGames];

    return activeGames.length > 0 ? activeGames[0].id : null;
};

export const useUpdateTime = () => {
    const queryClient = useQueryClient();
    return useMutation(updateTime, {
        onSuccess: () => {
            queryClient.invalidateQueries('game');
        },
    });
};

