# 🛡️ **SystemGuard: SOC Mail Yönetim ve Güvenlik Portalı**

**SystemGuard**, kurumların **Security Operations Center (SOC)** e-postalarını merkezi bir noktada analiz edebildiği, tehditleri otomatik olarak işleyebildiği ve anlık müdahale yapabildiği kullanıcı dostu bir **güvenlik yönetim portalıdır**.

Proje, Microsoft Graph API entegrasyonu ile SOC kaynaklarından gelen güvenlik maillerini işler, tehdit unsurlarını kategorilere ayırır ve IP analizleri ile detaylı içerik sunar. Gerçek zamanlı görüntüleme ve işlem yapma özelliği sayesinde güvenlik ekiplerinin iş yükünü azaltır.

---
## 📷 **Uygulama Görselleri**

![1](https://github.com/user-attachments/assets/721c51ac-7f69-4537-b72c-268d17140643)


![2](https://github.com/user-attachments/assets/826b1398-7094-4018-8a1d-968b06abea7f)


![3](https://github.com/user-attachments/assets/e817d749-2700-4e96-8150-b538b16ba3f6)


![4](https://github.com/user-attachments/assets/3f75a292-c4a2-4c08-b687-a165a5b4f0a3)


![6](https://github.com/user-attachments/assets/1a594fff-57ce-447e-b953-d390ed70c0f5)



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
