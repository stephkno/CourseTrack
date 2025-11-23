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
    
    static HashMap<Campus> campuses = new HashMap<>();
    
    public static HashMap<Campus> get() {
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
            campuses = (HashMap<Campus>)objectStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    HashMap<Department> departments = new HashMap<>();
    LinkedList<Student> students = new LinkedList<>();

    private Campus(String campusName) {
        this.campusName = campusName;
    }

    public String getCampusName() { 
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

    public Department getDepartment(String departmentName) {
        Log.Msg(departmentName);
        Log.Msg(departments.Size());
        return departments.Get(departmentName);
    }

    public String toString(){
        String outstring = "";

        outstring += campusName + "\nDepartments:\n";
        
        for(Department department : departments){
            outstring += department.toString();
        }

        return outstring + "\n";
    }
}
