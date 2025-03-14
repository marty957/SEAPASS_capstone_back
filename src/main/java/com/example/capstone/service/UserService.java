package com.example.capstone.service;



import com.cloudinary.utils.ObjectUtils;
import com.example.capstone.config.CloudinaryConfig;
import com.example.capstone.enumaration.UserRole;
import com.example.capstone.exception.RoleNotFound;
import com.example.capstone.exception.UserNotFound;
import com.example.capstone.model.Role;
import com.example.capstone.model.User;
import com.example.capstone.payload.UserDTO;
import com.example.capstone.payload.mapper.UserDTOmapper;
import com.example.capstone.payload.request.RegistrationRequest;
import com.example.capstone.repository.RoleRepository;
import com.example.capstone.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDTOmapper userDTOmapper;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CloudinaryConfig cloudinaryConfig;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired EmailService emailService;


// metodi di inserimento nuovo utente: il primo con default user il secondo solo per admin.

    public User registerNewUser(RegistrationRequest request){

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email gia in uso");
        }
        if(userRepository.existsByUsername((request.getUsername()))){
            throw new IllegalStateException("Username Gia in uso");
        }

        User user= userDTOmapper.reg_entity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role userRole= roleRepository.findByRole(UserRole.USER)
                .orElseThrow(()->new RoleNotFound("ruolo non trovato"));
        Set<Role> defaultRole=new HashSet<>();
        defaultRole.add(userRole);
        user.setRoles(defaultRole);
        userRepository.save(user);
        emailService.sendNotifications(user.getEmail(),"Registrazione Avvenuta con successo", "Benvenuto in SEAPASS, " +
                "l'App pensata per i marittimi come te e i tuoi certificati. SEEPASS gestirà le scadenza dei tuoi certificati dandonti un mese di preavisso.");
        return user;
   }


   public User registerNewAdmin(UserDTO userDTO){
       if (userRepository.existsByEmail(userDTO.getEmail())) {
           throw new IllegalStateException("Email gia in uso");
       }
       User user= userDTOmapper.dto_entity(userDTO);
       Role adminRole= roleRepository.findByRole(UserRole.ADMIN)
               .orElseThrow(()->new RoleNotFound("ruolo non trovato"));
       Set<Role> adminRoles=new HashSet<>();

       adminRoles.add(adminRole);
       user.setRoles(adminRoles);
       userRepository.save(user);
      return user;

   }

   //modifica user:

    public User editUser(UserDTO userDTO,long id){

        User user=userRepository.findById(id).orElseThrow(()->new UserNotFound("utente non trovato"));

        if (userDTO.getEmail() != null){
            user.setEmail(userDTO.getEmail());
        }
        if(userDTO.getUsername() != null){
            user.setUsername(userDTO.getUsername());
        }
        if(userDTO.getName() != null){
            user.setName(userDTO.getName());
        }

        if(userDTO.getEmail()!= null){
            user.setEmail(userDTO.getEmail());
        }
        if(userDTO.getSurname() != null){
            user.setSurname(userDTO.getSurname());
        }

        userRepository.save(user);
        return user;

    }

   //metodi per la ricerca :

    public User getUserById(long id){
        return userRepository.findById(id).orElseThrow(()->new UserNotFound("l'utente è inesistente"));
    }
   // cancellazione:


    public String deleteUser(long id){
        userRepository.deleteById(id);
        return "user eliminato";
    }

    // IMMAGINE PROFILO


   public String uploadImage(long id,MultipartFile file) throws IOException {

       User user= userRepository.findById(id).orElseThrow(()->new UserNotFound("utente non trovato"));
        Map uploadResult= cloudinaryConfig.uploader().uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = (String) uploadResult.get("url");
        user.setAvatar(imageUrl);
        userRepository.save(user);
        return  imageUrl;
   }




}
