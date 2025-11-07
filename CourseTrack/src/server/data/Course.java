package server.data;
import global.LinkedList;

public class Course {
    
    String name;
    int number;
    int units;
    Department department;

    // should have a reference to or own offered sections?
    LinkedList<Section> sections;

    public boolean VerifyPrereqs(Student student){
        return false;
    }

    public int GetUnits() {
        return units;
    }

    public String GetName() {
        return name;
    }

    public int GetNumber() {
        return number;
    }

    public Department getDepartment() { 
        return department; 
    }

    
}
