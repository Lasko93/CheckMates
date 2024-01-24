// SidebarButton.jsx
import React from 'react';
import './SidebarButton.css';

const SidebarButton = ({ iconSrc, text, iconID,onMouseEnter, onMouseLeave }) => (
    <div className="SidebarButton" onMouseEnter={onMouseEnter} onMouseLeave={onMouseLeave}>
        <div className="icon">
            <img id={iconID} src={iconSrc} alt={`${text} Icon`} />
        </div>
        <div className="text">
            {text}
        </div>
    </div>
);

export default SidebarButton;
