import React from 'react';
import { useStore } from '@nanostores/react';
import { $isLoggedIn } from '../../store/auth';

export default function AuthStatusMobile() {
  const isLoggedIn = useStore($isLoggedIn);

  return (
    <>
      {isLoggedIn ? (
        <>
          <a href="/profile" className="block px-6 py-2 text-gray-600 hover:bg-gray-100">Profile</a>
          <button id="logout-btn-mobile" className="w-full text-left block px-6 py-2 text-gray-600 hover:bg-gray-100">Logout</button>
        </>
      ) : (
        <>
          <a href="/login" className="block px-6 py-2 text-gray-600 hover:bg-gray-100">Login</a>
          <a href="/register" className="block px-6 py-2 text-gray-600 hover:bg-gray-100">Register</a>
        </>
      )}
    </>
  );
}
