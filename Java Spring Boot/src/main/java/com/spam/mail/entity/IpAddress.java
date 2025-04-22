package com.spam.mail.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "ip_address_list")
@Data // Getter, Setter, toString, equals, hashCode otomatik oluşturur
@NoArgsConstructor // Parametresiz constructor
@AllArgsConstructor // Tüm alanları içeren constructor
@Builder // Builder pattern desteği
public class IpAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip_address", nullable = false, unique = true)
    private String ipAddress; // IP Address

    @Column(name = "isp")
    private String isp; // Internet Service Provider

    @Column(name = "abuse_score")
    private Integer abuseScore; // Abuse Score

    @Column(name = "country_code", length = 2)
    private String countryCode; // Country Code (ISO format)



    @Column(name = "query_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date queryDate; // Query Date

    @Column(name = "domain")
    private String domain; // Domain name

    @Column(name = "status", columnDefinition = "BOOLEAN DEFAULT 1")
    private Boolean status; // Status (0: pasif, 1: aktif)
}
