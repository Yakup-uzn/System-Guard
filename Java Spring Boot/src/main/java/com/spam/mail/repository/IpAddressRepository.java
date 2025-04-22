package com.spam.mail.repository;

import com.spam.mail.entity.IpAddress;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IpAddressRepository extends JpaRepository<IpAddress, Long> {
	Optional<IpAddress> findByIpAddress(String ipAddress);
	
    @Query("SELECT i.countryCode, COUNT(i) " +
            "FROM IpAddress i " +
            "WHERE i.status = false " +
            "GROUP BY i.countryCode")
     List<Object[]> getBlockedIpCountsByCountry();
 }



