package id.app.asnot.controller;

import id.app.asnot.model.entity.User;
import id.app.asnot.model.request.UserResponse;
import id.app.asnot.model.response.UserRequest;
import id.app.asnot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @CrossOrigin("*")
    @GetMapping("/all-user")
    public List<UserResponse> getUser() {
      return  userService.findAll();
    }
    @CrossOrigin("*")
    @PostMapping("/set-access")
    public ResponseEntity<?> userAccess(@RequestBody UserResponse userRequest) {
        return userService.setAccess(userRequest);
    }
}
