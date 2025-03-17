package com.Invoices.AutoInvoices.Service;

import com.Invoices.AutoInvoices.Entity.Attendance;
import com.Invoices.AutoInvoices.Repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository atdRep;

    @Autowired
    private OblioService oblioService;

    public void saveMyAttendance(Attendance attendance){
        atdRep.save(attendance);

        //Verificare nr prezente
        int studentId = attendance.getIdStudent();
        long attendanceCount = atdRep.countByIdStudent(studentId);

        if(attendanceCount % 6 == 0){
            oblioService.createAndSendProforma(studentId);
        }
    }

    public List<Attendance> findByStudentId(int studentId) {
        List<Attendance> attendances = atdRep.findByIdStudent(studentId);

        // Definim un formatter pentru a transforma String-urile în LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Sortăm lista descrescător după data convertită din String în LocalDate
        Collections.sort(attendances, new Comparator<Attendance>() {
            @Override
            public int compare(Attendance a1, Attendance a2) {
                // Verificăm dacă datele nu sunt nule sau goale
                if (a1.getData() == null || a1.getData().isEmpty()) {
                    return 1; // Păstrăm datele nule sau goale la sfârșitul listei
                }
                if (a2.getData() == null || a2.getData().isEmpty()) {
                    return -1; // Păstrăm datele nule sau goale la sfârșitul listei
                }

                // Convertim doar datele valide
                LocalDate date1 = LocalDate.parse(a1.getData(), formatter);
                LocalDate date2 = LocalDate.parse(a2.getData(), formatter);
                return date2.compareTo(date1); // sortare descrescătoare
            }
        });

        return attendances;
    }

    public Attendance findById(int id) {
        return atdRep.findById(id).orElse(null);  // Returnăm null dacă nu se găsește prezența
    }

    public void deleteById(int id){
        atdRep.deleteById(id);
    }
}
