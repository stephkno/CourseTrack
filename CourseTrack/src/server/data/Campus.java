package server.data;
import global.HashMap;
import global.LinkedList;
import java.io.Serializable;

public class Campus implements Serializable {
    
    String campusName;

    HashMap<Department> departments = new HashMap<>();
    LinkedList<Student> students = new LinkedList<>();

    public Campus(String campusName) {
        this.campusName = campusName;
    }

    public String getCampusName() { 
        return campusName; 
    }

    public Department getDepartment(String departmentName) {
        return departments.Get(departmentName);
    }

    public boolean AddCourse(Course course) {

        // get department
        Department d = getDepartment(course.getDepartment().getName());
        
        if(d == null) return false;
        
        // check if course exists
        if(d.containsCourse(course)) return false;

        // add new course
        d.addCourse(course);
        return true;

    }
        
    public String toString(){
        String outstring = "";

        outstring += campusName + "\n";
        
        for(Department department : departments){
            outstring += department.toString();
        }

        return outstring;
    }
}
