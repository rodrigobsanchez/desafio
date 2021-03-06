package com.movierental.service;

import com.movierental.exception.BusinessException;
import com.movierental.model.User;
import com.movierental.repository.UserMovieRepository;
import com.movierental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    DataSource dataSource;

    private final UserRepository userRepository;
    private final UserMovieRepository userMovieRepository;

    public UserService(UserRepository userRepository, UserMovieRepository userMovieRepository) {
        this.userRepository = userRepository;
        this.userMovieRepository = userMovieRepository;
    }

    public String insertUser(User newUser) throws BusinessException {
        if(Objects.isNull(newUser)){
            throw new BusinessException("Objeto User do parametro newUser está null");
        }
        User user = userRepository.findByUsername(newUser.getUsername());
        if(user != null){
            return "O email " + newUser.getUsername() + " já está cadastrado. Inserir outro!";
        }else{
            boolean validName = validateName(newUser.getName());
            boolean validEmail = validateEmail(newUser.getUsername());
            boolean validPassword = validatePassword(newUser.getPassword());

            if(!validName){
                return "O nome está vazio ou inválido.";
            }
            if (!validEmail){
                return "Seu email não é válido";
            }
            if(!validPassword){
                return "O password está irregular. Deve ser menos de 32 caracteres ou está vazio";
            }
            newUser.setPassword(generatePassword(newUser.getPassword()));
            newUser.setRole("ROLE_USER");
            newUser.setEnable(true);

            userRepository.save(newUser);
            return "Novo cliente Cadastrado! Nome: "+ newUser.getName() + " Email: " + newUser.getUsername() +".";

        }
    }

    public String deleteUser(String userName) throws BusinessException {
        if(Objects.isNull(userName)){
            throw new BusinessException("Username is null, shouldn't be");
        }
        User user = userRepository.findByUsername(userName);
        userRepository.delete(user);
        return "O usuário: " + user.getUsername() + " foi deletado do sistema.";
    }

    public User findByUserName(String userName){
        return userRepository.findByUsername(userName);
    }

    private String generatePassword(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        return encodedPassword;
    }
    private boolean validateName(String name){
        if(name.isEmpty() || name == null){
            return false;
        }
        return true;
    }

    private boolean validateEmail(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
        return EMAIL_REGEX.matcher(email).matches();
    }

    private boolean validatePassword(String password){
        if(Objects.isNull(password) || password.isEmpty() || password.length() > 32){
            return false;
        }
        return true;
    }


}
