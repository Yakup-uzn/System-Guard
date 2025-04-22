// ForwardMail.jsx
import React, { useEffect, useState } from 'react';
import { FaFlag } from 'react-icons/fa';
import { FiMoreVertical } from 'react-icons/fi';
import '../css/soc.css';
import { toast } from 'react-toastify';
import { auth } from '../firebase'; // varsa auth tanımı buradan alınır

const ForwardMail = () => {
  const [replies, setReplies] = useState([]);
  const [selectedMessageId, setSelectedMessageId] = useState(null);
  const [emailDetail, setEmailDetail] = useState(null);
  const [flaggedIds, setFlaggedIds] = useState([]);
  const [openMenuId, setOpenMenuId] = useState(null);
    const [loading, setLoading] = useState(false);const [currentUserEmail, setCurrentUserEmail] = useState("");

    useEffect(() => {
      const unsubscribe = auth.onAuthStateChanged((user) => {
        if (user) {
          setCurrentUserEmail(user.email);
        }
      });
      return () => unsubscribe(); // cleanup
    }, []);
    

  useEffect(() => {
    fetch('http://localhost:8080/api/remails')
      .then((res) => res.json())
      .then(setReplies)
      .catch(console.error);
  }, []);

  useEffect(() => {
    if (selectedMessageId) {
      fetch(`http://localhost:8080/api/emails/get-by-message-id/${selectedMessageId}`)
        .then((res) => res.json())
        .then(setEmailDetail)
        .catch(console.error);
    }
  }, [selectedMessageId]);

  const handleSolve = async (messageId) => {
    try {
      // 🔹 1. ReMail silme işlemi
      const remailRes = await fetch(`http://localhost:8080/api/remails/delete-by-message-id/${messageId}`, {
        method: 'DELETE',
      });
  
      if (!remailRes.ok) {
        const remailErrorText = await remailRes.text();
        console.error("🛑 ReMail silme hatası:", remailRes.status, remailErrorText);
        throw new Error(`ReMail silinemedi (${remailRes.status}): ${remailErrorText}`);
      }

      toast.success('✅ Mail başarıyla silindi!');
      
    } catch (e) {
      console.error("❌ Silme işleminde hata:", e);
      toast.error(`❌ Mail silinemedi! ${e.message}`);
    }
  };
  
  

  const handleFlag = (id) => {
    setFlaggedIds(prev => prev.includes(id)
      ? prev.filter(flagId => flagId !== id)
      : [...prev, id]
    );
  };

  const toggleMenu = (id) => {
    setOpenMenuId(prev => prev === id ? null : id);
  };

  const toggleAccordion = (messageId) => {
    setSelectedMessageId(prev => prev === messageId ? null : messageId);
  };

  return (
    <div className="soc-container">
      <div className="soc-header-row">
        <h2>Yönlendirilmiş Mailler</h2>
      </div>

      {replies.map((mail) => {
        const isOpen = selectedMessageId === mail.messageId;
        const isFlagged = flaggedIds.includes(mail.id);

        return (
          <div key={mail.id} className={`soc-card ${isOpen ? 'running-self' : ''}`}>
            <div className="soc-summary" onClick={() => toggleAccordion(mail.messageId)}>
              
              <div className="soc-avatar">{mail.subject?.charAt(0)}</div>
              <div className="soc-header-content">
                <span className="soc-subject">
                  {isFlagged && <FaFlag className="soc-flag-icon" />} {mail.subject}
                </span>
              </div>
              <div className="soc-menu-wrapper" onClick={(e) => e.stopPropagation()}>
                <FiMoreVertical className="soc-menu-icon" onClick={() => toggleMenu(mail.id)} />
                {openMenuId === mail.id && (
                  <div className="soc-dropdown">
                    <div onClick={() => handleFlag(mail.id)}>{isFlagged ? '🚫 Bayrağı Kaldır' : '📌 Bayrak Ekle'}</div>
                    <div onClick={() => handleSolve(mail.messageId)}>✅ Solved</div>
                  </div>
                )}
              </div>
            </div>

            {isOpen && emailDetail && (
              <div className="soc-details">
                <div className="soc-section">
                <h4>📨 Cevap</h4>
                <div className="soc-detail-item"> <span className="soc-key"></span> <span className="soc-value">{mail.request}</span></div>
                  <h4>📄 Olay Bilgileri</h4>
                  <div className="soc-detail-grid">
                    <div className="soc-detail-item"><span className="soc-key">Alert Name:</span> <span className="soc-value">{emailDetail.alertName}</span></div>
                    <div className="soc-detail-item"><span className="soc-key">Log Source:</span> <span className="soc-value">{emailDetail.logSource}</span></div>
                    <div className="soc-detail-item"><span className="soc-key">Severity:</span> <span className="soc-value">{emailDetail.severity}</span></div>
                    <div className="soc-detail-item"><span className="soc-key">Analyst:</span> <span className="soc-value">{emailDetail.analyst}</span></div>
                    <div className="soc-detail-item"><span className="soc-key">Offense Time:</span> <span className="soc-value">{emailDetail.offenseTime}</span></div>
                    <div className="soc-detail-item"><span className="soc-key">Offense Source:</span> <span className="soc-value">{emailDetail.offenseSource}</span></div>
                    <div className="soc-detail-item" style={{ gridColumn: '1 / -1' }}>
                      <span className="soc-key">Analyst Comment:</span> <span className="soc-value">{emailDetail.analystComment}</span>
                    </div>
                  </div>
                  <div style={{ marginTop: '12px' }}>
                    <a href={emailDetail.webLink} target="_blank" rel="noopener noreferrer" className="soc-detail-link-btn">📎 Daha Fazla Detay</a>
                  </div>

                </div>

<div className="soc-section">
  <h4>📝 Vaka Değerlendirme Formu</h4>

  {loading && (
    <div className="loading-box">
      <span className="spinner"></span> Form gönderiliyor...
    </div>
  )}

  <form
    className="soc-evaluation-form"
    onSubmit={async (e) => {
      e.preventDefault();
      const vakaSonucu = e.target.vakaSonucu.value;
      const degerlendirme = e.target.degerlendirme.value;
    
      if (!vakaSonucu || !degerlendirme) {
        toast.warning("Lütfen tüm seçimleri yapınız.");
        return;
      }
    
      const payload = {
        formLink: emailDetail.formLink,
        caseResult: vakaSonucu,
        evaluationResult: degerlendirme,
        name: currentUserEmail,
      };
    
      try {
        setLoading(true);
    
        const response = await fetch("http://localhost:8080/api/solved", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
    
        const message = await response.text();
    
        if (response.status === 200) {
          toast.success("✅ " + message);
          e.target.reset();
          handleSolve(emailDetail.id); // ✅ form başarıyla gönderildiyse sil
        } else if (response.status === 208) {
          toast.warning("⚠️ " + message);
          handleSolve(emailDetail.id); // ✅ daha önce çözüldüyse yine sil
        } else {
          toast.error("❌ " + message);
        }
    
      } catch (error) {
        console.error("Değerlendirme gönderim hatası:", error);
        toast.error("⚠️ Sunucuya ulaşılamadı.");
      } finally {
        setLoading(false);
      }
    }}
    
  >
    <div className="form-row">
      <div className="form-group">
        <label>Vakayı sonuçlandırınız:</label>
        {[
          "False-Positive",
          "False-Positive Tuned",
          "Solved",
          "True-Positive",
          "Duplicate",
          "Non-Issue",
        ].map((option) => (
          <div className="radio-option" key={option}>
            <input type="radio" name="vakaSonucu" value={option} id={`vaka-${option}`} />
            <label htmlFor={`vaka-${option}`}>{option}</label>
          </div>
        ))}
      </div>

      <div className="form-group">
        <label>Vaka değerlendirme sonuçlarını belirtiniz:</label>
        {[
          "IP adresi engellenmiştir.",
          "İşlem kullanıcı bilgisi dahilindedir.",
          "İşlem bilgimiz dahilindedir.",
          "Riskli bir durum görülmemiştir.",
          "Gerekli kontroller sonrası aksiyon alınmıştır.",
        ].map((option) => (
          <div className="radio-option" key={option}>
            <input type="radio" name="degerlendirme" value={option} id={`deg-${option}`} />
            <label htmlFor={`deg-${option}`}>{option}</label>
          </div>
        ))}
      </div>
    </div>

    <button type="submit" disabled={loading}>Gönder</button>
  </form>
</div>


              </div>
            )}
          </div>
        );
      })}
    </div>
  );
};

export default ForwardMail;
