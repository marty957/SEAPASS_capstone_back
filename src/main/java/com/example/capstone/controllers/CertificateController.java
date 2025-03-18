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
import java.util.List;

@RestController
@RequestMapping("/api/certificate")
@CrossOrigin (origins = "http://localhost:5173")
public class CertificateController {



    @Autowired
    CertificateService certificateService;


    @PostMapping(value = "/{id}/new",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerCertificate(@PathVariable Long id,@RequestParam("name") String name,
                                                 @RequestParam("description") String description,
                                                 @RequestParam("issueDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate issueDate,
                                                 @RequestParam("expireDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expireDate,
                                                 @RequestParam(value = "typeCert",required = false) CertificateType typeCert,
                                                 @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        CertificateDTO certificateDTO=new CertificateDTO(name,description,issueDate,expireDate,id,null,typeCert);

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

    @GetMapping("/{id}/all")
    public List<Certificate> getAll(@PathVariable long id){

        return certificateService.getAllCertificate(id);
    }

    @PutMapping( value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?>  editCertificate(
            @PathVariable long id,
            @RequestParam(value = "name" ) String name,
            @RequestParam(value = "userId") long userId,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "typeCert",required = false) CertificateType typeCert,
            @RequestParam(value = "issueDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate issueDate,
            @RequestParam(value = "expireDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expireDate,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        CertificateDTO certificateDTO=new CertificateDTO(name,description,issueDate,expireDate,userId,null,typeCert);
        if(file !=null && !file.isEmpty()) {
            String pdf = certificateService.uploadFile(file);
            certificateDTO.setPdf(pdf);
        }else{
            certificateDTO.setPdf(null);
        }

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
