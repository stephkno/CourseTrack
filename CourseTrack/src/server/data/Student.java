package server.data;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    
    String studentId;
    List<Section> enrolledSections;

    public Student(String name, String password, String studentId) {
        super(name, password);
        this.studentId = studentId;
        this.enrolledSections = new ArrayList<>();
    }

    public String getStudentId() { 
        return studentId;
    }

    public List<Section> getEnrolledSections() { 
        return enrolledSections; 
    }
    
}
