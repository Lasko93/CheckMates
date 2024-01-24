import React from "react";
import './RequestData.css';
import {Link} from "react-router-dom";

const RequestData = ({ friend, onAccept, onDecline }) => {
    return (
        <div className="request-container">
            <div className="friendusername">  {friend.userName} </div>
            <div className="friendnames">{friend.firstName} {friend.lastName} </div>
            <Link className="profillink" to={`/profile/${friend.userName}`}>View Profile</Link>
            {/* Rest des Inhalts bleibt gleich */}
            <img src="data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='40' height='40' viewBox='0 0 40 40'><circle cx='20' cy='20' r='19' fill='%2300ff00'/><path fill='white' d='M16 20l-4 4l8 8l16-16l-4-4l-12 12z'/></svg>" alt="Accept" onClick={() => onAccept(friend.username)} />
            <img src="data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='40' height='40' viewBox='0 0 40 40'><circle cx='20' cy='20' r='19' fill='%23ff0000'/><path fill='white' d='M12 12l16 16m0-16L12 28' stroke='white' stroke-width='2'/></svg>" alt="Reject" onClick={() => onDecline(friend.username)} />
        </div>
    );
}

export default RequestData;