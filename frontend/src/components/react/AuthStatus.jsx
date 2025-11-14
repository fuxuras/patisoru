import React from 'react';
import { useStore } from '@nanostores/react';
import { $isLoggedIn } from '../../store/auth';

export default function AuthStatus() {
  const isLoggedIn = useStore($isLoggedIn);

  return (
    <>
      {isLoggedIn ? (
        <>
          <a href="/profile" className="text-gray-600 hover:text-blue-600">Profile</a>
          <button id="logout-btn-desktop" className="text-gray-600 hover:text-blue-600">Logout</button>
        </>
      ) : (
        <>
          <a href="/login" className="text-gray-600 hover:text-blue-600">Login</a>
          <a href="/register" className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">Register</a>
        </>
      )}
    </>
  );
}
