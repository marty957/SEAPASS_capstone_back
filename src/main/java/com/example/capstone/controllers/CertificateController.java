package com.example.capstone.controllers;


import com.example.capstone.enumaration.CertificateType;
import com.example.capstone.model.Certificate;
import com.example.capstone.payload.CertificateDTO;
import com.example.capstone.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/certificate")
@CrossOrigin (origins = "http://localhost:5174")
public class CertificateController {



    @Autowired
    CertificateService certificateService;


    @PostMapping(value = "/newCertificate/{idUser}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerCertificate(@PathVariable Long idUser,@RequestParam("name") String name,
                                                 @RequestParam("description") String description,
                                                 @RequestParam("issueDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate issueDate,
                                                 @RequestParam("expireDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expireDate,
                                                 @RequestParam("typeCert") CertificateType typeCert,
                                                 @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        CertificateDTO certificateDTO=new CertificateDTO(name,description,issueDate,expireDate,idUser,null,typeCert);

        // Carica il file
        if(file !=null && !file.isEmpty()) {
            String pdf = certificateService.uploadFile(file);
            certificateDTO.setPdf(pdf);
        }else{
            certificateDTO.setPdf(null);
        }
        // Crea il certificato
        Certificate certificate = certificateService.createCertificate(certificateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(certificate);
    }

    @GetMapping("{id}")
    public Certificate getCertifiateByID(@PathVariable long id){
     return certificateService.getById(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<?>  editCertificate(
            @PathVariable long id,
            @RequestBody CertificateDTO certificateDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        Certificate certificate=certificateService.updateCertificate(certificateDTO,id,file);
        return ResponseEntity.status(HttpStatus.OK).body(certificate);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable long id) throws IOException {
        certificateService.delelteCertificate(id);
        return ResponseEntity.status(HttpStatus.OK).body("certificato eliminato");
    }
//
}
