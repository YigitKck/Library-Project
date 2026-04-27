#Akıllı Kütüphane Yönetim ve Arama Sistemi

##  Proje Özeti

Bu proje, kullanıcıların Google Books API ile kitap arayabilmelerini, kiralayabilmelerini ve geri teslim etmelerini sağlayan bir kütüphane yönetim sistemi. Geciken kitaplar için otomatik e-posta hatırlatmaları gönderilir ve iade raporları oluşturulabilir.


##  Kullanılan Teknolojiler

- Java 21
- Spring Boot 3
- Maven
- PostgreSQL
- Lombok
- JavaFX
- Google Books API


##  Bağımlılıklar

Projede kullanılan başlıca bağımlılıklar şunlardır:

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Mail
- Spring Boot Starter Validation
- PostgreSQL JDBC Driver
- Lombok
- Apache POI (Excel ve PDF raporları için)
- JavaFX (Grafik arayüz için)
- JSON (org.json kütüphanesi)

## Kurulum ve Çalıştırma

### Gereksinimler:

- JDK 21 yüklü olmalı.
- Maven yüklü olmalı.
- PostgreSQL sunucusu çalışıyor olmalı.

### Çalıştırma Adımları:

1. Proje dosyalarını bilgisayarınıza alın.
2. IntelliJ IDEA ile projeyi açın.
3. `application.properties` veya `application.yml` dosyasındaki veritabanı ve mail ayarlarını kendi ortamınıza göre düzenleyin.
4. Maven ile bağımlılıkları yükleyin ve projeyi derleyin:
mvn clean install
5. Projeyi Spring Boot ile başlatmak için:
mvn spring-boot:run
