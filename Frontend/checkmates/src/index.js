import React from 'react';
import {createRoot} from 'react-dom/client'; // Note the import change here
import './index.css';
import App from './App';

const root = document.getElementById('root');
const rootContainer = createRoot(root);

rootContainer.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
);
