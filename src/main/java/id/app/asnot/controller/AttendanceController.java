package id.app.asnot.controller;

import id.app.asnot.config.BearerTokenUtil;
import id.app.asnot.service.AttendanceService;
import id.app.asnot.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private JwtService jwtService;

    @CrossOrigin("*")
    @PostMapping("/checkin")
    public ResponseEntity<?> checkIn(@RequestParam("photo") MultipartFile photo,
                                     @RequestParam("latitude") Double latitude,
                                     @RequestParam("longitude") Double longitude
                                     ) throws IOException {
        System.out.println("checkin >>"+latitude+","+longitude);
        System.out.println("photo >>"+photo.getOriginalFilename());
        byte[] fileBytes = photo.getBytes();

        // 2. Encode the byte array to Base64
        String base64EncodedString = Base64.getEncoder().encodeToString(fileBytes);
        Long userId = jwtService.extractUserId(BearerTokenUtil.getBearerTokenHeader());
        if (!isWithinRadius(latitude, longitude)) {
            return ResponseEntity.badRequest().body("Lokasi di luar area absensi");
        }
        String photoUrl = savePhoto(photo);
        return attendanceService.checkIn(userId,  photoUrl, latitude, longitude);
    }

    private boolean isWithinRadius(double lat, double lon) {
        // Implementasi Haversine formula
        double officeLat = 37.7749; // Contoh koordinat kantor
        double officeLon = -122.4194;
        // Hitung jarak, return true jika < radius
        return true; // Placeholder
    }

    private String savePhoto(MultipartFile photo) {
        // Simpan ke file system atau cloud, return URL
        return "path/to/photo.jpg";
    }

    @CrossOrigin("*")
    @PostMapping("/checkout")
    public ResponseEntity<?> checkOut(@RequestParam("photo") MultipartFile photo,
                                     @RequestParam("latitude") Double latitude,
                                     @RequestParam("longitude") Double longitude
    ) throws IOException {
        System.out.println("CHECKOUT  >>"+latitude+","+longitude);
        System.out.println("photo >>"+photo.getOriginalFilename());
        byte[] fileBytes = photo.getBytes();

//        String base64EncodedString = Base64.getEncoder().encodeToString(fileBytes);
        Long userId = jwtService.extractUserId(BearerTokenUtil.getBearerTokenHeader());
        if (!isWithinRadius(latitude, longitude)) {
            return ResponseEntity.badRequest().body("Lokasi di luar area absensi");
        }
        String photoUrl = savePhoto(photo);
        return attendanceService.checkOut(userId,  photoUrl, latitude, longitude);
    }


    @CrossOrigin("*")
    @GetMapping("/data-harian")
    public ResponseEntity<?> data_harian() {
        Long userId = jwtService.extractUserId(BearerTokenUtil.getBearerTokenHeader());
        return attendanceService.checkData(userId);
    }


}