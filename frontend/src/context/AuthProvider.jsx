import { useEffect, useState } from "react";
import { AuthContext } from "./AuthContext";
import authService from "@/services/authService";
import { jwtDecode } from 'jwt-decode'

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(localStorage.getItem('token'))

    const login = async (email, password) => {
        const response = await authService.login(email, password);
        setToken(response.data.accessToken);
        return response;
    };

    const logout = () => {
        setToken(null);
    };

    const value = {
        user,
        token,
        login,
        logout,
    };

    useEffect(() => {
        if (token) {
            try {
                const decodedToken = jwtDecode(token);
                const isExpired = decodedToken.exp * 1000 < Date.now();
                if (isExpired) {
                    logout();
                } else {
                    setUser({ email: decodedToken.sub, roles: decodedToken.roles });
                    localStorage.setItem('token', token);
                }
            } catch (error) {
                console.error("Invalid token: ", error);
                logout();
            }
        } else {
            localStorage.removeItem('token');
            setUser(null);
        }
    }, [token]);

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}