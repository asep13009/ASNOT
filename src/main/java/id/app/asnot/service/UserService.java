package id.app.asnot.service;

import id.app.asnot.model.entity.User;
import id.app.asnot.model.request.UserResponse;
import id.app.asnot.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserResponse> findAll() {
        List<UserResponse> users = new ArrayList<UserResponse>();
        List<User> usersAll = userRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();
        usersAll.forEach(user -> {users.add(modelMapper.map(user, UserResponse.class));});
        return  users;

    }

    public ResponseEntity<?> setAccess(UserResponse userRequest) {

        Map<String, Object> response = new HashMap<String, Object>();
        try{
            User user = userRepository.findById(userRequest.getId()).get();
            user.setRole(userRequest.getRole());
            userRepository.save(user);
            response.put("status", "success");
            response.put("user", userRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("user", userRequest);
            return ResponseEntity.internalServerError().body(response);
        }

    }
}
