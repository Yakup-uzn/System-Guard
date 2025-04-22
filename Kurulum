
# System-Guard – Backend ve Frontend Kurulum Rehberi 🚀

Merhaba,  
Bu rehber, **System-Guard** projesinin backend (Java Spring Boot) ve frontend (ReactJS) tarafını kendi bilgisayarında çalıştırabilmen için hazırlanmıştır.

Aşağıdaki adımları takip ederek uygulamayı eksiksiz şekilde ayağa kaldırabilirsin.

---

## ☕ Java Spring Boot – Backend Kurulum Rehberi (Windows)

### 🔧 Kurulum İçeriği:
- Java JDK 17
- IntelliJ IDEA (veya başka bir IDE)
- Apache Maven
- (Opsiyonel) Apache Tomcat

---

### ✅ 1. Java 17 Kurulumu

📥 [Java 17 Oracle Archive](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

1. “Windows x64 Installer” indir ve kur.
2. Ortam değişkeni oluştur:
   ```
   JAVA_HOME = C:\Program Files\Java\jdk-17
   ```
3. `Path` değişkenine şunu ekle:
   ```
   %JAVA_HOME%\bin
   ```

✅ Doğrulama:
```bash
java -version
javac -version
```

---

### ✅ 2. IntelliJ IDEA Kurulumu

📥 [IntelliJ IDEA Community](https://www.jetbrains.com/idea/download/)

1. IntelliJ IDEA kur.
2. File > Project Structure > SDKs > Add JDK kısmından `jdk-17` klasörünü tanıt.
3. Projenin bulunduğu klasördeki `pom.xml` dosyasını seçerek projeyi aç.
4. Sağ altta çıkan “Import Maven Project” uyarısını kabul et.

🔁 Alternatif IDE: Spring Tool Suite (STS)

---

### ✅ 3. Apache Maven Kurulumu

📥 [Maven İndir](https://maven.apache.org/download.cgi)

1. ZIP olarak indir.
2. `C:\Program Files\Apache\Maven` içine çıkar.
3. Ortam değişkenlerini ekle:
   ```
   MAVEN_HOME = C:\Program Files\Apache\Maven
   %MAVEN_HOME%\bin → Path’e ekle
   ```

✅ Doğrulama:
```bash
mvn -version
```

---

### ▶️ Uygulamayı Çalıştır

Proje dizininde terminal aç:

```bash
mvn spring-boot:run
```

veya

`src/main/java/.../Application.java` dosyasına sağ tıklayıp **Run**.

---

## ⚛️ React – Frontend Kurulum Rehberi

Bu bölümde ReactJS ile hazırlanmış frontend uygulamasının nasıl kurulup çalıştırılacağı yer alıyor.

---

### ✅ 1. Node.js ve npm Kurulumu

📥 [https://nodejs.org](https://nodejs.org)  
🔹 LTS sürümünü indir.

✅ Doğrulama:
```bash
node -v
npm -v
```

---

### ✅ 2. Projeyi Al

**ZIP ile teslim ettiysem:**
```bash
ZIP dosyasını çıkar → terminal aç → 
cd react-uygulama-klasoru
```

**Git ile aldıysan:**
```bash
git clone https://github.com/kullanici/proje-adi.git
cd proje-adi
```

---

### ✅ 3. Bağımlılıkları Kur

```bash
npm install
```

Bu komut, `package.json` dosyasına göre gerekli tüm modülleri indirir.

---

### ✅ 4. Uygulamayı Başlat

```bash
npm start
```

Tarayıcıda otomatik olarak açılmazsa:
```
http://localhost:3000
```

---

### ⚠️ Alternatif Scriptler

| Komut             | Açıklama                                      |
|------------------|-----------------------------------------------|
| `npm run build`  | Prodüksiyon için build klasörü oluşturur      |
| `npm test`       | Testleri çalıştırır                           |
| `npm run dev`    | Vite gibi dev ortamı (varsa) başlatır         |

---

### 💡 Ekstra Bilgiler

- `node_modules` klasörü paylaşılmaz, her zaman `npm install` ile yüklenir.
- `PORT` çatışması yaşarsan:
```bash
set PORT=3001 && npm start    # Windows
PORT=3001 npm start           # macOS/Linux
```

---

## ✅ Sistem Kontrol Listesi

| Kontrol                          | Durum  |
|----------------------------------|--------|
| Java 17 yüklendi mi?             | ✅     |
| Maven yüklendi mi?               | ✅     |
| `mvn spring-boot:run` çalıştı mı?| ✅     |
| Node.js ve npm yüklü mü?         | ✅     |
| `npm install` çalıştı mı?        | ✅     |
| `npm start` ile React açıldı mı? | ✅     |

---
