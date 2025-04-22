// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getFirestore } from 'firebase/firestore';

// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyDFNcp-q0_kmkPZ3gx8NG4X_KgOG2YPYH0",
  authDomain: "sysguard-f8f4c.firebaseapp.com",
  projectId: "sysguard-f8f4c",
  storageBucket: "sysguard-f8f4c.firebasestorage.app",
  messagingSenderId: "436599639110",
  appId: "1:436599639110:web:1fd15cca9dd2fbfaf18e47",
  measurementId: "G-VP0SE7RKHP"
};

// Initialize Firebase

const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);