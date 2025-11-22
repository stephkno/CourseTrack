package global.data;
import global.LinkedList;
import server.data.Student;

import java.io.Serializable;

public class Course implements Serializable {
    
    String name;
    int number;
    int units;
    Department department;
    Campus campus;

    // should have a reference to or own offered sections?
    LinkedList<Section> sections;

    public Course(String name, int number, int units, Department department){
        this.name = name;
        this.number = number;
        this.units = units;
        this.department = department;
        this.campus = department.getCampus();
    }

    public boolean verifyPrereqs(Student student) {
        return false;
    }

    public int getUnits() {
        return units;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public Department getDepartment() { 
        return department; 
    }

    public Campus getCampus() {
        return campus;
    }
    
}
