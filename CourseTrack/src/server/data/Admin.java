package server.data;
import client.UserType;
import java.io.Serializable;
import global.HashMap;
import global.data.Campus;
import global.data.Course;
import global.data.Department;
import global.data.Section;
import global.LinkedList;
public class Admin extends User implements Serializable {

    int id;
    static int nextId = 0;

    LinkedList<Campus> campuses = new LinkedList<>();
    LinkedList<Course> courses = new LinkedList<>();
    LinkedList<Department> departments = new LinkedList<>();
    LinkedList<Section> sections = new LinkedList<>();

    public void addCampus(Campus campus){
        this.campuses.Push(campus);
    }

    public boolean existsCampus(Campus campus){
        return this.campuses.Contains(campus);
    }

    public void removeCampus(Campus campus){
        this.campuses.Remove(campus);
    }

    public LinkedList<Campus> getCampuses(){
        return campuses;
    }

    public void addCourse(Course course){
        this.courses.Push(course);
    }

    public boolean existsCourse(Course course){
        return this.courses.Contains(course);
    }

    public void removeCourse(Course course){
        this.courses.Remove(course);
    }
    
    public LinkedList<Course> getCourses(){
        return this.courses;
    }

    public void addDepartment(Department department){
        this.departments.Push(department);
    }

    public boolean existsDepartment(Department department){
        return this.departments.Contains(department);
    }

    public void removeDepartment(Department department){
        this.departments.Remove(department);
    }

    public LinkedList<Department> getDepartments(){
        return this.departments;
    }

    public void addSection(Section section){
        this.sections.Push(section);
    }

    public boolean existsSection(Section section){
        return this.sections.Contains(section);
    }

    public void removeSection(Section section){
        this.sections.Remove(section);
    }

    public LinkedList<Section> getSections(){
        return this.sections;
    }

    public Admin(String name, String password) {
        super(name, password, UserType.ADMIN);
        this.id = nextId++;
    }

}
