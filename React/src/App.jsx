import React, { useEffect, useState, lazy, Suspense } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { onAuthStateChanged } from "firebase/auth";
import { auth } from "./firebase";
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';



// Lazy load component'ler
const Home = lazy(() => import("./pages/Home"));
const Login = lazy(() => import("./pages/Portal_login"));
const Portal = lazy(() => import("./pages/Portal"));
const SOC = lazy(() => import("./pages/SOC"));
const IPList = lazy(() => import("./pages/IPList"));
const ForwardMail = lazy(() => import("./pages/ForwardMail"));
const Dashboard = lazy(() => import("./pages/Dashboard"));
const SignUp = lazy(() => import("./pages/SignUp"));
const ForwardManager = lazy(() => import("./pages/ForwardManager"));



const App = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (currentUser) => {
      setUser(currentUser);
      setLoading(false);
    });
    return () => unsubscribe();
  }, []);

  const ProtectedRoute = ({ element }) => {
    if (loading) return <div>Yükleniyor...</div>;
    return user ? element : <Navigate to="/" />;
  };

  const LoginRoute = () => {
    if (loading) return <div>Yükleniyor...</div>;
    return user ? <Navigate to="/portal" /> : <Login />;
  };

  const HomeRoute = () => {
    if (loading) return <div>Yükleniyor...</div>;
    return user ? <Navigate to="/portal" /> : <Home />;
  };

  return (
<Router>
  <Suspense fallback={<div>Yükleniyor...</div>}>
    <Routes>
      <Route path="/" element={<HomeRoute />} />
      <Route path="/login" element={<LoginRoute />} />

      <Route
        path="/portal/*"
        element={<ProtectedRoute element={<Portal />} />}
      >
        <Route path="dashboard" element={<Dashboard />} />
        <Route path="ip-list" element={<IPList />} />
        <Route path="forward-mail" element={<ForwardMail />} />
        <Route path="forward-manager" element={<ForwardManager />} />
        <Route path="signup" element={<SignUp />} />
      </Route>

      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  </Suspense>
  <ToastContainer position="top-right" autoClose={3000} />
</Router>

  );
};

export default App;
