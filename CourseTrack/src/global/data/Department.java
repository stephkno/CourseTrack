package global.data;

import global.LinkedList;
import java.io.Serializable;

public class Department implements Serializable {
    
    int id;
    static int nextId = 0;
    String name;
    Campus campus;
    LinkedList<Course> courses = new LinkedList<>();

    public boolean addCourse(Course course){
        
        if(containsCourse(course)) {
            return false;
        }
        
        courses.Push(course);

        return true;

    }

    public boolean containsCourse(Course course){
        return courses.Contains(course);
    }

    // return course by id
    public Course getCourse(int i){
        for(Course course : courses){
            if(course.getId() == i){
                return course;
            }
        }
        return null;
    }

    public Department(String name, Campus campus) {
        this.name = name;
        this.campus = campus;
        this.id = Department.nextId++;
    }

    public int length() {
        return courses.Length();
    }

    public Campus getCampus() { 
        return campus; 
    }
    
    public String getName(){
        return name;
    }

    public boolean equals(Campus other){
        return id == other.id;
    }

    public String toString(){
        String outstring = "Department: " + name;

        outstring += "\nCourses:\n";

        for(Course course : courses){
            outstring += course.toString() + "\n";
        }

        return outstring;
    }

}
