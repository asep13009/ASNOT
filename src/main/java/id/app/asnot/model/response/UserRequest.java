package id.app.asnot.model.response;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String name;
    private String email;
    private String role;

}
