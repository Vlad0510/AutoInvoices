package com.Invoices.AutoInvoices.Repository;

import com.Invoices.AutoInvoices.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findAllByOrderByNumeAsc();
}
