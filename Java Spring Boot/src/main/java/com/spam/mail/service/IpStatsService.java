package com.spam.mail.service;

import com.spam.mail.dto.CountryAttackStatsDTO;
import com.spam.mail.repository.IpAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IpStatsService {

    private final IpAddressRepository ipAddressRepository;

    public List<CountryAttackStatsDTO> getBlockedIpStatsByCountry() {
        List<Object[]> results = ipAddressRepository.getBlockedIpCountsByCountry();

        return results.stream()
                .map(obj -> new CountryAttackStatsDTO(
                        (String) obj[0],
                        (Long) obj[1]
                ))
                .collect(Collectors.toList());
    }
}
