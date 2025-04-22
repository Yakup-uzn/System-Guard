package com.spam.mail.dto;

import com.spam.mail.entity.EmailEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//EmailEvent.java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailEvent {
 private String action; // "CREATE" veya "DELETE"
 private EmailEntity email;

 // getter, setter, constructor
}