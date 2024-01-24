import {useMutation, useQuery, useQueryClient} from 'react-query';
import {toast} from 'react-toastify';
import config from '../config';


// Funktion zum Hochladen einer CSV-Datei
const uploadPuzzles = async ({ fenNotations }) => {
    console.log("uploadPuzzles aufgerufen", fenNotations);
    try {
        const response = await fetch(`${config.apiUrl}/chesspuzzle/create-chesspuzzle`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({fenNotations}),
        });

        if (!response.ok) {
            // Versucht, die Antwort als Text zu lesen
            const errorText = await response.text();
            console.error('Error uploading puzzles:', errorText || 'Network response was not ok');
            return null;
        }

        // Überprüfen, ob der Server eine leere Antwort sendet
        if (response.status === 204 || response.headers.get('content-length') === '0') {
            console.log("Chess puzzle created successfully, but no content returned");
            return null; // Kein Inhalt, gibt null zurück
        }

        try {
            return await response.json();
        } catch (e) {
            console.warn("Response not in JSON format:", e);
            return null;
        }
    } catch (error) {
        console.error('Error uploading puzzles:', error);
        // Handle the error or rethrow as appropriate
        throw error;
    }
};

// Hilfsfunktion, um zu überprüfen, ob ein String einer gültigen FEN-Notation entspricht
const isValidFen = (fen) => {
    return typeof fen === 'string' && fen.includes('/') && (fen.includes(' w ') || fen.includes(' b '));
};
// Funktion, um Puzzles vom Backend zu holen
const fetchPuzzlesFromBackend = async () => {
    const response = await fetch(`${config.apiUrl}/chesspuzzle/find-all`);

    if (!response.ok) {
        throw new Error('Could not fetch puzzles');
    }

    const puzzlesData = await response.json();

    // Filter die Einträge, die gültige FEN-Notationen enthalten
    const validPuzzles = puzzlesData.filter(puzzle =>
        puzzle?.boardStatus?.value &&
        typeof puzzle.boardStatus.value === 'string' &&
        // Hier könnte eine genauere Überprüfung der FEN-Notation erfolgen
        puzzle?.boardStatus?.value && isValidFen(puzzle.boardStatus.value)
    );

    // Extrahiere nur die id und die FEN-Notation
    return validPuzzles.map(puzzle => ({
        id: puzzle.id,
        fen: puzzle.boardStatus.value
    }));
};


export const validateMoveWithBackend = async (fenPosition, answerString) => {
    try {
        const url = new URL(`${config.apiUrl}/stockfish/check-if-best`);
        const params = { fenPosition, answerString };
        url.search = new URLSearchParams(params).toString();

        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.status == 202) { // HTTP Status ACCEPTED
            return true;
        } else if (response.status == 418) { // HTTP Status I_AM_A_TEAPOT
            return false;
        } else {
            // Behandeln anderer Statuscodes oder genereller Fehler
            throw new Error('Unexpected response status: ' + response.status);
        }
    } catch (error) {
        console.error('Error validating move:', error);
        throw error;
    }
};

export const getBestMoveFromBackend = async (fenPosition, difficulty) => {
    try {
        const url = new URL(`${config.apiUrl}/stockfish/get-best-move`);
        const params = {fenPosition, difficulty};
        url.search = new URLSearchParams(params).toString();

        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response) {
            return await response.json();
        } else {
            // Behandeln anderer Statuscodes oder genereller Fehler
            throw new Error('Unexpected response status: ' + response.status);
        }
    }
    catch (error) {
        console.error('Error validating move:', error);
        throw error;
    }
}


// Custom Hook zum Hochladen von Puzzles
export const useUploadPuzzles = () => {
    const queryClient = useQueryClient();
    return useMutation(uploadPuzzles, {
        onSuccess: () => {
            queryClient.invalidateQueries('randomPuzzle');
            toast.success("Puzzles uploaded successfully!");
        },
        onError: (error) => {
            console.error("Error in mutation:", error);
            toast.error("Failed to upload puzzles.");
        },
    });
};

export const usePuzzlesList = () => {
    return useQuery('puzzles', fetchPuzzlesFromBackend, {
        onSuccess: (data) => {
            console.log('Puzzles fetched successfully:', data);
        },
        onError: (error) => {
            console.error('Error fetching puzzles:', error);
        }
    });
};
export const usePuzzleById = (id) => {
    return useQuery(['puzzle', id], async () => {
        if (!id) return;

        try {
            const response = await fetch(`${config.apiUrl}/chesspuzzle/find-by-id/${id}`);
            if (!response.ok) {
                console.error('Network response was not ok');
                return null;
            }
            const puzzleData = await response.json();

            // Wenn boardStatus ein Objekt mit einer value-Eigenschaft ist, verwenden Sie diese, ansonsten nehmen Sie boardStatus direkt.
            const fenNotation = typeof puzzleData.boardStatus === 'object'
                ? puzzleData.boardStatus.value
                : puzzleData.boardStatus;

            if (typeof fenNotation !== 'string') {
                console.error('FEN notation is not a string');
                return null;
            }

            return fenNotation; // Rückgabe der FEN-Notation
        } catch (error) {
            console.error('Error fetching puzzle by ID:', error);
            throw error;
        }
    }, {
        enabled: !!id, // Führe die Abfrage nur aus, wenn eine ID vorhanden ist
    });
};
