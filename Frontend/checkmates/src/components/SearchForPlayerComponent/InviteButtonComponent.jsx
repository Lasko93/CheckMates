import {useState} from "react";

const InviteButtonComponent = ({ initialText, buttonID, specificClassName, style, onClick }) => {
    const [buttonText, setButtonText] = useState(initialText);
    const [buttonStyle, setButtonStyle] = useState(style);

    const handleClick = () => {

        onClick();

        setButtonStyle({
            ...buttonStyle,
            backgroundColor: '#6EBFFF',
        });
        setButtonText('Invite Sent');
    };

    return (
        <div className={`GeneralButton ${specificClassName}`} id={buttonID} onClick={handleClick} style={buttonStyle}>
            <p>{buttonText}</p>
        </div>
    );
}

export default InviteButtonComponent;
