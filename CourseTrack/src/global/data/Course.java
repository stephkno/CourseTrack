package global.data;
import global.LinkedList;
import global.Log;
import java.io.Serializable;
import server.data.Student;

public class Course implements Serializable {
    
    String name;
    int id;
    static int nextId = 0;
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

        if(this.campus == null){
            Log.Err("Course added with null campus.");
        }

        this.id = nextId++;
    }

    public boolean verifyPrereqs(Student student) {
        return false;
    }

    public int getId(){
        return id;
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

    public String toString(){
        String outstring = "";
        outstring += name + " " + number;
        return outstring;
    }
    
}
