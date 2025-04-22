package com.spam.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IPInfo {
	//private boolean isPublic;
	//private boolean isWhitelisted;
	private int abuseConfidenceScore;
	private String countryCode;
	private String usageType;
	private String isp;
	private String domain;
	//private int totalReports;
	//private int numDistinctUsers;
	//private String lastReportedAt;

}
