package server.data;
import global.LinkedList;

public class Campus {
    
    String campusName;

    // courses offered at this campus
    LinkedList<Course> courses;
    LinkedList<Student> students;

    public Campus(String campusName) {
        this.campusName = campusName;
    }

    public String getCampusName() { 
        return campusName; 
    }

}
