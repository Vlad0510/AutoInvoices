package com.Invoices.AutoInvoices.Repository;

import com.Invoices.AutoInvoices.Entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByIdStudent(int idStudent);

    //Metoda numarat prezente
    long countByIdStudent(int idStudent);
}
