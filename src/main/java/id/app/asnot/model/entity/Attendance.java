package id.app.asnot.model.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private LocalDate date;
    private Double latitude_in;
    private Double longitude_in;
    private Double latitude_out;
    private Double longitude_out;
    private byte[] photo_in;
    private byte[] photo_out;
    private Status status;
}