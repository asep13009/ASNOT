package id.app.asnot.controller;

import id.app.asnot.config.BearerTokenUtil;
import id.app.asnot.service.AttendanceService;
import id.app.asnot.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Value("${office.lat}")
    private double officeLat;
    @Value("${office.lon}")
    private double officeLon;
    @Value("${office.radius}")
    private double radius;
    @Value("${EARTH_RADIUS}")
    private double EARTH_RADIUS ;

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

        // // 2. Encode the byte array to Base64
        // String IMAGE_BASE64 = Base64.getEncoder().encodeToString(fileBytes);
        Long userId = jwtService.extractUserId(BearerTokenUtil.getBearerTokenHeader());
        if (!isWithinRadius(latitude, longitude)) {
            return ResponseEntity.badRequest().body("Lokasi di luar area absensi");
        } 
        return attendanceService.checkIn(userId,  fileBytes, latitude, longitude);
    }

    private boolean isWithinRadius(double lat, double lon) {
     
        double dLat = Math.toRadians(lat - officeLat);
        double dLon = Math.toRadians(lon - officeLon);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(officeLat)) * Math.cos(Math.toRadians(lat)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        return distance <= radius; 
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

    //    String IMAGE_BASE64 = Base64.getEncoder().encodeToString(fileBytes);
        Long userId = jwtService.extractUserId(BearerTokenUtil.getBearerTokenHeader());
        if (!isWithinRadius(latitude, longitude)) {
            System.out.println("on this" );
            return ResponseEntity.badRequest().body("Lokasi di luar area absensi");
        }
        System.out.println("on this ok " ); 
        return attendanceService.checkOut(userId,  fileBytes, latitude, longitude);
    }


    @CrossOrigin("*")
    @GetMapping("/data-harian")
    public ResponseEntity<?> data_harian() {
        Long userId = jwtService.extractUserId(BearerTokenUtil.getBearerTokenHeader());
        return attendanceService.checkData(userId);
    }


}