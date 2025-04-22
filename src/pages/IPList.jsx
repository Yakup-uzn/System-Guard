import React, { useEffect, useState } from "react";
import {
  FaGlobeAmericas,
  FaBuilding,
  FaExclamationTriangle,
  FaCheckCircle,
  FaTimesCircle,
  FaExchangeAlt,
  FaListUl,
  FaBan,
  FaInfoCircle,
} from "react-icons/fa";

const IPList = () => {
  const [ipData, setIpData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState("all");
  const [limit, setLimit] = useState(50);
  const [searchIP, setSearchIP] = useState("");
  const [searchISP, setSearchISP] = useState("");

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = () => {
    fetch("http://localhost:8080/api/ips")
      .then((res) => res.json())
      .then((data) => {
        setIpData(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Veri alınamadı:", err);
        setLoading(false);
      });
  };

  const toggleStatus = async (id) => {
    setIpData((prevData) =>
      prevData.map((item) =>
        item.id === id ? { ...item, status: !item.status } : item
      )
    );

    try {
      const response = await fetch("http://localhost:8080/api/ips/status", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ id }),
      });

      if (!response.ok) {
        console.error("API isteği başarısız. Durum geri alınıyor...");
        setIpData((prevData) =>
          prevData.map((item) =>
            item.id === id ? { ...item, status: !item.status } : item
          )
        );
      }
    } catch (error) {
      console.error("İstek sırasında hata:", error);
      setIpData((prevData) =>
        prevData.map((item) =>
          item.id === id ? { ...item, status: !item.status } : item
        )
      );
    }
  };

  const filteredData = ipData
    .filter((item) =>
      filter === "blocked"
        ? item.status === false
        : filter === "unblocked"
        ? item.status === true
        : true
    )
    .filter((item) =>
      item.ipAddress.toLowerCase().includes(searchIP.toLowerCase())
    )
    .filter((item) =>
      item.isp.toLowerCase().includes(searchISP.toLowerCase())
    )
    .slice(0, limit);

  return (
    <div style={containerStyle}>
      <h2 style={titleStyle}>💾 IP Listesi Yönetim Sayfası</h2>

      {/* Filtre Paneli */}
      <div style={filterBarStyle}>
        <div style={filterButtonGroup}>
          <button
            style={{
              ...filterButton,
              backgroundColor: filter === "all" ? "#3498db" : "#ecf0f1",
              color: filter === "all" ? "#fff" : "#2c3e50",
            }}
            onClick={() => setFilter("all")}
          >
            <FaListUl /> Tümü
          </button>
          <button
            style={{
              ...filterButton,
              backgroundColor: filter === "blocked" ? "#27ae60" : "#ecf0f1",
              color: filter === "blocked" ? "#fff" : "#2c3e50",
            }}
            onClick={() => setFilter("blocked")}
          >
            <FaCheckCircle /> Engellenenler
          </button>
          <button
            style={{
              ...filterButton,
              backgroundColor: filter === "unblocked" ? "#c0392b" : "#ecf0f1",
              color: filter === "unblocked" ? "#fff" : "#2c3e50",
            }}
            onClick={() => setFilter("unblocked")}
          >
            <FaBan /> Engellenmeyenler
          </button>
        </div>

        <select
          style={dropdownStyle}
          value={limit}
          onChange={(e) => setLimit(Number(e.target.value))}
        >
          <option value={50}>Son 50 Kayıt</option>
          <option value={100}>Son 100 Kayıt</option>
          <option value={200}>Son 200 Kayıt</option>
        </select>

        <input
          type="text"
          placeholder="IP adresini arayın..."
          value={searchIP}
          onChange={(e) => setSearchIP(e.target.value)}
          style={searchInput}
        />
        <input
          type="text"
          placeholder="ISP'yi arayın..."
          value={searchISP}
          onChange={(e) => setSearchISP(e.target.value)}
          style={searchInput}
        />
      </div>

      {/* Bilgilendirme */}
      <p style={infoStyle}>
        <FaInfoCircle /> Toplam <strong>{ipData.length}</strong> kayıt bulundu.{" "}
        Son <strong>{filteredData.length}</strong> kayıt gösteriliyor.
      </p>

      {/* Tablo */}
      {loading ? (
        <p style={loadingStyle}>Yükleniyor...</p>
      ) : (
        <div style={tableContainer}>
          <table style={tableStyle}>
            <thead>
              <tr>
                <th style={thStyle}>#</th>
                <th style={thStyle}>IP Adresi</th>
                <th style={thStyle}>
                  <FaBuilding /> ISP
                </th>
                <th style={thStyle}>
                  <FaExclamationTriangle /> Skor
                </th>
                <th style={thStyle}>
                  <FaGlobeAmericas /> Ülke
                </th>
                <th style={thStyle}>Alan Adı</th>
                <th style={thStyle}>Sorgu Tarihi</th>
                <th style={thStyle}>Durum</th>
                <th style={thStyle}>İşlem</th>
              </tr>
            </thead>
            <tbody>
              {filteredData.map((ip, index) => (
                <tr
                  key={ip.id}
                  style={{
                    ...trStyle,
                    backgroundColor: index % 2 === 0 ? "#ffffff" : "#f9f9f9",
                  }}
                >
                  <td style={tdStyle}>{ip.id}</td>
                  <td style={tdStyle}>{ip.ipAddress}</td>
                  <td style={tdStyle}>{ip.isp}</td>
                  <td style={tdStyle}>
                    <span
                      style={{
                        ...badgeStyle,
                        backgroundColor:
                          ip.abuseScore >= 80
                            ? "#e74c3c"
                            : ip.abuseScore >= 40
                            ? "#f39c12"
                            : "#2ecc71",
                      }}
                    >
                      {ip.abuseScore}
                    </span>
                  </td>
                  <td style={tdStyle}>{ip.countryCode}</td>
                  <td style={tdStyle}>{ip.domain}</td>
                  <td style={tdStyle}>
                    {new Date(ip.queryDate).toLocaleString("tr-TR")}
                  </td>
                  <td style={tdStyle}>
                    {ip.status ? (
                      <span style={{ color: "#27ae60", fontWeight: "bold" }}>
                        <FaCheckCircle /> Engelsiz
                      </span>
                    ) : (
                      <span style={{ color: "#c0392b", fontWeight: "bold" }}>
                        <FaTimesCircle /> Engelli
                      </span>
                    )}
                  </td>
                  <td style={tdStyle}>
                    <button
                      style={buttonStyle}
                      onClick={() => toggleStatus(ip.id)}
                    >
                      <FaExchangeAlt /> 
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

// 🎨 Stil tanımları
const containerStyle = {

  fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
  backgroundColor: "",
  maxHeight: "100vh",

};

const titleStyle = {
  fontSize: "28px",
  fontWeight: "600",
  textAlign: "center",
  marginBottom: "30px",
  color: "#2c3e50",
};

const loadingStyle = {
  textAlign: "center",
  fontSize: "18px",
};


const tableContainer = {
  maxHeight: "460px", // 👈 Yaklaşık 13 satır yüksekliği
  overflowY: "auto",
  backgroundColor: "#fff",
  borderRadius: "15px",
  boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
};

const tableStyle = {
  width: "100%",
  borderCollapse: "collapse",
  borderRadius: "12px",
  overflow: "hidden",
};

const thStyle = {
  padding: "14px",
  backgroundColor: "#2c3e50",
  color: "#ecf0f1",
  fontWeight: "600",
  fontSize: "14px",
  textAlign: "left",
};

const trStyle = {
  transition: "background-color 0.2s ease-in-out",
};

const tdStyle = {
  padding: "12px 14px",
  fontSize: "14px",
  color: "#34495e",
};

const badgeStyle = {
  color: "white",
  padding: "4px 10px",
  borderRadius: "8px",
  fontWeight: "bold",
  fontSize: "12px",
};

const buttonStyle = {
  backgroundColor: "#2980b9",
  color: "#fff",
  border: "none",
  padding: "6px 10px",
  borderRadius: "6px",
  cursor: "pointer",
  fontSize: "13px",
  display: "flex",
  alignItems: "center",
  gap: "6px",
};

const filterBarStyle = {
  display: "flex",
  flexWrap: "wrap",
  gap: "10px",
  marginBottom: "20px",
  alignItems: "center",
};

const filterButtonGroup = {
  display: "flex",
  gap: "10px",
};

const filterButton = {
  display: "flex",
  alignItems: "center",
  gap: "6px",
  padding: "8px 14px",
  borderRadius: "6px",
  border: "none",
  cursor: "pointer",
  fontSize: "14px",
};

const dropdownStyle = {
  padding: "8px",
  borderRadius: "6px",
  border: "1px solid #ccc",
  fontSize: "14px",
};

const searchInput = {
  padding: "8px",
  borderRadius: "6px",
  border: "1px solid #ccc",
  fontSize: "14px",
  flex: "1",
  minWidth: "180px",
};

const infoStyle = {
  fontSize: "14px",
  color: "#2c3e50",
  marginBottom: "16px",
};

export default IPList;
