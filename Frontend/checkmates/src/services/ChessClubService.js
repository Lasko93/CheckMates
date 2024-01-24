import {useMutation, useQuery, useQueryClient} from 'react-query';
import config from '../config';
import {toast} from 'react-toastify';

// Fetch all clubs
const fetchAllClubs = async () => {
    const response = await fetch(`${config.apiUrl}/api/v1/club/`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Fetch club by name
const fetchClubByName = async (clubName) => {
    const response = await fetch(`${config.apiUrl}/club/${clubName}`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Fetch all members by club name
const fetchAllMembersByClubName = async (clubName) => {
    const response = await fetch(`${config.apiUrl}/club/${clubName}/members/`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Fetch clubs by member username
const fetchClubsByMember = async (userName) => {
    const response = await fetch(`${config.apiUrl}/api/v1/club/members/${userName}`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
};

// Join a club
const joinClub = async (clubRequest) => {
    try {
        const response = await fetch(`${config.apiUrl}/api/v1/club/join`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(clubRequest),
        });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        toast.success('Joined club successfully!');
        return data;
    } catch (error) {

    }
};

// Leave a club
const leaveClub = async (clubRequest) => {
    try {
        const response = await fetch(`${config.apiUrl}/api/v1/club/leave`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(clubRequest),
        });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        toast.success('Left club successfully!');
        return data;
    } catch (error) {

    }
};

// Custom hooks for club operations
export const useFetchAllClubs = () => useQuery('clubs', fetchAllClubs);
export const useFetchClubByName = (clubName) => useQuery(['clubs', clubName], () => fetchClubByName(clubName));
export const useFetchAllMembersByClubName = (clubName) => useQuery(['clubs', clubName], () => fetchAllMembersByClubName(clubName));
export const useFetchClubsByMember = (userName) => useQuery(['clubs', userName], () => fetchClubsByMember(userName));
export const useJoinClub = () => {
    const queryClient = useQueryClient();
    return useMutation(joinClub, {
        onSuccess: () => {
            queryClient.invalidateQueries('clubs');
        },
    });
};
export const useLeaveClub = () => {
    const queryClient = useQueryClient();
    return useMutation(leaveClub, {
        onSuccess: () => {
            queryClient.invalidateQueries('clubs');
        },
    });
};
