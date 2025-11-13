package server.data;
import global.LinkedList;

public class Campus {
    
    String campusName;
    String location;

    // courses offered at this campus
    LinkedList<Course> courses;
    LinkedList<Student> students;

    public Campus(String campusName, String location) {
        this.campusName = campusName;
        this.location = location;
    }

    public String getCampusName() { 
        return campusName; 
    }

    public String getLocation() { 
        return location; 
    }

}
