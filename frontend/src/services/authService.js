import axios from 'axios';

const API_URL = '/api/auth';

const login = (email, password) => {
    return axios.post(API_URL + '/login', {
        email,
        password
    });
}

const register = (fullName, email, password) => {
    return axios.post(API_URL + '/register', {
        fullName,
        email,
        password
    })
}

const authService = {
    login,
    register
};

export default authService;