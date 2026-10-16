import React, { createContext, useContext, useState, useCallback } from 'react';

const AuthContext = createContext(null);

/**
 * Decodes JWT payload without a library.
 * Returns the payload object or null if invalid.
 */
function decodeToken(token) {
  try {
    const payload = token.split('.')[1];
    return JSON.parse(atob(payload));
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('smarthire_token'));
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('smarthire_user');
    return saved ? JSON.parse(saved) : null;
  });

  const loginUser = useCallback((token, userData) => {
    localStorage.setItem('smarthire_token', token);
    localStorage.setItem('smarthire_user', JSON.stringify(userData));
    setToken(token);
    setUser(userData);
  }, []);

  const logoutUser = useCallback(() => {
    localStorage.removeItem('smarthire_token');
    localStorage.removeItem('smarthire_user');
    setToken(null);
    setUser(null);
  }, []);

  const isAuthenticated = Boolean(token);
  const role = user?.role || null;

  return (
    <AuthContext.Provider value={{ token, user, role, isAuthenticated, loginUser, logoutUser }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
