package com.example.capstone.payload.mapper;


import com.example.capstone.model.Certificate;
import com.example.capstone.payload.CertificateDTO;
import com.example.capstone.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class CertificateDTOmapper {

    @Autowired
    UserRepository userRepository;


    public CertificateDTO entity_dto(Certificate entity){

        CertificateDTO dto=new CertificateDTO();
        dto.setDescription(entity.getDescription());
        dto.setName(entity.getName());
        dto.setExpireDate(entity.getExpireDate());
        dto.setIssueDate(entity.getIssueDate());
        dto.setUserId(entity.getUser().getId());
        dto.setPdf(entity.getPdf());
        dto.setTypeCert(entity.getTypeCert());

        return dto;
    }


    public Certificate dto_entity(CertificateDTO  dto){

        Certificate entity=new Certificate();
        if(dto.getPdf() != null){
            entity.setPdf(dto.getPdf());
        }
        entity.setTypeCert(dto.getTypeCert());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUser(userRepository.findById(dto.getUserId()).orElseThrow());
        entity.setIssueDate(dto.getIssueDate());
        entity.setExpireDate(dto.getExpireDate());

        return entity;
    }
}
