package guru.springframework.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptionServiceImpl implements EncryptionService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public EncryptionServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public String encryptString(String input) {
        return bCryptPasswordEncoder.encode(input);
    }

    @Override
    public boolean checkPassword(String plainPassword, String encryptedPassword) {
        return bCryptPasswordEncoder.matches(plainPassword, encryptedPassword);
    }
}
