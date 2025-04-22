# 🛡️ **SystemGuard: SOC Mail Yönetim ve Güvenlik Portalı**

**SystemGuard**, kurumların **Security Operations Center (SOC)** e-postalarını merkezi bir noktada analiz edebildiği, tehditleri otomatik olarak işleyebildiği ve anlık müdahale yapabildiği kullanıcı dostu bir **güvenlik yönetim portalıdır**.

Proje, Microsoft Graph API entegrasyonu ile SOC kaynaklarından gelen güvenlik maillerini işler, tehdit unsurlarını kategorilere ayırır ve IP analizleri ile detaylı içerik sunar. Gerçek zamanlı görüntüleme ve işlem yapma özelliği sayesinde güvenlik ekiplerinin iş yükünü azaltır.

---
## 📷 **Uygulama Görselleri**

![image](https://github.com/user-attachments/assets/d66a4d5c-76f8-4c24-9123-5a7e809ec2bc)

![image](https://github.com/user-attachments/assets/c53f7163-ec80-4a89-b7ce-9213e59db0ff)


![image](https://github.com/user-attachments/assets/e415de48-5d88-4f7d-af8c-8f0097557529)

![image](https://github.com/user-attachments/assets/b9ebe8fa-8ac6-414b-8b87-e5572fc90652)

![image](https://github.com/user-attachments/assets/f04dc50b-c469-4c14-a6fb-5098398af2ee)

---

## 📌 **Temel Özellikler**

### ✉️ SOC Mail Entegrasyonu
- Microsoft Graph API ile gelen güvenlik e-postaları sisteme otomatik olarak çekilir.
- "Alert Closed" olmayan mailler filtrelenerek veri tabanına kaydedilir.
- Mailler başlıklarına göre ayrıştırılır (IP, Domain, Analyst Comment, vb.).

### 🔍 Otomatik IP Analizi
- Mail içeriğindeki IP adresleri otomatik olarak tanınır.
- Abuse verileri (ISP, Geo, Domain, Score, Hostnames vb.) çekilerek ekrana yansıtılır.
- IP'ler whitelist/blacklist sistemine eklenebilir.

### 👤 Kullanıcı İşlem Takibi
- Her mail üzerinde kim çalışıyor bilgisi anlık olarak gösterilir.
- WebSocket altyapısı ile canlı güncelleme sağlanır.

### 📊 Dashboard ve Filtreleme
- Mail yoğunluğu, tehdit türleri ve kullanıcı bazlı etkileşimler dashboard’da analiz edilir.
- Gelen mailler tablo halinde filtrelenebilir şekilde listelenir.

---

## 💻 **Kullanılan Teknolojiler**

| Katman | Teknoloji |
|--------|-----------|
| **Backend** | Java Spring Boot, Spring Data JPA, WebSocket |
| **Frontend** | React JS |
| **Veritabanı** | MySQL |
| **Mail Servisi** | Microsoft Graph API |
| **Gerçek Zamanlılık** | STOMP WebSocket |
| **IP Analizi** | AbuseIPDB Entegrasyonu |

---



## 🚀 **Kurulum Adımları**

### 1. Gereksinimler
- Java 17+
- MySQL 8.x
- Node.js ve npm
- Microsoft Azure'da tanımlı bir uygulama (Graph API erişimi için)

### 2. Backend Kurulumu
```bash
cd backend
./mvnw clean install
