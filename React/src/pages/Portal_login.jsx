// src/pages/Login.jsx
import React, { useState } from "react";
import { signInWithEmailAndPassword } from "firebase/auth";
import { auth } from "../firebase";
import { useNavigate } from "react-router-dom";
import '../css/Login.css';

const Portal_login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    try {
      await signInWithEmailAndPassword(auth, email, password);
      navigate("/portal/ip-list");
    } catch (err) {
      setError("Giriş başarısız. Email veya şifre hatalı.");
      console.error(err.message);
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleLogin} className="login-form">
        <div className="logo-container">
          <img src="/logo.png" alt="logo" className="logo"/>
          <h2>SYSTEM GUARD</h2>
        </div>
        <div className="form-group">
          <label htmlFor="email">E-Posta</label>
          <input
            type="email"
            id="email"
            placeholder="xxxxx@xxxxx.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="password">Şifre</label>
          <input
            type="password"
            id="password"
            placeholder="••••••"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <button type="submit">Gönder</button>
        {error && <p className="error">{error}</p>}
      </form>
    </div>
  );
};

export default Portal_login;
