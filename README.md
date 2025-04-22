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

## 📷 **Uygulama Görselleri**

> _(Bu alana uygulamadan ekran görüntüleri ekleyebilirsiniz. Örnek: mail listesi, detay görünümü, IP analizi, dashboard vs.)_

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
