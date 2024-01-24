import React, {useEffect, useRef, useState} from 'react';
import Form from 'react-bootstrap/Form';
import {toast, ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './PGN.css'
import {Chess} from "chess.js";
import {useNavigate} from "react-router-dom";

function PGN() {
    const navigate = useNavigate();
    const [file, setFile] = useState(null);
    const fileInputRef = useRef(null);
    const [pgnList, setPgnList] = useState([]);
    const [FenFromPGN, setFenFromPGN] = useState([]);

    const userName = sessionStorage.getItem('userName');
    console.log(userName)


    useEffect(() => {
        if (!userName) {
            toast.error('Benutzername ist nicht gesetzt!');
            return;
        }

        fetch(`http://localhost:8080/pgn/my-pgn?username=${encodeURIComponent(userName)}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error('Netzwerkantwort war nicht ok');
                }
                return response.json();
            })
            .then((data) => {
                setPgnList(data); // Directly setting the received data
            })
            .catch((error) => {
                toast.error('Fehler beim Laden der PGN-Liste!');
                console.error('Fetch error:', error);
            });
    }, [userName]);



    console.log(pgnList);
    const handleDragOver = (e) => {
        e.preventDefault();
    };

    const handleDrop = (e) => {
        e.preventDefault();
        if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
            processFile(e.dataTransfer.files[0]);
            e.dataTransfer.clearData();
        }
    };
    const handlePgnClick = (pgnData) => {
        const decodedPgn = atob(pgnData.content);
        console.log(decodedPgn);
        const chessGame = new Chess();
        chessGame.loadPgn(decodedPgn);
        console.log(chessGame.history({ verbose: true }));
        chessGame.history({ verbose: true }).map((move) => {
            FenFromPGN.push(move.before);
            if(FenFromPGN.length === chessGame.history({ verbose: true }).length){
                FenFromPGN.push(move.after);
            }
        });
        console.log(FenFromPGN);
        sessionStorage.setItem('PGNArray', JSON.stringify(FenFromPGN));
        navigate(`/ShowAgainPage/${pgnData.fileName}`);
    }

    const handleFileChange = (e) => {
        if (e.target.files && e.target.files.length > 0) {
            processFile(e.target.files[0]);
        }
    };

    const processFile = (file) => {
        setFile(file);
    };

    const handleClick = () => {
        fileInputRef.current.click();
    };

    const uploadFile = () => {
        if (!file) {
            toast.error('Bitte wähle zuerst eine Datei aus!');
            return;
        }

        // Check if a username is available
        if (!userName) {
            toast.error('Benutzername ist nicht gesetzt!');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);
        formData.append('username', userName);

        fetch('http://localhost:8080/pgn/upload', {
            method: 'POST',
            body: formData,
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Netzwerkantwort war nicht ok');
                }
                return response.json();
            })
            .then(data => {
                console.log('Datei erfolgreich hochgeladen:', data);
                toast.success('Datei erfolgreich hochgeladen!');
            })
            .catch(error => {
                console.error('Fehler beim Hochladen der Datei:', error);
                toast.error('Fehler beim Hochladen der Datei!');
            });
    };


    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '100vh' }}>
            <ToastContainer />
            <Form>
                <Form.Group controlId="formFile" className="mb-3">
                    <Form.Label onClick={handleClick}>Drag and drop your PGN file here or click to select</Form.Label>
                    <div onClick={handleClick} onDragOver={handleDragOver} onDrop={handleDrop} style={{ border: '2px dashed #ccc', padding: '20px', cursor: 'pointer' }}>
                        {file ? <p>{file.name}</p> : <p>Drop file here</p>}
                        <Form.Control type="file" ref={fileInputRef} onChange={handleFileChange} style={{ opacity: 0, position: 'absolute', zIndex: -1 }} />
                    </div>
                </Form.Group>
                <button type="button" onClick={uploadFile}>Datei hochladen</button>
            </Form>
            <div className="pgn-list">
                {pgnList.map((pgn, index) => (
                    <button
                        key={index}
                        className="pgn-button"
                        onClick={() => handlePgnClick(pgn)}
                    >
                        <span className="pgn-fileName">{pgn.fileName}</span>
                    </button>
                ))}
            </div>
        </div>
    );
}

export default PGN;