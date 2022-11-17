package by.bsuir.sweetybear.model;

import by.bsuir.sweetybear.model.enums.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Entity
@Table(name = "users")
@Data
public class User extends IdentifiedModel implements UserDetails {

    @Column(name = "email", unique = true)
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;
    @Column(name = "name")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    @NotBlank(message = "Name is required")
    private String name;
    @Column(name = "active")
    private boolean active;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image avatar;
    @Column(name = "password", length = 1000)
    @NotBlank(message = "Password is required")
    private String password;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
    @Column(name = "date_Of_Created")
    private LocalDateTime dateOfCreated;
    @Column(name = "address")
    private String address;
    @Column(name = "activation_code")
    private String activationCode;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Bucket bucket;

    public boolean isAdmin() {
        return roles.contains(Role.ROLE_ADMIN) || roles.contains(Role.ROLE_OWNER);
    }

    public boolean isOwner() {
        return roles.contains(Role.ROLE_OWNER);
    }

    public boolean isAvatarNull() {
        return avatar != null;
    }

    public void addAvatarToUser(Image image) {
        image.setUser(this);
        this.avatar = image;
    }

    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
