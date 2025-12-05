package id.app.asnot.model.response;

import id.app.asnot.model.entity.Status;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class AttendanceResponse {
    private Long id;
    private Long userId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private LocalDate date;
    private Double latitude_in;
    private Double longitude_in;
    private Double latitude_out;
    private Double longitude_out;
    private String photoUrl_in;
    private String photoUrl_out;
    private Status status;
}
