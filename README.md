# System-Guard
# ☕ Java Spring Boot Geliştirme Ortamı Kurulum Rehberi (Windows)

Bu rehberde sıfırdan bir Java Spring Boot geliştirme ortamı kuracağız. Kurulum adımlarında şunlar yer alır:

- Java JDK 17 kurulumu
- IntelliJ IDEA kurulumu (Spring IDE)
- Apache Maven kurulumu
- Apache Tomcat kurulumu (opsiyonel)
- Ortam değişkenleri
- Proje çalıştırma adımları

---

## ✅ 1. Java 17 Kurulumu

🔗 [Java 17 Oracle Archive](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

### 🔧 Kurulum Adımları:
1. "Windows x64 Installer" dosyasını indir ve çalıştır.
2. Kurulum yolunu not al (örnek: `C:\Program Files\Java\jdk-17`).
3. Kurulum tamamlandıktan sonra ortam değişkenlerini ayarla.

### ⚙️ Ortam Değişkeni Ayarı:
- `JAVA_HOME` oluştur:
  ```
  JAVA_HOME = C:\Program Files\Java\jdk-17
  ```
- `Path` değişkenine şunu ekle:
  ```
  %JAVA_HOME%\bin
  ```

### ✅ Kontrol:
```bash
java -version
javac -version
```

---

## ✅ 2. IntelliJ IDEA Kurulumu (Spring IDE)

🔗 [IntelliJ IDEA Community](https://www.jetbrains.com/idea/download/)

### 🧭 Kurulum Adımları:
1. Community Edition indir ve yükle.
2. İlk açılışta `JAVA_HOME` klasörünü tanıt:  
   File > Project Structure > SDKs > Add JDK → `C:\Program Files\Java\jdk-17`
3. Projeni `pom.xml` içeren klasörden aç.
4. Sağ altta çıkan `Maven project needs to be imported` uyarısına tıkla → Import Project.

### ⚙️ IDE Önerilen Ayarlar:
- **Java Compiler Level:** 17  
- **Encoding:** UTF-8  
- **Build Tool:** Maven  
- **Frameworks:** Spring Boot (otomatik algılanır veya elle eklenir)

### ▶️ Proje Çalıştırma:
`src/main/java/.../Application.java` → sağ tık → `Run 'Application'`

> Alternatif IDE:  
> 🔸 [Spring Tool Suite (STS)](https://spring.io/tools) (Eclipse tabanlı)

---

## ✅ 3. Apache Maven Kurulumu

🔗 [Maven İndir](https://maven.apache.org/download.cgi)

### 📦 Kurulum:
1. `apache-maven-*-bin.zip` dosyasını indir.
2. `C:\Program Files\Apache\Maven` klasörüne çıkar.

### ⚙️ Ortam Değişkeni Ayarı:
- `MAVEN_HOME` oluştur:
  ```
  MAVEN_HOME = C:\Program Files\Apache\Maven
  ```
- `Path` değişkenine şunu ekle:
  ```
  %MAVEN_HOME%\bin
  ```

### ✅ Kontrol:
```bash
mvn -version
```

---

## ✅ 4. Apache Tomcat Kurulumu (Opsiyonel)

🔗 [Tomcat 9 İndir](https://tomcat.apache.org/download-90.cgi)

### 🛠️ Adımlar:
1. ZIP dosyasını indir.
2. `C:\apache-tomcat-9.0.85` klasörüne çıkar.
3. `bin/startup.bat` dosyasını çalıştır.
4. Tarayıcıdan test et:
```
http://localhost:8080
```

> Tomcat için `CATALINA_HOME` değişkeni tanımlamak istersen:
```
CATALINA_HOME = C:\apache-tomcat-9.0.85
```

---

## ✅ 5. Ortam Değişkenleri Özeti

| Değişken Adı     | Değeri                                  |
|------------------|------------------------------------------|
| JAVA_HOME        | C:\Program Files\Java\jdk-17             |
| MAVEN_HOME       | C:\Program Files\Apache\Maven            |
| CATALINA_HOME    | C:\apache-tomcat-9.0.85                  |
| Path (ekle)      | %JAVA_HOME%\bin;<br>%MAVEN_HOME%\bin;    |

