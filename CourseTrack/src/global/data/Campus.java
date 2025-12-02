package global.data;
import global.HashMap;
import global.LinkedList;
import global.Log;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import server.data.Student;

public class Campus implements Serializable {
    
    String campusName;
    
    int id;
    static int nextId = 0;
    
    static HashMap<String, Campus> campuses = new HashMap<>();
    // transient
    HashMap<String, Department> departments = new HashMap<>();

    // transient
    LinkedList<Student> students = new LinkedList<>();

    boolean serializeFull = false;
    public void serializeFull(){
        serializeFull = true;
    }

    public static HashMap<String, Campus> get() {
        return campuses;
    }

    public static Campus get(String campusName) {
        
        return campuses.Get(campusName);

    }

    public static Campus add(String campusName){

        if(campuses.Contains(campusName)) return campuses.Get(campusName);

        Campus newCampus = new Campus(campusName);
        campuses.Put(campusName, newCampus);
        return newCampus;

    }

    public static void remove(String campusName){
        
        Campus campus = get(campusName);
        
        for(Department department : campus.getDepartments()){
            Log.Msg("Removing department " + department.getName());

            for(Course course : department.getCourses()){
                Log.Msg("Removing course " + course.getName());
                
                for(Term term : course.getTerms()){
                    for(Section section : course.getSections(term)){

                        Log.Msg("Removing section " + section.getName());
                        
                        if(!term.removeSection(section)){
                            Log.Err("Section not found");
                        }else{
                            Log.Msg("Section removed");
                        }
                        
                    }
                }
            }
        }

        if(!campuses.Contains(campusName)) return;
        campuses.Remove(campusName);

    }

    public static boolean exists(String campusName) {
        return campuses.Contains(campusName);
    }

    public static void save(ObjectOutputStream objectStream) {
        try {
            objectStream.writeObject(campuses);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void load(ObjectInputStream objectStream) {
        try {
            campuses = (HashMap<String, Campus>)objectStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setNextId(int id){
        nextId = id;
    }

    public static int getNextId(){
        return nextId;
    }

    private Campus(String campusName) {
        this.campusName = campusName;
        this.id = Campus.nextId++;
    }

    public String getName() { 
        return campusName; 
    }

    public boolean hasDepartment(String departmentName){
        return departments.Contains(departmentName);
    }

    public Department addDepartment(String departmentName){
        if(departments.Contains(departmentName)) return null;

        Department newDepartment = new Department(departmentName, this);
        departments.Put(departmentName, newDepartment);

        return newDepartment;
    }

    public boolean removeDepartment(String departmentName){
        if(!departments.Contains(departmentName)) return false;
        departments.Remove(departmentName);
        return true;
    }

    public Department getDepartment(String departmentName) {
        return departments.Get(departmentName);
    }

    public LinkedList<Department> getDepartments(){
        LinkedList<Department> outlist = new LinkedList<>();

        for(Department d : departments){
            outlist.Push(d);
        }

        return outlist;
    }

    @Override
    public boolean equals(Object other){
        return id == ((Campus)other).id;
    }

    public String toString(){

        String outstring = new String(campusName);

        if(departments != null){

            outstring += campusName + "\nDepartments:\n";
            
            for(Department department : departments){
                outstring += department.toString();
            }

        }

        return outstring + "\n";
    }
}
