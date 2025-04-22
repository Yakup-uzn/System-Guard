import React, { useState } from 'react';
import { createUserWithEmailAndPassword } from 'firebase/auth';
import { auth } from '../firebase';
import { FaUser, FaEnvelope, FaLock, FaUserShield } from 'react-icons/fa';
import '../css/SignUp.css';

const SignUp = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('user');
  const [error, setError] = useState('');

  const handleSignUp = async (e) => {
    e.preventDefault();
    try {
      await createUserWithEmailAndPassword(auth, email, password);
      alert('Kullanıcı başarıyla oluşturuldu ve yetkilendirildi!');
      setName('');
      setEmail('');
      setPassword('');
      setRole('user');
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="signup-container-portal">
      <form className="signup-form-portal" onSubmit={handleSignUp}>
        <h2 className="form-title">Kullanıcı Ekle ve Yetkilendir</h2>
        {error && <p className="error-message">{error}</p>}

        <div className="input-group-portal">
          <label><FaUser /> Ad Soyad</label>
          <input
            type="text"
            placeholder="Kullanıcının Adı ve Soyadı"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </div>

        <div className="input-group-portal">
          <label><FaEnvelope /> E-posta</label>
          <input
            type="email"
            placeholder="E-posta adresi"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>

        <div className="input-group-portal">
          <label><FaLock /> Şifre</label>
          <input
            type="password"
            placeholder="Güçlü bir şifre giriniz"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <div className="input-group-portal">
          <label><FaUserShield /> Yetki Seviyesi</label>
          <select value={role} onChange={(e) => setRole(e.target.value)} required>
            <option value="user">Kullanıcı</option>
            <option value="admin">Admin</option>
            <option value="analyst">Analist</option>
          </select>
        </div>

        <button type="submit" className="signup-button-portal">Kullanıcıyı Kaydet</button>
      </form>
    </div>
  );
};

export default SignUp;
