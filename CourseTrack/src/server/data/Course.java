package server.data;
import global.LinkedList;

public class Course {
    
    String name;
    int number;
    int units;
    Department department;
    Term term;

    // should have a reference to or own offered sections?
    LinkedList<Section> sections;

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

    
}
