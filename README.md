# 🛡️ **SystemGuard: SOC Mail Yönetim ve Güvenlik Portalı**

**SystemGuard**, kurumların **Security Operations Center (SOC)** e-postalarını merkezi bir noktada analiz edebildiği, tehditleri otomatik olarak işleyebildiği ve anlık müdahale yapabildiği kullanıcı dostu bir **güvenlik yönetim portalıdır**.

Proje, Microsoft Graph API entegrasyonu ile SOC kaynaklarından gelen güvenlik maillerini işler, tehdit unsurlarını kategorilere ayırır ve IP analizleri ile detaylı içerik sunar. Gerçek zamanlı görüntüleme ve işlem yapma özelliği sayesinde güvenlik ekiplerinin iş yükünü azaltır.

---
## 📷 **Uygulama Görselleri**

![image](![1](https://github.com/user-attachments/assets/db4fb5ea-4c90-457e-bfd1-d8dd0722b9ba))


![image](![2](https://github.com/user-attachments/assets/c14aba0c-91ca-46b2-a631-690dc88af908)

![image](![3](https://github.com/user-attachments/assets/3539f366-26f9-47ae-99bb-726ce3a30c5b)

![image](![4](https://github.com/user-attachments/assets/39abb976-2c8b-49f4-ab4d-842f8d00704d)

![image](![6](https://github.com/user-attachments/assets/9bb27e63-e2a4-44b1-8810-8d8743e55cc3)


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
