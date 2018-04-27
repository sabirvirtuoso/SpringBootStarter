package guru.springframework.converters;

import guru.springframework.DTO.UserDetailsImpl;
import guru.springframework.domain.User;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class UserToUserDetails implements Converter<User, UserDetails> {

    @Synchronized
    @Override
    @Nullable
    public UserDetails convert(User source) {
        if (source == null) {
            return null;
        }

        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setUserName(source.getUserName());
        userDetails.setPassword(source.getEncryptedPassword());
        userDetails.setEnabled(source.getEnabled());

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        source.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRole())));
        userDetails.setAuthorities(authorities);

        return userDetails;
    }
}
