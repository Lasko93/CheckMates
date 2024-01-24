import './ChessClub.css';
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import ChatRoomComponent from "../../components/Chat/ChatRoomComponent";

const ChessClub = () => {


    const { clubId } = useParams();

    const [showPopup, setShowPopup] = useState(false);
    const [groupChatId, setGroupChatId] = useState(null);
    useEffect(() => {
        const chatRoomId = `club_${clubId}`;
        setGroupChatId(chatRoomId);
    }, [clubId]);


    const togglePopup = () => {
        setShowPopup(!showPopup);
    }


    return (
        <div>
            <div className="TitleChessClub">
                This is the ChessClub Page of {clubId}
            </div>
                <ChatRoomComponent
                    groupChatId={groupChatId}
                />

        </div>

    );
}

export default ChessClub;
