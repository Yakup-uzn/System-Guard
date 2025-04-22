package com.spam.mail.service;

import com.spam.mail.dto.AlertCountDTO;
import com.spam.mail.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertStatsService {

    private final EmailRepository emailRepository;

    public List<AlertCountDTO> getAlertNameStats() {
        List<Object[]> results = emailRepository.getAlertNameCounts();

        return results.stream()
                .map(row -> new AlertCountDTO(
                        (String) row[0],
                        (Long) row[1]
                ))
                .collect(Collectors.toList());
    }
}
