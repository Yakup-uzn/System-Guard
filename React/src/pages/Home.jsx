// src/pages/Home.jsx
import React from "react";
import { useNavigate } from "react-router-dom";
import { FaShieldAlt, FaEnvelope, FaClipboardList, FaChartBar } from "react-icons/fa";
import logo from '../assets/logo4.png';
import '../css/Home.css';

const Home = () => {
  const navigate = useNavigate();

  return (
    <div className="home-container">
      <header className="home-header">
        <img src={logo} alt="SYS GUARD Logo" className="home-logo" />
        <h1>SYSTEM GUARD</h1>
      </header>
      <section className="home-intro">
        <p>
          SYS GUARD, SOC süreçlerinizi yönetmenizi sağlayan modern bir güvenlik platformudur. SOC e-postalarını organize edin, Sysmon ve Wazuh loglarını takip edin.
        </p>
      </section>
      <section className="home-features">
        <div className="feature-card">
          <FaEnvelope className="feature-icon" />
          <h3>SOC E-postaları</h3>
        </div>
        <div className="feature-card">
          <FaClipboardList className="feature-icon" />
          <h3>Log Takibi</h3>
        </div>
        <div className="feature-card">
          <FaChartBar className="feature-icon" />
          <h3>Raporlama</h3>
        </div>
        <div className="feature-card">
          <FaShieldAlt className="feature-icon" />
          <h3>Güvenlik</h3>
        </div>
      </section>
      <button className="home-login-btn" onClick={() => navigate("/login")}>
        Giriş Yap
      </button>
    </div>
  );
};

export default Home;