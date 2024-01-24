import {useMutation, useQuery, useQueryClient} from 'react-query';
import config from "../config";


//Fetch profile picture

const fetchProfilePicture = async (userName) => {
    if (userName === undefined) {
        return null;
    }
    const response = await fetch(`${config.apiUrl}/api/v1/photos/${userName}`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
}

// Fetch all users
const fetchUsers = async () => {
    const response = await fetch(`${config.apiUrl}/api/v1/users/`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Fetch a specific user by userName
const fetchUser = async (userName) => {
    const response = await fetch(`${config.apiUrl}/api/v1/users/${userName}`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Add a new user
const postUser = async (newUser) => {
    const response = await fetch(`${config.apiUrl}/api/v1/users/`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(newUser),
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Delete a user by userName
const deleteUser = async (userName) => {
    const response = await fetch(`${config.apiUrl}/api/v1/users/${userName}`, {
        method: 'DELETE',
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
};


//Custom hook to use the fetchProfilePicture function
export const useFetchProfilePicture = (userName) => useQuery(['profilePicture', userName], () => fetchProfilePicture(userName));

// Custom hook to use the fetchUsers function
export const useFetchUsers = () => useQuery('users', fetchUsers);

// Custom hook to use the fetchUser function
export const useFetchUser = (userName) => useQuery(['user', userName], () => fetchUser(userName));

// Custom hook to use the postUser function
export const usePostUser = () => {
    const queryClient = useQueryClient();
    return useMutation(postUser, {
        onSuccess: () => {
            queryClient.invalidateQueries('users');
        },
    });
};

// Custom hook to use the deleteUser function
export const useDeleteUser = () => {
    const queryClient = useQueryClient();
    return useMutation(deleteUser, {
        onSuccess: () => {
            queryClient.invalidateQueries('users');
        },
    });
};
