# 🛡️ **SystemGuard: SOC Mail Yönetim ve Güvenlik Portalı**

**SystemGuard**, kurumların **Security Operations Center (SOC)** e-postalarını merkezi bir noktada analiz edebildiği, tehditleri otomatik olarak işleyebildiği ve anlık müdahale yapabildiği kullanıcı dostu bir **güvenlik yönetim portalıdır**.

Proje, Microsoft Graph API entegrasyonu ile SOC kaynaklarından gelen güvenlik maillerini işler, tehdit unsurlarını kategorilere ayırır ve IP analizleri ile detaylı içerik sunar. Gerçek zamanlı görüntüleme ve işlem yapma özelliği sayesinde güvenlik ekiplerinin iş yükünü azaltır.


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

## 📷 **Uygulama Görselleri**

![1](https://github.com/user-attachments/assets/d3d72adb-41da-4deb-8052-9a496e6d07c5)
![2r](https://github.com/user-attachments/assets/653a298c-6c84-4729-8bfc-222839c11d2d)
![3r](https://github.com/user-attachments/assets/3fa83a43-9a7f-4b70-bcea-c054ae2e7809)
![4r](https://github.com/user-attachments/assets/efbd9255-862b-4ed2-bb60-8d6e082f833d)
![6](https://github.com/user-attachments/assets/da6cffcd-58b5-49ba-8dc4-a0a3567fd96e)


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
