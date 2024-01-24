export const updateExpertTrophy = (userName, puzzleId) => {
    // Laden der gelösten Puzzles aus dem localStorage
    let solvedPuzzles = JSON.parse(localStorage.getItem('solvedPuzzles')) || {};

    // Sicherstellen, dass solvedPuzzles[userName] ein Array ist
    if (!Array.isArray(solvedPuzzles[userName])) {
        solvedPuzzles[userName] = [];
    }

    // Hinzufügen der Puzzle-ID zum Array, wenn sie noch nicht vorhanden ist
    if (!solvedPuzzles[userName].includes(puzzleId)) {
        solvedPuzzles[userName].push(puzzleId);
    }

    // Speichern der aktualisierten Informationen im localStorage
    localStorage.setItem('solvedPuzzles', JSON.stringify(solvedPuzzles));

    // Überprüfen, ob der Benutzer 3 oder mehr Puzzles gelöst hat
    if (solvedPuzzles[userName].length >= 3) {
        // Setzen des Expertenstatus
        setExpertStatus(userName);
        console.log('EXPERTE');
    }
};

const setExpertStatus = (userName) => {
    // Speichern des Expertenstatus im localStorage
    let expertUsers = JSON.parse(localStorage.getItem('expertUsers')) || [];
    if (!expertUsers.includes(userName)) {
        expertUsers.push(userName);
    }
    localStorage.setItem('expertUsers', JSON.stringify(expertUsers));

    // Benachrichtigung anzeigen
    console.log(`User ${userName} hat den Expertenstatus erreicht.`);
};

// Hilfsfunktion, um zu überprüfen, ob der Benutzer ein Experte ist
export const isUserExpert = (userName) => {
    let expertUsers = JSON.parse(localStorage.getItem('expertUsers')) || [];
    return expertUsers.includes(userName);
};