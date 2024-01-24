import React, {useState} from 'react';
import './SidebarComponent.css';
import CheckMatesLogo from '../../assets/images/CheckMatesLogo.svg';
import PlayIcon from '../../assets/images/Play_Icon.svg';
import PuzzleIcon from '../../assets/images/puzzle_icon.svg';
import SocialIcon from '../../assets/images/social_icon.svg';
import FriendsIcon from '../../assets/images/friends_icon.svg';
import ChessClubIcon from '../../assets/images/chessclub_icon.svg';
import PlayerIcon from '../../assets/images/PlayerIcon.svg';
import CreateLobby from '../../assets/images/create_lobby.svg';
import Matchmaking from '../../assets/images/matchmaking_icon.svg';
import SearchForLobby from '../../assets/images/search_for_lobby_icon.svg';
import SearchForPlayer from '../../assets/images/search_for_player_icon.svg';
import LeaderBoard from '../../assets/images/leaderboard_icon.svg';
import SidebarButton from './SidebarButton';
import ButtonComponent from '../../components/Buttons/ButtonComponent';
import SubSidebarButton from './SubSidebarButton'
import CreateLobbyPopup from "../LobbyComponents/CreateLobbyComponent/CreateLobbyPopup";
import {useNavigate} from "react-router-dom";
import {checkUserInGamesAndGetId} from "../../services/GameService";
import CheckActiveMatchComponent from "../CheckActiveMatchComponent/CheckActiveMatchComponent";

import Modal from 'react-modal';
import Login from '../../pages/User/Login';
import Register from '../../pages/User/Register';
import SearchForFriendPopup from "../SearchForFriendComponent/SearchForFriendPopup";
import {toast} from "react-toastify";

Modal.setAppElement('#root');

const SidebarComponent = () => {

    const navigate = useNavigate();
    const username = sessionStorage.getItem('userName');
    const [hoveredButton, setHoveredButton] = useState(null);
    const [isLoginModalOpen, setIsLoginModalOpen] = useState(false);
    const [isRegisterModalOpen, setIsRegisterModalOpen] = useState(false);


    const [isLoggedIn, setIsLoggedIn] = useState(false);

    const handleLoginSuccess = () => {
        setIsLoggedIn(true);
        navigate('/')
        window.location.reload();
        closeLoginModal();
    };


    const handleLogout = () => {
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('userName');
        setIsLoggedIn(false);
        navigate('/')
        window.location.reload();
    };

    const openLoginModal = () => {
        console.log('Opening login modal');
        setIsLoginModalOpen(true);
        setIsRegisterModalOpen(false);
    };

    const closeLoginModal = () => setIsLoginModalOpen(false);

    const openRegisterModal = () => {
        setIsRegisterModalOpen(true) ;
        setIsLoginModalOpen(false);
    }
    const closeRegisterModal = () => setIsRegisterModalOpen(false);

    const handleMouseEnter = (buttonName) => {
        setHoveredButton(buttonName);
    };

    const handleMouseLeave = () => {
        setHoveredButton(null);
    };

    //Toggling Popup for Create Lobby
    const [lobbyPopup, setLobbyPopup] = useState(false);

    const toggleCreateLobbyPopup = () => {
        setLobbyPopup(!lobbyPopup);
    };

    const [findFriendPopup, setfindFriendPopup] = useState(false);

    const togglefindFriendPopup = () => {
        if (!username) {
            toast.error('Please log in to add friends');
            return;
        }
        setfindFriendPopup(!findFriendPopup);
    };

    const [ActiveMatchPopup, setActiveMatchPopup] = useState(false);

    const toggleActiveMatchPopup = () => {
        setActiveMatchPopup(!ActiveMatchPopup)
    };

    const navigateToProfile = () => {

        if (username) {
            navigate('/myprofile');
        } else {
            toast.error('Please log in to view your profile');
        }
    };

    const navigateToFriends = () => {

        if (username) {
            navigate('/friends');
        } else {
            toast.error('Please log in to view your friends');
        }
    };
    const navigateToLeaderboard = () => {

        if (username) {
            navigate('/leaderboard');
        } else {
            toast.error('Please log in to view the leaderboard');
        }
    };
    const navigateToLivematches = () => {

        if (username) {
            navigate('/LiveMatches');
        } else {
            toast.error('Please log in to view the livematches');
        }
    };

    const navigateToChessClub = () => {
        if (username) {
            navigate('/chessClubs');
        } else {
            toast.error('Please log in to view the chess clubs');
        }
    };

    const handleLogoClick = () => {
        navigate("/");
    };
    const handleCreateLobbyButton = async () => {
        if (!username) {
            toast.error('Please log in to play online');
            return;
        }


        const hasActiveMatches = await checkUserInGamesAndGetId(username).then()

        if (hasActiveMatches) {
            toggleActiveMatchPopup()
        }
        else{
            toggleCreateLobbyPopup()
        }
    };
    const navigateToPuzzles = () => {
        navigate('/chesspuzzles');
    }
    const navigateToPgn = () => {
        navigate('/pgn');
    }

    //SubSidebarContent
    const renderSubSidebarContent = () => {
        switch(hoveredButton) {
            case 'Play':
                return <div className="SubSidebarButtonContainer" >
                    <SubSidebarButton
                        onClick={handleCreateLobbyButton}
                        iconSrc={CreateLobby}
                        text="Create Lobby"
                        iconID="CreateLobby"
                    />
                    <SubSidebarButton
                        iconSrc={SearchForLobby}
                        text="Find Lobby"
                        iconID="FindLobby"
                    />
                    <SubSidebarButton
                        iconSrc={SearchForLobby}
                        text="Import PGN"
                        onClick={navigateToPgn}

                    />
                </div>
            case 'Puzzles':
                return <div className="SubSidebarButtonContainer" >
                    <SubSidebarButton
                        iconSrc={PuzzleIcon}
                        text="Puzzles"
                        iconID="PuzzleIcon"
                        onClick={navigateToPuzzles}
                    />
                </div>
            case 'Social':
                return <div className="SubSidebarButtonContainer" >
                    <SubSidebarButton
                        iconSrc={PlayerIcon}
                        text="My Profile"
                        iconID="MyProfileIcon"
                        onClick={navigateToProfile}
                    />

                    <SubSidebarButton
                        iconSrc={FriendsIcon}
                        text="My Mates"
                        iconID="FriendsIcon"
                        onClick={navigateToFriends}
                    />
                    <SubSidebarButton
                        iconSrc={ChessClubIcon}
                        text="Mates-Club"
                        iconID="ChessClubIcon"
                        onClick={navigateToChessClub}
                    />
                    <SubSidebarButton
                        iconSrc={SearchForPlayer}
                        text="Find Mates"
                        iconID="SearchForPlayer"
                        onClick={togglefindFriendPopup}
                    />
                    <SubSidebarButton
                        iconSrc={LeaderBoard}
                        text="Leaderboard"
                        iconID="LeaderbordeIcon"
                        onClick={navigateToLeaderboard}

                    />
                    <SubSidebarButton
                        iconSrc={Matchmaking}
                        text="Livematches"
                        iconID="Matchmaking"
                        onClick={navigateToLivematches}

                    />


                </div>;
            default:
                return null;
        }
    };

    return (
        <div className="SidebarWrapper">
            <div className="OverlayBackground"></div>
            <SearchForFriendPopup isOpen={findFriendPopup} onClose={togglefindFriendPopup} />
            <CreateLobbyPopup isOpen={lobbyPopup} onClose={toggleCreateLobbyPopup} />
            <CheckActiveMatchComponent isOpen={ActiveMatchPopup} onClose={toggleActiveMatchPopup} text="You are still in a Lobby...."/>
            <div className={"SidebarBackground"}>
                <img className={"CheckMatesLogo"} src={CheckMatesLogo} alt="Check Mates Logo" onMouseEnter={() => handleMouseEnter(null)} onClick={handleLogoClick}/>
                <div className="SidebarButtons" >
                    <SidebarButton
                        iconSrc={PlayIcon}
                        text="Play"
                        iconID="PlayIcon"
                        onMouseEnter={() => handleMouseEnter('Play')}

                    />
                    <SidebarButton
                        iconSrc={PuzzleIcon}
                        text="Puzzles"
                        iconID="PuzzleIcon"
                        onMouseEnter={() => handleMouseEnter('Puzzles')}

                    />
                    <SidebarButton
                        iconSrc={SocialIcon}
                        text="Social"
                        iconID="SocialIcon"
                        onMouseEnter={() => handleMouseEnter('Social')}

                    />
                </div>
                <div className={"Buttons"} onMouseEnter={() => handleMouseEnter(null)}>
                    {username && (
                        <div className="WelcomeMessage">
                            Welcome back, {username}!
                        </div>
                    )}
                    {!username ? (
                        <>
                            <ButtonComponent text="Login" buttonID="LoginButton" onClick={openLoginModal} />
                            <ButtonComponent text="Register" buttonID="RegisterButton" onClick={openRegisterModal} />
                        </>
                    ) : (

                        <ButtonComponent text="Logout" buttonID="LogoutButton" onClick={handleLogout} />

                    )}
                </div>
            </div>
            {hoveredButton && (
                <div className={`SubSidebar ${hoveredButton ? 'open' : ''}`}
                     onMouseLeave={handleMouseLeave}>
                    {renderSubSidebarContent()}
                </div>
            )}
            <Modal
                isOpen={isLoginModalOpen}
                onRequestClose={closeLoginModal}
                contentLabel="Login Modal"
                className="modal-overlay"
            >
                <Login closeLoginModal={closeLoginModal} onLoginSuccess={handleLoginSuccess} />
            </Modal>
            <Modal
                isOpen={isRegisterModalOpen}
                onRequestClose={closeRegisterModal}
                className="modal-overlay"
                contentLabel="Register Modal"
            >
                <Register closeRegisterModal={closeRegisterModal}/>
            </Modal>
        </div>
    );
};

export default SidebarComponent;
