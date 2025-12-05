package id.app.asnot.model.request;

import id.app.asnot.model.entity.User;
import id.app.asnot.model.response.UserRequest;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String role;
    private UserRequest user;

    public LoginResponse(String token, String name, UserRequest user) {
        this.token = token;
        this.role = name;
        this.user = user;

    }
}
