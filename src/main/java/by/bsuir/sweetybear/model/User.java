package by.bsuir.sweetybear.model;

import by.bsuir.sweetybear.annotation.UsernameValid;
import by.bsuir.sweetybear.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Entity
@Table(name = "users")
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class User extends IdentifiedModel implements UserDetails {
    @Column(name = "email", unique = true)
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;
    @Column(name = "name")
    @UsernameValid
    private String name;
    @Column(name = "active")
    private boolean active;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image avatar;
    @Column(name = "password", length = 1000)
    private String password;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
    @Column(name = "date_Of_Created")
    private LocalDateTime dateOfCreated;
    @Column(name = "activation_code")
    private String activationCode;
    @Column(name = "reset_password_code")
    private String resetPasswordCode;

    @OneToOne(cascade = CascadeType.ALL)
    private Bucket bucket;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<BankCard> bankCards = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "users_favorite_products",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> favoriteProducts = new ArrayList<>();

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

    public void addProductToFavorites(Product product) {
        favoriteProducts.add(product);
    }

    public void removeProductFromFavorites(Product product) {
        favoriteProducts.remove(product);
    }

    public List<Long> getFavoriteProductsIds() {
        return favoriteProducts.stream()
                .map(Product::getId)
                .toList();
    }

    public void addBankCardToUser(BankCard bankCard) {
        bankCard.setUser(this);
        bankCards.add(bankCard);
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
