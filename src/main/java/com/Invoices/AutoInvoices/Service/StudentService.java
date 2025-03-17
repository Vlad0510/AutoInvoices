package com.Invoices.AutoInvoices.Service;

import com.Invoices.AutoInvoices.Entity.Student;
import com.Invoices.AutoInvoices.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public void save(Student student){
        studentRepository.save(student);
    }

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public Student findById(int id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public void deleteById(int id){
        studentRepository.deleteById(id);
    }


//    public Student getStudentById(int i){
//
//        return s.getId();
//    }
}
