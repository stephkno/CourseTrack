package global.data;

import global.LinkedList;
import java.io.Serializable;

public class Department implements Serializable {
    
    String name;
    Campus campus;
    LinkedList<Course> courses;

    public boolean addCourse(Course course){
        
        if(!containsCourse(course)) {
            courses.Push(course);
            return true;
        }

        return false;
    }

    public boolean containsCourse(Course course){
        return courses.Contains(course);
    }

    public Department(String name, Campus campus) {
        this.name = name;
        this.campus = campus;
    }

    public Campus getCampus() { 
        return campus; 
    }
    
    public String getName(){
        return name;
    }

}
