import React, { useEffect, useState } from 'react';
import { FiMoreVertical } from 'react-icons/fi';
import { FaFlag } from 'react-icons/fa';
import '../css/soc.css';
import { toast } from 'react-toastify';
import { auth } from '../firebase';

import { Client } from '@stomp/stompjs';

const SOC = () => {
  const [emails, setEmails] = useState([]);
  const [expandedId, setExpandedId] = useState(null);
  const [ipDetails, setIpDetails] = useState({});
  const [filter, setFilter] = useState('');
  const [openMenuId, setOpenMenuId] = useState(null);
  const [flaggedIds, setFlaggedIds] = useState([]);
  const [blockedIps, setBlockedIps] = useState([]);
  const [localOpenedId, setLocalOpenedId] = useState(null);
  const [currentUserEmail, setCurrentUserEmail] = useState("");
  const [loading, setLoading] = useState(false);


  useEffect(() => {
    const unsubscribe = auth.onAuthStateChanged((user) => {
      if (user) {
        setCurrentUserEmail(user.email);
      }
    });
    return () => unsubscribe();
  }, []);

  useEffect(() => {
    const stompClient = new Client({
      brokerURL: 'ws://localhost:8080/ws-running-update',
      onConnect: () => {
        console.log('🔌 WebSocket bağlantısı kuruldu.');
  
        // ✅ 1. running güncellemesi
        stompClient.subscribe('/topic/running-updates', (message) => {
          const data = JSON.parse(message.body);
          setEmails(prevEmails =>
            prevEmails.map(email =>
              email.id === data.emailId
                ? { ...email, running: data.running, byUser: data.byUser }
                : email
            )
          );
        });
  
        // ✅ 2. ekleme/silme
        stompClient.subscribe('/topic/email-updates', (message) => {
          const data = JSON.parse(message.body);
          setEmails(prevEmails => {
            if (data.action === "CREATE") {
              return [data.email, ...prevEmails];
            } else if (data.action === "DELETE") {
              return prevEmails.filter(email => email.id !== data.email.id);
            }
            return prevEmails;
          });
        });
  
        // ✅ 3. forward güncellemesi
        stompClient.subscribe('/topic/forward-updates', (message) => {
          const data = JSON.parse(message.body);
          
          setEmails(prevEmails =>
            prevEmails.map(email =>
              email.id === data.emailId
                ? { ...email, forward: data.forward }
                : email
            )
          );
        });
      },
      onStompError: (frame) => {
        console.error('STOMP error:', frame);
      },
    });
  
    stompClient.activate();
  
    return () => {
      stompClient.deactivate();
    };
  }, []);
  
  
  

  useEffect(() => {
    fetch('http://localhost:8080/api/emails')
      .then((res) => {
        if (!res.ok) throw new Error(`HTTP hata: ${res.status}`);
        return res.json();
      })
      .then(setEmails)
      .catch(console.error);
  }, []);
  

  useEffect(() => {
    const handleUnload = () => {
      if (currentUserEmail) {
        fetch(`http://localhost:8080/api/emails/close-by-user?byUser=${encodeURIComponent(currentUserEmail)}`, {
          method: 'POST',
          keepalive: true
        });
      }
    };
  
    window.addEventListener("beforeunload", handleUnload);
    return () => window.removeEventListener("beforeunload", handleUnload);
  }, [currentUserEmail]);
  
  

 

  const fetchIpDetails = async (ip) => {
    if (!ip) return;
    try {
      const res = await fetch(`http://localhost:8080/api/ip-check/${ip}`);
      if (!res.ok) return;
      const data = await res.json();
      setIpDetails(prev => ({ ...prev, [ip]: data }));
    } catch (error) {
      console.error('IP sorgulama hatası:', error);
    }
  };

  const handleSolve = async (id) => {
    try {
      const res = await fetch(`http://localhost:8080/api/emails/${id}`, {
        method: 'DELETE',
      });
      if (!res.ok) throw new Error('Silme işlemi başarısız');
      setEmails((prev) => prev.filter((email) => email.id !== id));
      toast.success("✅ Mail başarıyla silindi!");
    } catch (error) {
      console.error('Silme hatası:', error);
      toast.error("❌ Mail silinemedi.");
    }
  };

  const handleForward = async (email) => {
    const userComment = prompt("Yönlendirme notunuzu giriniz:", "SOC tarafından bildirilen olayı iletiyorum. İnceleyiniz.");
    if (!userComment) {
      toast.warning("Yorum girilmediği için yönlendirme iptal edildi.");
      return;
    }
  
    const payload = {
      messageId: email.messageId,
      comment: userComment
    };
  
    try {
      const res = await fetch("http://localhost:8080/api/forward", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
  
      if (res.ok) {
        // ✅ Forward true yap
        await fetch(`http://localhost:8080/api/emails/${email.id}/forward`, {
          method: "PATCH"
        });
  
        toast.success("📤 Mail başarıyla yönlendirildi.");
      } else {
        toast.error("❌ Mail yönlendirme başarısız oldu.");
      }
    } catch (error) {
      console.error("Forward hatası:", error);
      toast.error("⚠️ Bir hata oluştu.");
    }
  };
  

  const handleFlag = (id) => {
    setFlaggedIds(prev => prev.includes(id) ? prev.filter(flagId => flagId !== id) : [...prev, id]);
  };

  const toggleMenu = (id) => {
    setOpenMenuId(prev => (prev === id ? null : id));
  };

  const toggleAccordion = async (id, offenseSource) => {
    const isOpening = expandedId !== id;
    const previouslyOpenedId = localOpenedId;

    setExpandedId(isOpening ? id : null);
    if (isOpening) setLocalOpenedId(id);

    if (isOpening && offenseSource && !ipDetails[offenseSource]) {
      await fetchIpDetails(offenseSource);
    }

    if (previouslyOpenedId && previouslyOpenedId !== id && currentUserEmail) {
      try {
        await fetch(`http://localhost:8080/api/emails/${previouslyOpenedId}/running?running=0&byUser=${currentUserEmail}`, {
          method: 'PATCH',
        });
      } catch (err) {
        console.error('Önceki accordion running sıfırlama hatası:', err);
      }
    }

    if (currentUserEmail) {
      try {
        await fetch(`http://localhost:8080/api/emails/${id}/running?running=${isOpening ? 1 : 0}&byUser=${currentUserEmail}`, {
          method: 'PATCH',
        });
      } catch (err) {
        console.error('Running güncelleme hatası:', err);
      }
    }

    setEmails((prevEmails) =>
      prevEmails.map((email) => {
        if (email.id === id) {
          return { ...email, running: isOpening ? 1 : 0 };
        } else if (email.id === previouslyOpenedId) {
          return { ...email, running: 0 };
        } else {
          return email;
        }
      })
    );
  };

  const filteredEmails = emails.filter(email =>
    email.subject && email.subject.toLowerCase().includes(filter.toLowerCase())
  );

  const handleBlockIp = async (ipInfo, offenseSource) => {
    const isBlocked = blockedIps.includes(offenseSource);

    try {
      if (isBlocked) {
        
        const res = await fetch(`http://localhost:8080/api/ips/unblock?ipAddress=${offenseSource}`, {
          method: 'PATCH',
        });
        if (!res.ok) return;
        toast.info('🟢 IP engeli kaldırıldı.');
      } else {
        const response = await fetch('http://localhost:8080/api/ips/block', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            ipAddress: ipInfo.ipAddress || offenseSource,
            isp: ipInfo.isp,
            abuseScore: ipInfo.abuseConfidenceScore,
            countryCode: ipInfo.countryCode,
            domain: ipInfo.domain,
          }),
        });

        if (response.status === 409) {
          toast.warning('⚠️ Bu IP zaten engellenmiş!');
        } else if (response.ok) {
          toast.success('🚫 IP başarıyla engellendi!');
        }
      }
    } catch (err) {
      toast.error('❌ IP engelleme hatası!');
      console.error('IP engelleme hatası:', err);
    }
  };

  return (
    <div className="soc-container">
      <div className="soc-header-row">
        <h2>SOC Güvenlik Olayları</h2>
        <input
          type="text"
          className="soc-filter-input"
          placeholder="Subject ile filtrele..."
          value={filter}
          onChange={(e) => setFilter(e.target.value)}
        />
      </div>
      {filteredEmails.map((email) => {
        const offenseSource = email.offenseSource;
        const ipInfo = offenseSource ? ipDetails[offenseSource] : null;
        const isFlagged = flaggedIds.includes(email.id);

        return (
        <div key={email.id} className={`soc-card 
          ${email.running === 1 && expandedId === email.id ? 'running-self' : ''}
          ${email.running === 1 && expandedId !== email.id ? 'running-foreign' : ''}
        `}>

          {email.running === 1 && email.byUser && (
            <div className="running-user-divider">
              {email.byUser}
            </div>
          )}

            <div className="soc-summary" onClick={() => toggleAccordion(email.id, offenseSource)}>
              <div className="soc-avatar">
                {email.senderName ? email.senderName.charAt(0) : '?'}
              </div>
              <div className="soc-header-content">
              <span className="soc-subject">
                {isFlagged && <FaFlag className="soc-flag-icon" />} 
                {email.forward && <span className="forwarded-label">📤</span>} 
                {email.subject || 'Konu Yok'}
              </span>

                <span className="soc-alert">{email.alertName || 'Bilinmeyen Uyarı'}</span>
              </div>
              <div className="soc-menu-wrapper" onClick={(e) => e.stopPropagation()}>
                <FiMoreVertical className="soc-menu-icon" onClick={() => toggleMenu(email.id)} />
                {openMenuId === email.id && (
                  <div className="soc-dropdown">
                    <div onClick={() => handleFlag(email.id)}>{isFlagged ? '🚫 Bayrağı Kaldır' : '📌 Bayrak Ekle'}</div>
                    <div onClick={() => handleSolve(email.id)}>✅ Solved</div>
                    <div onClick={() => handleForward(email)}>📤 Forward</div>
                  </div>
                )}
              </div>
            </div>

            {expandedId === email.id && email.running === 1 && (
              <div className="soc-details">
                <div className="soc-section">
                  <h4>📄 Olay Bilgileri</h4>
                  <div className="soc-detail-grid">
                    {email.alertName && <div className="soc-detail-item"><span className="soc-key">Alert Name: </span><span className="soc-value">{email.alertName}</span></div>}
                    {email.logSource && <div className="soc-detail-item"><span className="soc-key">Log Source: </span><span className="soc-value">{email.logSource}</span></div>}
                    {email.severity && <div className="soc-detail-item"><span className="soc-key">Severity: </span><span className="soc-value">{email.severity}</span></div>}
                    {email.analyst && <div className="soc-detail-item"><span className="soc-key">Analyst: </span><span className="soc-value">{email.analyst}</span></div>}
                    {email.offenseTime && <div className="soc-detail-item"><span className="soc-key">Offense Time: </span><span className="soc-value">{email.offenseTime}</span></div>}
                    {email.offenseSource && <div className="soc-detail-item"><span className="soc-key">Offense Source: </span><span className="soc-value">{email.offenseSource}</span></div>}
                    {email.analystComment && <div className="soc-detail-item" style={{ gridColumn: '1 / -1' }}><span className="soc-key">Analyst Comment: </span><span className="soc-value" style={{ whiteSpace: 'pre-wrap' }}>{email.analystComment}</span></div>}
                  </div>
                  <div style={{ marginTop: '12px' }}>
                    <a href={email.webLink} target="_blank" rel="noopener noreferrer" className="soc-detail-link-btn">📎 Daha Fazla Detay</a>
                  </div>
                </div>

                {ipInfo && (
                  <div className="soc-section">
                    <h4>🧠 IP Detayları</h4>
                    <table className="soc-ip-table">
                      <thead>
                        <tr>
                          <th>abuseConfidenceScore</th>
                          <th>countryCode</th>
                          <th>usageType</th>
                          <th>isp</th>
                          <th>domain</th>
                          <th style={{ textAlign: 'center' }}>{offenseSource}</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td><span className={`dot ${ipInfo.abuseConfidenceScore >= 70 ? 'red' : ipInfo.abuseConfidenceScore >= 30 ? 'yellow' : 'green'}`}></span>{ipInfo.abuseConfidenceScore}</td>
                          <td>{ipInfo.countryCode}</td>
                          <td>{ipInfo.usageType}</td>
                          <td>{ipInfo.isp}</td>
                          <td>{ipInfo.domain}</td>
                          <td style={{ textAlign: 'left' }}>
                            <button
                              className={`soc-ip-block-btn-inside ${blockedIps.includes(offenseSource) ? 'unblock' : ''}`}
                              onClick={() => {
                                if (blockedIps.includes(offenseSource)) {
                                  setBlockedIps(prev => prev.filter(ip => ip !== offenseSource));
                                } else {
                                  setBlockedIps(prev => [...prev, offenseSource]);
                                }
                                handleBlockIp(ipInfo, offenseSource);
                              }}>
                              {blockedIps.includes(offenseSource) ? '🟢 Engeli Kaldır' : '🚫 IP’yi Engelle'}
                            </button>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                )}
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
        formLink: email.formLink,
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
          handleSolve(email.id); // ✅ form başarıyla gönderildiyse sil
        } else if (response.status === 208) {
          toast.warning("⚠️ " + message);
          handleSolve(email.id); // ✅ daha önce çözüldüyse yine sil
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

export default SOC;
