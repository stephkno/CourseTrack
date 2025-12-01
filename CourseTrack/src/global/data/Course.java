package global.data;
import global.LinkedList;
import global.Log;
import java.io.Serializable;
import server.data.Student;
import global.HashMap;

public class Course implements Serializable {
    
    String name;
    int id;
    static int nextId = 0;
    int number;
    int units;
    Department department;
    Campus campus;

    HashMap<Term, LinkedList<Section>> sections = new HashMap<>();

    // list of required courses
    LinkedList<Course> requirements = new LinkedList<>();

    public Course(String name, int number, int units, Department department, LinkedList<Course> requirements) {

        // verify that Course.requirements is not recursive
        // It shouldn't be possible anyway
        for(Course course : requirements){
            if(course == this) Log.Err("Error: Course added as requirement of itself");
        }

        this.name = name;
        this.number = number;
        this.units = units;
        this.department = department;
        this.campus = department.getCampus();

        if(this.campus == null){
            Log.Err("Course added with null campus.");
        }

        this.id = Course.nextId++;
        
    }

    public static void setNextId(int id){
        nextId = id;
    }
    public static int getNextId(){
        return nextId;
    }

    public LinkedList<Course> getRequirements() {
        return requirements;
    }

    // add section to this course in this term and return id
    public int addSection(Term term, Section section) {
        
        LinkedList<Section> ll;
        
        Log.Msg(this);

        if(sections.Contains(term)){
            ll = sections.Get(term);
            ll.Push(section);
        }else{
            ll = new LinkedList<Section>();
            ll.Push(section);
            sections.Put(term, ll);
        }

        return ll.Length();

    }

    public LinkedList<Section> getSections(Term term) {
        return sections.Get(term);
    }

    public boolean verifyPrereqs(Student student) {

        for(Course course : requirements){
            if(student.hasTaken(course)) {
                return false;
            } 
        }
        
        return true;
    }

    public int getId() {
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

    public String toString() {
        String outstring = "";
        outstring += name + " " + number;
        return outstring;
    }

    public boolean equals(Campus other){
        return id == other.id;
    }

}

