import React from 'react';
import './SearchBar.css';

const SearchBar = ({ value, onChange }) => {
    return (
        <div className="search-container">
            <i className="search-icon"></i>
            <input  type="text"
                    placeholder="Search for a player..."
                    className="search-input"
                    value={value}
                    onChange={onChange}
            />
        </div>
    );
};

export default SearchBar;
