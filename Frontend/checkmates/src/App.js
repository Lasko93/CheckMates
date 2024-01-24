import React from 'react';
import {QueryClient, QueryClientProvider} from 'react-query';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import './App.css';
import SidebarComponent from './components/SidebarComponent';
import HomePage from './pages/HomePage/HomePage';
import MyFriendsPage from './pages/MyFriendsPage/MyFriendsPage'
import LoginPage from './pages/LoginPage/LoginPage'
import GameLobbyPage from './pages/GameLobbyPage/GameLobby'
import MyProfile from './pages/MyProfile/MyProfile'
import {FriendListProvider} from "./pages/MyFriendsPage/FriendListContext";
import FriendProfile from "./pages/FriendProfile/FriendProfile";
import LeaderboardPage from "./pages/LeaderboardPage/LeaderboardPage";
import ChessClubsPage from "./pages/ChessClub/ChessClubs";
import ChessClubPage from "./pages/ChessClub/ChessClub";
import {ToastContainer} from "react-toastify";
import ChessPuzzlePage from "./pages/ChessPuzzlePage/ChessPuzzlePage";
import PlayPuzzle from "./pages/ChessPuzzlePage/PlayPuzzle";
import PlayAgainstComputer from "./pages/PlayAgainstComputer/playAgainstComputer";
import LiveMatches from "./pages/LiveMatchPage/LiveMatches";
import ShowAgainPage from "./pages/ShowAgainPage/ShowAgainPage";
import PGN from "./pages/PGN/PGN";


const queryClient = new QueryClient();


function App() {
    return (
        <FriendListProvider>
        <div className="App">
            <QueryClientProvider client={queryClient}>
            <Router>
                <div className="content">
                    <ToastContainer
                        position="top-right"
                        autoClose={1500}
                        className="my-toast-container"
                        hideProgressBar={true}
                    />
                    <SidebarComponent />
                    <Routes>
                        <Route path="/" exact element={<HomePage />} />
                        <Route path="/friends" element={<MyFriendsPage/>} />
                        <Route path="/login" element={<LoginPage />} />
                        <Route path="/Lobby/:gameId" element={<GameLobbyPage />} />
                        <Route path="/myprofile" element={<MyProfile/>}/>
                        <Route path="/profile/:userName" element={<FriendProfile/>} />
                        <Route path="/leaderboard" element={<LeaderboardPage/>}/>
                        <Route path="/LiveMatches" element={<LiveMatches/>}/>
                        <Route path="/chesspuzzles" element={<ChessPuzzlePage/>}/>
                        <Route path="/playPuzzle/:puzzleId" element ={<PlayPuzzle/>}/>
                        <Route path="/chessClubs" element={<ChessClubsPage/>} />
                        <Route path="/chessClub/:clubId" element={<ChessClubPage/>} />
                        <Route path="/playAgainstComputer" element={<PlayAgainstComputer/>} />
                        <Route path="/showAgainPage/:gameId" element={<ShowAgainPage/>}/>
                        <Route path="/pgn" element={<PGN/>} />

                    </Routes>
                </div>
            </Router>
            </QueryClientProvider>
        </div>
        </FriendListProvider>
    );
}

export default App;

