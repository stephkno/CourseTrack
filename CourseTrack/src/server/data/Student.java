package server.data;

import client.UserType;
import global.LinkedList;

public class Student extends User {
    
    String studentId;
    LinkedList<Section> enrolledSections;

    public Student(String name, String password, String studentId) {
        super(name, password, UserType.STUDENT);
        this.studentId = studentId;
        this.enrolledSections = new LinkedList<>();
    }

    public String getStudentId() { 
        return studentId;
    }

    public LinkedList<Section> getEnrolledSections() { 
        return enrolledSections; 
    }
    
}
