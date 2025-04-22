package com.spam.mail.service;

import com.spam.mail.entity.IpAddress;
import com.spam.mail.repository.IpAddressRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IpAddressService {

    private final IpAddressRepository ipAddressRepository;

    public IpAddressService(IpAddressRepository ipAddressRepository) {
        this.ipAddressRepository = ipAddressRepository;
    }

    public List<IpAddress> getAllIps() {
        return ipAddressRepository.findAll();
    }

    @Transactional
    public void toggleIpStatusById(long id) {
        Optional<IpAddress> optionalIp = ipAddressRepository.findById(id);
        optionalIp.ifPresent(ip -> {
            ip.setStatus(ip.getStatus() != null && !ip.getStatus());
            ipAddressRepository.save(ip);
        });
    }

    @Transactional
    public boolean unblockIpByAddress(String ipAddress) {
        Optional<IpAddress> optionalIp = ipAddressRepository.findByIpAddress(ipAddress);
        if (optionalIp.isPresent()) {
            IpAddress ip = optionalIp.get();
            ip.setStatus(true);
            ip.setQueryDate(new Date());
            ipAddressRepository.save(ip);
            return true;
        }
        return false;
    }

    public Optional<IpAddress> findByIpAddress(String ip) {
        return ipAddressRepository.findByIpAddress(ip);
    }

    public IpAddress saveIpInfo(IpAddress ipAddress) {
        return ipAddressRepository.findByIpAddress(ipAddress.getIpAddress())
                .orElseGet(() -> ipAddressRepository.save(ipAddress));
    }

    public IpAddress save(IpAddress ipAddress) {
        return ipAddressRepository.save(ipAddress);
    }
}