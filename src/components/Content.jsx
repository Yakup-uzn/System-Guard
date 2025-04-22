import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import SOC from "../pages/SOC";
import IPList from "../pages/IPList";
import ForwardMail from "../pages/ForwardMail";
import Dashboard from "../pages/Dashboard";
import SignUp from "../pages/SignUp";
import "../css/Content.css";
import ForwardManager from "../pages/ForwardManager";

const Content = () => {
  return (
    <div className="content">
      <div className="content-container">
        <Routes>
          <Route path="/" element={<Navigate to="/portal/dashboard" replace />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/soc" element={<SOC />} />
          <Route path="/ip-list" element={<IPList />} />
          <Route path="/forward-mail" element={<ForwardMail />} />
          <Route path="/forward-manager" element={<ForwardManager />} />
          <Route path="/signup" element={<SignUp />} />
        </Routes>
      </div>
    </div>
  );
};

export default Content;
