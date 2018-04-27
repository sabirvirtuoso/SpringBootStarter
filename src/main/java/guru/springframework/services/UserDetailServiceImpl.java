package guru.springframework.services;

import guru.springframework.converters.UserToUserDetails;
import guru.springframework.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private UserService userService;
    private UserToUserDetails userToUserDetails;

    public UserDetailServiceImpl(UserService userService, UserToUserDetails userToUserDetails) {
        this.userService = userService;
        this.userToUserDetails = userToUserDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userToUserDetails.convert(userService.findByUserName(s));
    }
}
