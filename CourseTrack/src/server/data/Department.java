package server.data;

public class Department {
    
    String name;
    Campus campus;

    public Department(String name, Campus campus){
        this.name = name;
        this.campus = campus;
    }

    public Campus getCampus() { 
        return campus; 
    }

}
