import React from "react";
import { NavLink, useNavigate } from "react-router-dom";
import {
  FaHome, FaChartPie, FaNetworkWired, FaEnvelope, FaUserTie, FaCog, FaSignOutAlt,FaTools 
} from "react-icons/fa";
import { signOut } from "firebase/auth";
import { auth } from "../firebase";
import "../css/Sidebar.css";
import logo from '../assets/logo3.png'; // logonun konumunu doğru yazdığınızdan emin olun


const Sidebar = () => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await signOut(auth);
      navigate("/");
    } catch (error) {
      console.error("Logout error:", error);
    }
  };

  return (
    <div className="sidebar">
      <div className="logo-section">
      <img src={logo} alt="SysGuard Logo" className="logo-image" />
        <h1 className="logo">SYSTEM GUARD</h1>
      </div>

      <ul className="menu">
        <li>
          <NavLink to="/portal/dashboard" className="menu-item">
            <FaHome /> Dashboard
          </NavLink>
        </li>
        <li>
          <NavLink to="/portal/soc" className="menu-item">
            <FaChartPie /> SOC Analyzer
          </NavLink>
        </li>
        <li>
          <NavLink to="/portal/ip-list" className="menu-item">
            <FaNetworkWired /> IP List
          </NavLink>
        </li>

        <li>
          <NavLink to="/portal/forward-mail" className="menu-item">
            <FaEnvelope /> Forward Mail
          </NavLink>
        </li>
        <li>
          <NavLink to="/portal/forward-manager" className="menu-item">
            <FaTools  /> Forward Manager
          </NavLink>
        </li>
        <li>
          <NavLink to="/portal/signup" className="menu-item">
            <FaUserTie /> Add User
          </NavLink>
        </li>
      </ul>

      <div className="sidebar-footer">

        <button className="btn logout-btn" onClick={handleLogout}>
          <FaSignOutAlt /> Logout
        </button>
      </div>
    </div>
  );
};

export default Sidebar;
