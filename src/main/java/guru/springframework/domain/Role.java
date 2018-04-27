package guru.springframework.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private String role;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
        }

        if (!user.getRoles().contains(this)) {
            user.getRoles().add(this);
        }
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getRoles().remove(this);
    }
}
