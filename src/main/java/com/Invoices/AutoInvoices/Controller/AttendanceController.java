package com.Invoices.AutoInvoices.Controller;

import com.Invoices.AutoInvoices.Entity.Attendance;
import com.Invoices.AutoInvoices.Entity.AttendanceForm;
import com.Invoices.AutoInvoices.Entity.Student;
import com.Invoices.AutoInvoices.Service.AttendanceService;
import com.Invoices.AutoInvoices.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/{id}")
    public String showAttendancePage(@PathVariable("id") int id, Model model) {
        Student student = studentService.findById(id);
        model.addAttribute("student", student);
        //Preluam prezentele din BD
        List<Attendance> attendances = attendanceService.findByStudentId(id);
        model.addAttribute("attendances", attendances);
        model.addAttribute("attendanceForm", new AttendanceForm());
        return "attendance";
    }

    @PostMapping("/add")
    public String addAttendance(@ModelAttribute AttendanceForm form) {
        Attendance attendance = new Attendance();
        attendance.setIdStudent(form.getStudentId());
        attendance.setData(form.getAttendanceDate());

        attendanceService.saveMyAttendance(attendance);
        return "redirect:/attendance/" + form.getStudentId();
    }

    @RequestMapping("/deleteMyAttendance/{id}")
    public String deleteMyAttendance(@PathVariable("id") int id){
        Attendance attendance = attendanceService.findById(id);
        attendanceService.deleteById(id);
        return "redirect:/attendance/" + attendance.getIdStudent();

    }

    @GetMapping("students")
    public ModelAndView getStudents(){
        List<Student> list = studentService.getAllStudents();
        return new ModelAndView("studentList", "student", list);
    }

    @GetMapping("student_register")
    public String studentRegister(){
        return "studentRegister";
    }

}
