import React from 'react';
import { BrowserRouter, Routes, Route, Navigate, Link } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Home from './pages/Home';
import MovieDetail from './pages/MovieDetail';
import Login from './pages/Login';
import Register from './pages/Register';
import ForgotPassword from './pages/ForgotPassword';
import 'bootstrap/dist/css/bootstrap.min.css';

// Ümumi Dizayn Qatı
function Layout({ children }) {
    const { user, loading, logout } = useAuth();

    return (
        <div style={{ backgroundColor: '#0a0a0a', minHeight: '100vh', color: 'white' }}>
            <nav className="navbar navbar-dark bg-black px-3 px-md-4 shadow-lg border-bottom border-danger border-opacity-25 sticky-top">
                <Link className="navbar-brand fw-bold text-danger fs-3" to="/" style={{ letterSpacing: '2px' }}>
                    NEO CINEMA
                </Link>
                <div className="d-flex align-items-center gap-3">
                    {loading ? (
                        <div className="spinner-border spinner-border-sm text-danger" />
                    ) : user ? (
                        <div className="d-flex align-items-center gap-3">
                            <span className="text-secondary d-none d-sm-inline small">Xoş gəldin, {user.username}</span>
                            <button className="btn btn-outline-danger btn-sm rounded-pill px-3" onClick={logout}>Çıxış</button>
                        </div>
                    ) : (
                        <div className="d-flex gap-2">
                            <Link className="btn btn-link text-white text-decoration-none btn-sm" to="/login">Giriş</Link>
                            <Link className="btn btn-danger btn-sm rounded-pill px-3 shadow" to="/register">Qeydiyyat</Link>
                        </div>
                    )}
                </div>
            </nav>
            <div className="container mt-4">
                {children}
            </div>
        </div>
    );
}

// Qorumalı Marşrut (Giriş etməyənləri Login-ə atır)
function RequireAuth({ children }) {
    const { user, loading } = useAuth();
    if (loading) return <div className="text-center mt-5"><div className="spinner-border text-danger" /></div>;
    return user ? children : <Navigate to="/login" replace />;
}

export default function App() {
    return (
        <BrowserRouter>
            <AuthProvider>
                <Layout>
                    <Routes>
                        <Route path="/" element={<RequireAuth><Home /></RequireAuth>} />
                        <Route path="/movie/:id" element={<RequireAuth><MovieDetail /></RequireAuth>} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />
                        <Route path="/forgot-password" element={<ForgotPassword />} />
                        <Route path="*" element={<Navigate to="/" replace />} />
                    </Routes>
                </Layout>
            </AuthProvider>
        </BrowserRouter>
    );
}