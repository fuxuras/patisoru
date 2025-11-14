import { atom } from 'nanostores';

export const $isLoggedIn = atom<boolean>(false);
export const $jwtToken = atom<string | null>(null);
export const $user = atom<any | null>(null);

export function login(token: string, userData: any) {
    $jwtToken.set(token);
    $isLoggedIn.set(true);
    $user.set(userData);
}

export function logout() {
    $jwtToken.set(null);
    $isLoggedIn.set(false);
    $user.set(null);
}