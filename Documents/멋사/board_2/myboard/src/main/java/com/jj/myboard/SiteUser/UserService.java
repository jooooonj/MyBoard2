package com.jj.myboard.SiteUser;

import com.jj.myboard.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public SiteUser addUser(String username, String password, String name, String email){
        SiteUser user = SiteUser
                .builder()
                .username(username)
                .name(name)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();

        return userRepository.save(user);
    }

    public SiteUser getUser(long id){
        Optional<SiteUser> _user = userRepository.findById(id);

        if (_user.isEmpty()) {
            throw new DataNotFoundException("user is not found");
        }

        return _user.get();
    }

    public SiteUser getUser(String username){
        Optional<SiteUser> _user = userRepository.findByUsername(username);

        if (_user.isEmpty()) {
            throw new DataNotFoundException("user is not found");
        }

        return _user.get();
    }

}
