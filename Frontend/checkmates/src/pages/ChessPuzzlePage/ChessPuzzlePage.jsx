import React, {useEffect, useRef, useState} from 'react';
import './ChessPuzzlePage.css';
import PuzzleIcon from '../../assets/images/puzzle_icon.svg';
import Chessboard from '../../assets/images/chessboard.svg';
import ButtonComponent from "../../components/Buttons/ButtonComponent";
import {useNavigate} from "react-router-dom";
import {usePuzzlesList, useUploadPuzzles,} from "../../services/ChessPuzzleService";
import PuzzleListComponent from "../../components/PuzzleListComponent/PuzzleListComponent";


const ChessPuzzlePage = () => {
    const navigate = useNavigate();
    const fileInputRef = useRef(null);
    const { mutate: uploadPuzzles } = useUploadPuzzles();
    const { data: puzzlesData, refetch: refetchPuzzlesList } = usePuzzlesList();
    const [localPuzzles, setLocalPuzzles] = useState([]);

// Effekt, der beim Laden der Komponente und der Aktualisierung von puzzlesData ausgeführt wird.
    useEffect(() => {
        if (puzzlesData) {
            setLocalPuzzles(puzzlesData);
        }
    }, [puzzlesData]);

// Funktion, die aufgerufen wird, wenn der Upload-Button geklickt wird.
    const handleButtonClick = () => {
        fileInputRef.current.click();
    };

// Funktion zum Verarbeiten des Inhalts einer CSV-Datei.
    // Extrahiert FEN-Notationen zwischen dem ersten und zweiten Komma jeder Zeile.
    const processCSV = (text) => {
        const lines = text.split('\n').slice(1); // Überspringt die erste Zeile & Teilt den Text in Zeilen
        const fenNotations = lines.map(line => {
            const parts = line.split(','); // Teilt jede Zeile an Kommas
            //  FEN-Notation ist das zweite Element (Index 1)
            return parts[1] ? parts[1].trim() : '';
        }).filter(fen => fen);// Filtert leere oder ungültige Zeilen aus
        console.log("Extrahierte FEN-Notationen: ", fenNotations); // Log für Debugging
        return fenNotations.join(','); // Verbindet alle FEN-Notationen zu einem String

    };

    // Funktion, die aufgerufen wird, wenn eine Datei ausgewählt wird.
    // Liest die Datei und verarbeitet ihren Inhalt
    const handleFileChange = async (event) => {
        const file = event.target.files[0];// Die ausgewählte Datei
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                const text = e.target.result;// Inhalt der Datei als Text
                const fenNotations = processCSV(text);// Verarbeitet den Text
                console.log("Zum Upload vorbereitete FEN-Notationen: ", fenNotations); // Log für Debugging
                uploadPuzzles({fenNotations}, {
                    onSuccess: () => {
                        // Neuladen der Seite nach erfolgreichem Hochladen
                        window.location.reload();
                    },
                    onError: (error) => {
                        // Fehlerbehandlung, falls der Upload fehlschlägt
                        console.error("Fehler beim Hochladen: ", error);
                    }
                });
            };
            reader.readAsText(file);// Liest die Datei als Text
        }
    };

    const navigateToPlayPuzzles = (id) => {
        navigate(`/playPuzzle/${id}`); // Nutzt die ID, um zur PlayPuzzle-Seite zu navigieren
    };
    return (
        <div className="ChessPuzzle">
            <div className="ChessPuzzleHeader">
                <img className="PuzzleIcon" src={PuzzleIcon} alt="Puzzle Icon"/>
                <h1>Chess Puzzles</h1>
            </div>
            <input
                type="file"
                style={{ display: 'none' }}
                ref={fileInputRef}
                onChange={handleFileChange}
                accept=".csv"
            />

            <ButtonComponent
                specificClassName="UploadButton"
                text="Upload your own puzzle (CSV file)"
                buttonID="UploadCSV"
                onClick={handleButtonClick}
            />
            <div className="PuzzleText">
                Click on a file to play a chess puzzle!
            </div>

            <div className="ChessBoardClick" >
                <img className="Chessboard" src={Chessboard} alt="Chessboard"/>
            </div>
            <br/>

            {puzzlesData && <PuzzleListComponent puzzles={puzzlesData} onPuzzleClick={navigateToPlayPuzzles} />}



        </div>
    );
}

export default ChessPuzzlePage;
