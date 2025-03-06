package com.example.capstone.payload;


import com.example.capstone.enumaration.CertificateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateDTO {

    private String name;
    private String description;
    private LocalDate issueDate;
    private LocalDate expireDate;
    private Long userId;
    private String pdf;
    private CertificateType typeCert;


}
