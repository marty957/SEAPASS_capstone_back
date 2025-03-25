package com.example.capstone.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import com.example.capstone.exception.CertificateNotFound;
import com.example.capstone.exception.UserNotFound;
import com.example.capstone.model.Certificate;
import com.example.capstone.model.User;
import com.example.capstone.payload.CertificateDTO;
import com.example.capstone.payload.mapper.CertificateDTOmapper;
import com.example.capstone.repository.CertificateRepository;
import com.example.capstone.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CertificateService {
//

    @Autowired
    Cloudinary cloudinary;

    @Autowired
    CertificateRepository certificateRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CertificateDTOmapper certificateDTOmapper;

    @Autowired
    EmailService emailService;

    // creazione certificato

    public Certificate createCertificate(  CertificateDTO certificateDTO){

        User user=userRepository.findById(certificateDTO.getUserId()).orElseThrow(()->new UserNotFound("Utente non trovato"));

        Certificate certificate=certificateDTOmapper.dto_entity(certificateDTO);
        certificateRepository.save(certificate);
        return certificate;

    }


     //ricerca singola e di gruppo

    public Certificate getById(long id){
        return certificateRepository.findById(id).orElseThrow(()->new CertificateNotFound("Certificato non trovato"));
    }

    public Page<CertificateDTO> getAllCertificate(int page, int size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Certificate> certificatePage=certificateRepository.findAll(pageable);

        return certificatePage.map(certificateDTOmapper::entity_dto);

    }
    public List<Certificate> getAllCertificate(long id){
        return certificateRepository.findAllByUserId(id);
    }

    //modifica di un certificate

    public Certificate updateCertificate(CertificateDTO certificateDTO, long id,MultipartFile file) throws IOException {

    Certificate certificate=certificateRepository.findById(id)
            .orElseThrow(()->new CertificateNotFound("Certificato non trovato"));

    if(file!=null){
        String url= uploadFile(file);
        certificate.setPdf(url);
    }

    if(certificateDTO.getUserId()!=null){
        certificate.setUser(userRepository.findById(certificateDTO.getUserId()).
                orElseThrow(()->new UserNotFound("USER NON TROVATO")));
    }
    if(certificateDTO.getDescription()!=null){
        certificate.setDescription(certificateDTO.getDescription());
    }
    if(certificateDTO.getIssueDate()!=null){
        certificate.setIssueDate(certificateDTO.getIssueDate());
    }
    if(certificateDTO.getName()!=null){
        certificate.setName(certificateDTO.getName());
    }
    if(certificateDTO.getExpireDate()!=null){
        certificate.setExpireDate((certificateDTO.getExpireDate()));
    }
    if(certificateDTO.getTypeCert()!=null){
        certificate.setTypeCert(certificateDTO.getTypeCert());
    }

    certificateRepository.save(certificate);

       return certificate;


    }


    // eliminare un file e un certificato:

    public void delelteCertificate(long id ) throws IOException {

        Certificate certificate=certificateRepository.findById(id)
                .orElseThrow(()-> new CertificateNotFound("Certificato non presente in database"));

        if(certificate.getPdf()!= null){
            deletePdf(certificate.getPdf());
        }
        certificateRepository.deleteById(id);
    }





    //caricamento file e salvataggio su cloudinary
    public String uploadFile(MultipartFile file) throws IOException {

        Map uploadResult= cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type","auto","access_mode","public"));


        String pdfUrl= uploadResult.get("secure_url").toString();
        System.out.println("CLOUDINARY PDF url: " + pdfUrl);

        System.out.println("Upload result: " + uploadResult);
        return pdfUrl;
    }

    //eliminazione certificato pdf da cloudinary

    public void deletePdf(String url) throws IOException {
        System.out.println("URL del file su Cloudinary: " + url);
        String publicId=url.substring(url.lastIndexOf("/")+1, url.lastIndexOf("."));
        System.out.println("Public ID estratto: " + publicId);
        cloudinary.uploader().destroy(publicId,ObjectUtils.asMap("resource_type","image"));

    }


    //implementazione per verificare la scadenza di un certificato e per inviare un email

    public void checkCertificationExipiration(long certificateId,String email){

        Certificate certificate= certificateRepository.findById(certificateId)
                .orElseThrow(()->new CertificateNotFound("Certificato non trovato"));


        LocalDate expireDate=certificate.getExpireDate();
        LocalDate today= LocalDate.now();

        if(expireDate.isBefore(today.plusDays(30))){
            String subject="⚠️ il tuo certificato sta per scadere";
            String text="Ciao,\n\n"+
                    "Un promemoria veloce per te! 📌 Il tuo certificato " + certificate.getName() + " scade il:  " + expireDate + ". ⏳\n\n"  +
                    "Se hai bisogno di assistenza, siamo qui per aiutarti! 💡\n\n" +
                    "A presto,\nIl team di SEAPASS 🚀";

            emailService.sendNotifications(email,subject,text);
        }


    }

}
