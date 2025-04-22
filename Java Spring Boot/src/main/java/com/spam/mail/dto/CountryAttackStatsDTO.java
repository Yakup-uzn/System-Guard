package com.spam.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountryAttackStatsDTO {
    private String countryCode;
    private Long attackCount;
}
