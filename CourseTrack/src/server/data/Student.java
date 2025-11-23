package server.data;

import client.UserType;
import global.LinkedList;
import global.data.Section;
import java.io.Serializable;

public class Student extends User implements Serializable {
    
    String studentId;

    // list of currently enrolled sections
    LinkedList<Section> enrolledSections;
    
    // list of all sections previously taken by student
    LinkedList<Section> pastSections;

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
