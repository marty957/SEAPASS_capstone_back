package com.example.capstone.service.schedulerEmail;


import com.example.capstone.model.Certificate;
import com.example.capstone.repository.CertificateRepository;
import com.example.capstone.repository.UserRepository;
import com.example.capstone.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CertificateExpirationScheduler {


    @Autowired
    CertificateService certificateService;

    @Autowired
    CertificateRepository certificateRepository;

    @Autowired
    UserRepository userRepository;

    @Scheduled(cron = "0 0 9 * * ?")//controlla ogni giorno alle 9 AM
    public void checkExpiredCertificates(){

        System.out.println("✅ Avvio controllo scadenza certificati");
        List<Certificate> certificateList=certificateRepository.findAll();
        if(certificateList.isEmpty()){
            System.out.println("✅ Nessuno certificato trovato");
        }else{

        for(Certificate certificate: certificateList){
            System.out.println("Controllo certificato ID: " + certificate.getId() + "per: " +certificate.getUser().getEmail() );

            certificateService.checkCertificationExipiration(certificate.getId(),certificate.getUser().getEmail());
        }}
        System.out.println("✔️ Controllo scadenza certificati completato.");
    }
}
