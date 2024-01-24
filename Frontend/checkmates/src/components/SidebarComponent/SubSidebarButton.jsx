import React from 'react';
import './SubSidebarButton.css';

const SubSidebarButton = ({ iconSrc, text, iconID, onClick }) => (
    <div className="SubSidebarButton" onClick={onClick}>
        <div className="icon" >
            <img id={iconID} src={iconSrc} alt={`${text} Icon`} />
        </div>
        <div className="SubText">
            {text}
        </div>
    </div>
);

export default SubSidebarButton;
