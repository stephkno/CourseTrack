package server.data;

import client.UserType;
import global.LinkedList;
import global.data.Course;
import global.data.Section;
import java.io.Serializable;

public class Student extends User implements Serializable {
    
    int id;
    static int nextId = 0;

    // list of currently enrolled sections
    LinkedList<Section> enrolledSections;
    
    // list of all sections previously taken by student
    LinkedList<Course> pastSections;

    public Student(String name, String password) {
        super(name, password, UserType.STUDENT);
        this.id = nextId++;
        this.enrolledSections = new LinkedList<>();
    }

    public void addSection(Section section){
        enrolledSections.Push(section);
    }

    public void removeSection(Section section){
        enrolledSections.Remove(section);
    }

    public int getStudentId() { 
        return id;
    }

    public LinkedList<Section> getEnrolledSections() { 
        return enrolledSections; 
    }
    
    public boolean hasTaken(Course course){
        return pastSections.Contains(course);
    }
    
}
