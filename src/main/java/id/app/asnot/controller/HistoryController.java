package id.app.asnot.controller;

import id.app.asnot.config.BearerTokenUtil;
import id.app.asnot.service.AttendanceService;
import id.app.asnot.service.JwtService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AttendanceService attendanceService;

    @CrossOrigin("*")
    @GetMapping("/datas")
    public ResponseEntity<?> data(   @PathParam("date")  String date) {
        System.out.println("date : "+date);
        Long userId = jwtService.extractUserId(BearerTokenUtil.getBearerTokenHeader());
        return attendanceService.checkDataAll(userId, date);
    }

}
