// dto/ForwardUpdateDTO.java
package com.spam.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForwardUpdateDTO {
    private Long emailId;
    private boolean forward;
}
