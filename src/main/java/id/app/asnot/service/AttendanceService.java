package id.app.asnot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.app.asnot.model.entity.Attendance;
import id.app.asnot.model.entity.Status;
import id.app.asnot.model.entity.User;
import id.app.asnot.model.response.AttendanceResponse;
import id.app.asnot.repository.AttendanceRepository;
import id.app.asnot.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private final UserRepository userRepository;

    // Konstanta untuk waktu kerja (contoh: check-in sebelum 09:00 dianggap PRESENT, setelahnya LATE)
    private static final LocalTime WORK_START_TIME = LocalTime.of(9, 0);

    public AttendanceService(AttendanceRepository attendanceRepository, UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> checkIn(Long userId, String photoUrl, Double latitude, Double longitude) throws ResponseStatusException {

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("user tidak ada");
        }
        LocalDateTime now = LocalDateTime.now();
        Optional<Attendance> existingAttendance = attendanceRepository.findByUserIdAndDate(user, now.toLocalDate());
        if (existingAttendance.isPresent()) {
            return ResponseEntity.badRequest().body("anda sudah absen hari ini");
        }

        Status status = now.toLocalTime().isBefore(WORK_START_TIME)
                ? Status.PRESENT
                : Status.LATE;

        Attendance attendance = new Attendance();
        attendance.setCheckIn(now);
        attendance.setUser(user);
        attendance.setPhotoUrl_in(photoUrl);
        attendance.setLatitude_in(latitude);
        attendance.setLongitude_in(longitude);
        attendance.setStatus(status);
        attendance.setDate(now.toLocalDate());
        attendanceRepository.save(attendance); 
        return ResponseEntity.ok("Trimakasih");
    }

    // Method untuk check-out
    public  ResponseEntity<?>  checkOut(Long userId, String photoUrl, Double latitude, Double longitude) {
        // Validasi: Pastikan user ada
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Cari attendance hari ini yang belum check-out
        LocalDate today = LocalDate.now();
        Optional<Attendance> attendanceOpt = attendanceRepository.findByUserIdAndDate(user, today);
        if (attendanceOpt.isEmpty() || attendanceOpt.get().getCheckOut() != null) {
            return ResponseEntity.badRequest().body("Anda Sudah CHECKIN CHECKOUT hari ini");
        }

        // Update check-out time
        Attendance attendance = attendanceOpt.get();
        attendance.setCheckOut(LocalDateTime.now());
        attendance.setLatitude_out(latitude);
        attendance.setLongitude_out(longitude);
        attendance.setPhotoUrl_out(photoUrl);

        // Simpan perubahan
        attendanceRepository.save(attendance);
        return ResponseEntity.ok("CheckOut Successfully");
    }


    public ResponseEntity<?> checkData(Long userId) {
        System.out.println("user> "+userId);
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("user tidak ada");
        }
        LocalDate today = LocalDate.now();
        return  ResponseEntity.ok(attendanceRepository.findByUserIdAndDate(user, today));
    }

    public ResponseEntity<?> checkDataAll(Long userId, String date) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("user tidak ada");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        System.out.println(localDate);
        System.out.println(month);
        System.out.println(year);
        List<Attendance> attendances = attendanceRepository.findByUserIdAndYearAndMonth(user, year,month);
        ModelMapper modelMapper = new ModelMapper();
        List<AttendanceResponse> attendanceResponse = attendances.stream()
                .map(attendance -> modelMapper.map(attendance, AttendanceResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(attendanceResponse);
    }
}