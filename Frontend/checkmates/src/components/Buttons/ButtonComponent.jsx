import React from 'react';
import "./ButtonComponent.css"

const ButtonComponent = ({text, buttonID, onClick, specificClassName, style}) => {
    return <div className={`GeneralButton ${specificClassName}`} id={buttonID} onClick={onClick} style={style}>
        <p>
            {text}
        </p>
    </div>


}

export default ButtonComponent