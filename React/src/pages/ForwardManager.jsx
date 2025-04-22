import React, { useEffect, useState } from "react";
import '../css/ForwardManager.css'
const ForwardManager = () => {
    const [ccList, setCcList] = useState([]);
    const [toList, setToList] = useState([]);
    const [newCc, setNewCc] = useState("");
    const [newTo, setNewTo] = useState("");
  
    const API = "http://localhost:8080/api/forward";
  
    const fetchData = async () => {
      const ccRes = await fetch(`${API}/cc`);
      const toRes = await fetch(`${API}/to`);
      setCcList(await ccRes.json());
      setToList(await toRes.json());
    };
  
    useEffect(() => {
      fetchData();
    }, []);
  
    const handleAdd = async (type) => {
      const email = type === "cc" ? newCc : newTo;
      if (!email) return alert("Email boş olamaz");
  
      const res = await fetch(`${API}/${type}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email }),
      });
  
      if (res.ok) {
        alert("Email eklendi");
        type === "cc" ? setNewCc("") : setNewTo("");
        fetchData();
      } else {
        alert("Ekleme başarısız");
      }
    };
  
    const handleDelete = async (type, id) => {
      const res = await fetch(`${API}/${type}/${id}`, { method: "DELETE" });
      if (res.ok) {
        alert("Silindi");
        fetchData();
      } else {
        alert("Silme başarısız");
      }
    };
  
    return (
      <div className="forward-manager-container">
        <div className="forward-section">
          <h2 className="forward-title">Forward CC</h2>
          <div className="forward-form">
            <input
              className="forward-input"
              type="email"
              placeholder="Yeni CC email"
              value={newCc}
              onChange={(e) => setNewCc(e.target.value)}
            />
            <button className="forward-button" onClick={() => handleAdd("cc")}>
              Ekle
            </button>
          </div>
          <ul className="forward-list">
            {ccList.map((item) => (
              <li key={item.id} className="forward-list-item">
                <span>{item.email}</span>
                <button
                  className="forward-delete-button"
                  onClick={() => handleDelete("cc", item.id)}
                >
                  Sil
                </button>
              </li>
            ))}
          </ul>
        </div>
  
        <div className="forward-section">
          <h2 className="forward-title">Forward TO</h2>
          <div className="forward-form">
            <input
              className="forward-input"
              type="email"
              placeholder="Yeni TO email"
              value={newTo}
              onChange={(e) => setNewTo(e.target.value)}
            />
            <button className="forward-button" onClick={() => handleAdd("to")}>
              Ekle
            </button>
          </div>
          <ul className="forward-list">
            {toList.map((item) => (
              <li key={item.id} className="forward-list-item">
                <span>{item.email}</span>
                <button
                  className="forward-delete-button"
                  onClick={() => handleDelete("to", item.id)}
                >
                  Sil
                </button>
              </li>
            ))}
          </ul>
        </div>
      </div>
    );
  };
  
  export default ForwardManager;