// src/pages/Portal.jsx
import React from "react";
import Sidebar from "../components/Sidebar";
import Content from "../components/Content";
import "../css/Portal.css";

const Portal = () => {
  return (
    <div className="app-container">
      <Sidebar />
      <Content />
    </div>
  );
};

export default Portal;
