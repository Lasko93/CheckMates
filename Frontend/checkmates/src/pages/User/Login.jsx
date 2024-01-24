import React, {useState} from "react";
import {Button, Col, Form, FormGroup, Input, Label} from "reactstrap";
import {toast} from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./loginStyles.css";

function Login(props) {
    const [step, setStep] = useState(1);
    const [userName, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const [code, setCode] = useState("");
    const [authCodeId, setAuthCodeId] = useState(null);
    const [errorMessages, setErrorMessages] = useState({
        userName: "",
        password: "",
        code: "",
    });

    const closeTwoFactorForm = (e) => {
        e.preventDefault();
        setStep(1);
    };

    const handleLogin = async (e) => {
        e.preventDefault();

        setErrorMessages({ userName: "", password: "", code: "" });

        try {
            const response = await fetch("http://localhost:8080/api/v1/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    userName: userName,
                    password: password,
                }),
            });
            console.log(response);
            const data = await response.json();
            console.log(response);

            if (data.authCodeId) {
                setAuthCodeId(data.authCodeId);
                setStep(2);
            } else {
                setErrorMessages((prev) => ({
                    ...prev,
                    userName: data.userNameError,
                    password: data.passwordError,
                }));
            }
        } catch (error) {
            toast.error("An error occurred while logging in.");
        }
    };

    const handleVerifyCode = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch("http://localhost:8080/api/v1/verifyAuthCode", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    id: authCodeId,
                    authCode  : code,
                }),
            });

            const data = await response.json();
            if (data.token) {
                toast.success("Login successful!");
                sessionStorage.setItem('token', data.token);
                sessionStorage.setItem('userName', userName);



                props.onLoginSuccess();
            } else {
                setErrorMessages((prev) => ({ ...prev, code: data.codeError }));
            }
        } catch (error) {
            toast.error("An error occurred while verifying the code.");
        }
    };

    return (
        <div className="login-container">
            {step === 1 && (
                <Form
                    onSubmit={handleLogin}
                    className="login-form"
                    style={{ position: "relative" }}
                >
                    <a
                        href="#"
                        onClick={(e) => {
                            e.preventDefault();
                            props.closeLoginModal();
                        }}
                        style={{
                            position: "absolute",
                            right: "-10px",
                            marginTop: "-20px",
                            textDecoration: "none",
                            fontSize: "18px",
                            fontWeight: "bold",
                            color: "white"
                        }}
                    >
                        X
                    </a>
                    <FormGroup row>
                        <Label for="userName" lg={3} className="login-label">
                            UserName:
                        </Label>
                        <Col lg={9}>
                            <Input
                                type="text"
                                name="userName"
                                id="userName"
                                placeholder="Username"
                                value={userName}
                                onChange={(e) => setUserName(e.target.value)}
                                required
                            />
                            {errorMessages.userName && (
                                <div className="error-text">{errorMessages.userName}</div>
                            )}
                        </Col>
                    </FormGroup>

                    <FormGroup row>
                        <Label for="password" lg={3} className="login-label">
                            Password:
                        </Label>
                        <Col lg={9}>
                            <Input
                                type="password"
                                name="password"
                                id="password"
                                value={password}
                                placeholder="Password"
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                            {errorMessages.password && (
                                <div className="error-text">{errorMessages.password}</div>
                            )}
                        </Col>
                    </FormGroup>
                    <Button type="submit" >Login</Button>
                </Form>
            )}

            {step === 2 && (
                <Form onSubmit={handleVerifyCode}>
                    <a
                        href="#"
                        onClick={closeTwoFactorForm}
                        style={{
                            position: "absolute",
                            right: "10px",
                            top: "10px",
                            cursor: "pointer",
                            color: "white"

                        }}
                    >
                        X
                    </a>
                    <FormGroup>
                        <Label for="code">2-Factor Code:</Label>
                        <Input
                            type="text"
                            name="code"
                            id="code"
                            value={code}
                            onChange={(e) => setCode(e.target.value)}
                            required
                        />
                        {errorMessages.code && (
                            <div className="error-text">{errorMessages.code}</div>
                        )}
                    </FormGroup>
                    <Button type="submit">Confirm</Button>
                </Form>
            )}
        </div>
    );
}

export default Login;