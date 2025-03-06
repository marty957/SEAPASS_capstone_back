package com.example.capstone.payload.mapper;


import com.example.capstone.exception.RoleNotFound;
import com.example.capstone.model.Role;
import com.example.capstone.model.User;
import com.example.capstone.payload.UserDTO;
import com.example.capstone.payload.request.RegistrationRequest;
import com.example.capstone.repository.RoleRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Component
public class UserDTOmapper {

    public UserDTO entity_dto(User entity){

        UserDTO dto=new UserDTO();

        dto.setName(entity.getName());
        dto.setAvatar(entity.getAvatar());
        dto.setEmail(entity.getEmail());
        dto.setSurname(entity.getSurname());
        dto.setUsername(entity.getUsername());

        return dto;

    }


    public User dto_entity(UserDTO dto) {
        User entity = new User();

        entity.setAvatar(dto.getAvatar());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setSurname(dto.getSurname());
        entity.setUsername(dto.getUsername());

        return entity;
    }

    public User reg_entity(RegistrationRequest request){
        User entity=new User();
        entity.setAvatar(request.getAvatar());
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setSurname(request.getSurname());
       // entity.setPassword(request.getPassword());
        entity.setUsername(request.getUsername());
        return entity;
    }
}
