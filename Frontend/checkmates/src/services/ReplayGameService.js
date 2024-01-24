const ReplayGameService = {
    fetchGameById: async (gameId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/game/history/${gameId}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            console.log('DAta:',data);
            return data;
        } catch (error) {
            console.error('Error fetching game by ID:', error);
            throw error;
        }
    },
};
export default ReplayGameService;