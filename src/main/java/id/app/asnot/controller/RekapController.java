package id.app.asnot.controller;

import id.app.asnot.service.RekapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rekap")
public class RekapController {

    @Autowired
    private RekapService rekapService;

    @CrossOrigin("*")
    @GetMapping("/all")
    public ResponseEntity<?> getAllRekap() {
        return rekapService.getAllRekap();
    }

    @CrossOrigin("*")
    @GetMapping("/daily")
    public ResponseEntity<?> getDailyRekap() {
        return rekapService.getDailyRekap();
    }
}
