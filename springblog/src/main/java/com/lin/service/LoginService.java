package com.lin.service;
import com.lin.entity.User;
import com.lin.repository.ConfirmationTokenRepository;
import com.lin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    // Use spring-security for password encryption
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public String loginUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null ) {
            //Email is not activated
            if (existingUser.getIsEnabled()==0) {
                return ("The email not verified");
            }
            //Sign in successfully
            else if (encoder.matches(user.getPassword(), existingUser.getPassword())){
                return ("sucess");
            }
            //Password error
            else {
                return ("Incorrect password. Try again");
            }
        }
        //Unregistered
        else
            return "Your Acount don't exist, Please register!";
    }
}
