package com.jj.myboard.security;

import com.jj.myboard.SiteUser.SiteUser;
import com.jj.myboard.SiteUser.UserRepository;
import com.jj.myboard.SiteUser.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> _user = userRepository.findByUsername(username);
        if (_user.isEmpty()) {
            throw new UsernameNotFoundException("해당 id는 없는 아이디입니다.");
        }

        SiteUser user = _user.get();

        List<GrantedAuthority> authorities = new ArrayList<>(); //권한
        if(username.equals("admin"))
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        else
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));

        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
