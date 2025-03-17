package com.Invoices.AutoInvoices.Controller;

import com.Invoices.AutoInvoices.Entity.Student;
import com.Invoices.AutoInvoices.Repository.StudentRepository;
import com.Invoices.AutoInvoices.Service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentService service;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/")
    public String home() {
        return "home";
    }

//    @GetMapping("student_register")
//    public String studentRegister(){
//        return "studentRegister";
//    }
    @GetMapping("student_register")
    public String studentRegister(Model model) {
        model.addAttribute("student", new Student());
        return "studentRegister";
    }

    @GetMapping("students")
    public ModelAndView getAllStudents(){
        List<Student> list = studentRepository.findAllByOrderByNumeAsc();
        return new ModelAndView("studentList", "student", list);
    }

//    @PostMapping("/save")
//    public String addStudent(@ModelAttribute @Valid Student s) {
//        service.save(s);
//        return "redirect:/students";
//    }

    @PostMapping("/save")
    public String addStudent(@ModelAttribute @Valid Student s, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "studentRegister";
        }
        service.save(s);
        return "redirect:/students";
    }

    @RequestMapping("/editStudent/{id}")
    public String editStudent(@PathVariable("id") int id, Model model){
        Student s = service.findById(id);
        model.addAttribute("student", s);
        return "studentEdit";
    }

    @RequestMapping("/deleteStudent/{id}")
    public String deleteStudent(@PathVariable("id") int id){
        service.deleteById(id);
        return "redirect:/students";
    }

    @GetMapping("/editStudent/students")
    public String handleEditStudents(){
        return "redirect:/students";
    }

    @GetMapping("/editStudent/student_register")
    public String handleNewStudents(){
        return "redirect:/student_register";
    }
}
