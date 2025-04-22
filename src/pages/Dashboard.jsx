import React, { useEffect, useState } from 'react';
import {
  FaEnvelopeOpenText,
  FaFileAlt,
  FaExclamationTriangle,
  FaChartPie
} from 'react-icons/fa';
import {
  PieChart,
  Pie,
  Cell,
  ResponsiveContainer,
  Tooltip,
  Legend
} from 'recharts';
import '../css/Dashboard.css';
import axios from 'axios';

const COLORS = ['#34495e', '#b71c1c', '#c2b280', '#8c1414', '#27ae60', '#2980b9'];

const Dashboard = () => {
  const [chartData, setChartData] = useState([]);
  const [alertStats, setAlertStats] = useState([]);

  // Engellenmiş IP'leri çek
  useEffect(() => {
    axios
      .get('http://localhost:8080/api/ip-stats/blocked')
      .then((res) => {
        const transformedData = res.data.map((item) => ({
          name: item.countryCode || 'Bilinmeyen',
          value: item.attackCount
        }));
        setChartData(transformedData);
      })
      .catch((err) => {
        console.error('IP API verisi alınamadı:', err);
      });

    // Alert name'leri çek
    axios
      .get('http://localhost:8080/api/alerts/counts')
      .then((res) => {
        setAlertStats(res.data);
      })
      .catch((err) => {
        console.error('Alert API verisi alınamadı:', err);
      });
  }, []);

  return (
    <div className="dashboard-container">
      <h2 className="dashboard-title">Genel Durum Paneli</h2>

      <div className="dashboard-cards">
        <div className="card card-email">
          <FaEnvelopeOpenText className="card-icon" />
          <h3>Gelen SOC Mailleri</h3>
          <p>150</p>
        </div>

        <div className="card card-alert">
          <FaExclamationTriangle className="card-icon" />
          <h3>Wazuh Uyarıları</h3>
          <p>90</p>
        </div>

        <div className="card card-log">
          <FaFileAlt className="card-icon" />
          <h3>Sysmon Logları</h3>
          <p>240</p>
        </div>

        <div className="card card-pending">
          <FaChartPie className="card-icon" />
          <h3>Bekleyen İşlemler</h3>
          <p>60</p>
        </div>
      </div>

      <div className="dashboard-chart">
        <h3 style={{ textAlign: 'center' }}>Engellenen IP'ler - Ülke Dağılımı</h3>
        <ResponsiveContainer width="100%" height={300}>
          <PieChart>
            <Pie
              data={chartData}
              cx="50%"
              cy="50%"
              innerRadius={60}
              outerRadius={100}
              fill="#8884d8"
              paddingAngle={5}
              dataKey="value"
              label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
            >
              {chartData.map((entry, index) => (
                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
              ))}
            </Pie>
            <Tooltip />
            <Legend verticalAlign="bottom" height={36} />
          </PieChart>
        </ResponsiveContainer>
      </div>

      <div className="dashboard-alert-table">
        <h3 style={{ textAlign: 'center', marginTop: '30px' }}>📋 Alert Türlerine Göre Dağılım</h3>
        <table className="alert-table">
          <thead>
            <tr>
              <th>Alert Adı</th>
              <th>Gelen Sayı</th>
            </tr>
          </thead>
          <tbody>
            {alertStats.map((alert, index) => (
              <tr key={index}>
                <td>{alert.alertName || 'Bilinmeyen'}</td>
                <td>{alert.count}</td>
                
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Dashboard;
