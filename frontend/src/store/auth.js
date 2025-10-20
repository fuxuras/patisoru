import { atom } from 'nanostores';

export const isUserLoggedIn = atom(false);
export const jwtToken = atom(null);

export function login(token) {
    jwtToken.set(token);
    isUserLoggedIn.set(true);
}

export function logout() {
    jwtToken.set(null);
    isUserLoggedIn.set(false);
}