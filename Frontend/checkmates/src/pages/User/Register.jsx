import React, {useState} from "react";
import "./registerStyles.css";
import {toast, ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function Register({closeRegisterModal}) {
    const [formData, setFormData] = useState({
        userName: "",
        firstName: "",
        lastName: "",
        birthDay: "",
        email: "",
        password: "",
        repeatPassword: "",
        photo: null,
    });

    const [imagePreviewUrl, setImagePreviewUrl] = useState("");

    const [errorMessages, setErrorMessages] = useState({
        userName: "",
        password: "",
        repeatPassword: "",
    });


    const todayDate = new Date().toISOString().split('T')[0];

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
        setErrorMessages({
            ...errorMessages,
            [e.target.name]: "",
        });
    };


    const add = (a,b)  => {
        return "a+b";
    }
    const handleFileChange = (e) => {
        let file = e.target.files[0];

        setFormData({
            ...formData,
            photo: file,
        });
        console.log(file);

        // Create a URL for the uploaded image to preview it
        let reader = new FileReader();
        reader.onloadend = () => {
            setImagePreviewUrl(reader.result);
        };

        if (file) {
            reader.readAsDataURL(file);
        } else {
            setImagePreviewUrl("");
        }
    };
    const validatePassword = (password) => {
        const hasUpperCase = /[A-Z]/.test(password);
        const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);
        return password.length >= 8 && hasUpperCase && hasSpecialChar;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Check if passwords match
        if (formData.password !== formData.repeatPassword) {
            setErrorMessages((prevErrors) => ({
                ...prevErrors,
                repeatPassword: "Passwords do not match",
            }));
            return;
        }

        // Validate password strength
        if (!validatePassword(formData.password)) {
            setErrorMessages((prevErrors) => ({
                ...prevErrors,
                password:
                    "Password must be at least 8 characters long, contain at least one special character and one uppercase letter.",
            }));
            return;
        }

        // Validate email format
        if (!formData.email.includes("@")) {
            setErrorMessages((prevErrors) => ({
                ...prevErrors,
                email: "Email must contain '@'",
            }));
            return;
        }

        try {
            const userDataResponse = await fetch("http://localhost:8080/api/v1/register", {
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    userName: formData.userName,
                    firstName: formData.firstName,
                    lastName: formData.lastName,
                    birthDay: formData.birthDay,
                    email: formData.email,
                    password: formData.password,
                }),
            });

            const userData = await userDataResponse.json();
            console.log(userData);

            if (!userDataResponse.ok) {
                if (userDataResponse.status === 400) {
                    toast.error("Username already exists.", {
                        position: "bottom-right",
                        autoClose: 3000,
                    });
                } else {
                    throw new Error(userData.message || "User data submission failed");
                }
                return;
            }

            if (formData.photo) {
                const formDataObj = new FormData();
                formDataObj.append('userName', formData.userName);
                formDataObj.append('image', formData.photo);

                const photoResponse = await fetch("http://localhost:8080/api/v1/photos/", {
                    method: "POST",
                    body: formDataObj,
                });

                if (!photoResponse.ok) {
                    const photoData = await photoResponse.json();
                    throw new Error(photoData.message || "Photo submission failed");
                }
            }

            toast.success("Registration successful!", {
                position: "bottom-right",
                autoClose: 2000,
            });
            toast.success("bitte loggen Sie sich ein", {
                position: "top-right",
                autoClose: 3000,
            });
            closeRegisterModal();

        } catch (error) {
            console.error('Fetch error:', error);
            toast.error("Network error. Please try again.", {
                position: "bottom-right",
                autoClose: 3000,
            });
        }
    };

    return (
        <div className="register-container">
            <ToastContainer />
            <form onSubmit={handleSubmit} className="register-form">
                <div className="form-group">
                    <label htmlFor="username" className="register-form-label">Username:</label>
                    <input type="text" name="userName" id="username" placeholder="Username" onChange={handleChange} required />
                    {errorMessages.userName && <div className="error-text">{errorMessages.userName}</div>}
                </div>

                <div className="form-group">
                    <label htmlFor="firstname" className="register-form-label">Firstname:</label>
                    <input type="text" name="firstName" id="firstname"  placeholder="Firstname" onChange={handleChange} required />
                </div>

                <div className="form-group">
                    <label htmlFor="lastname" className="register-form-label">Lastname:</label>
                    <input type="text" name="lastName" id="lastname" placeholder="Lastname" onChange={handleChange} required />
                </div>

                <div className="form-group">
                    <label htmlFor="email" className="register-form-label">Email:</label>
                    <input type="email" name="email" id="email" onChange={handleChange} placeholder="Email" required />
                    {errorMessages.email && <div className="error-text">{errorMessages.email}</div>}
                </div>
                <div className="form-group">
                    <label htmlFor="birthday" className="register-form-label">Birthday:</label>
                    <input
                        type="date"
                        name="birthDay"
                        id="birthday"
                        onChange={handleChange}
                        min="1920-01-01"
                        max={todayDate}
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="password" className="register-form-label">Password:</label>
                    <input type="password" name="password" id="password" onChange={handleChange} placeholder="Password" required />
                    {errorMessages.password && <div className="error-text">{errorMessages.password}</div>}
                </div>

                <div className="form-group">
                    <label htmlFor="repeatPassword" className="register-form-label">Repeat Password:</label>
                    <input type="password" name="repeatPassword" id="repeatPassword" onChange={handleChange} placeholder="repeat Password" required />
                    {errorMessages.repeatPassword && <div className="error-text">{errorMessages.repeatPassword}</div>}
                </div>

                <div className="form-group">
                    <label htmlFor="photo" className="photo-label">Upload Photo</label>
                    <div className="photo-upload">
                        <label htmlFor="photo" className="upload-label">
                            {imagePreviewUrl ? (
                                <img src={imagePreviewUrl} alt="Uploaded preview" className="photo-preview" />
                            ) : (
                                <div className="upload-icon">+</div>
                            )}
                        </label>
                        <input
                            type="file"
                            name="photo"
                            id="photo"
                            onChange={handleFileChange}
                            className="photo-input"
                        />
                    </div>
                </div>

                <div className="form-group action-buttons">
                    <button type="submit" className="submit-button">Register</button>
                    <button type="button" className="close-button" onClick={closeRegisterModal}>Cancel</button>
                </div>
            </form>
        </div>
    );
}
export default Register;