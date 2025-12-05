package id.app.asnot.repository;

import id.app.asnot.model.entity.Attendance;
import id.app.asnot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByUserId(Long userId);

    @Query("SELECT a FROM Attendance a WHERE a.user = :userId AND DATE(a.checkIn) = :date")
    Optional<Attendance> findByUserIdAndDate(@Param("userId") User userId, @Param("date") LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.user = :userId AND YEAR(a.date) = :year AND MONTH(a.date) = :month")
    List<Attendance> findByUserIdAndYearAndMonth(User userId, int year, int month);
}
