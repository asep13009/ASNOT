package id.app.asnot.model.request;

import id.app.asnot.model.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    @NotBlank
    @Column(unique = true)
    private String username;
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
}
