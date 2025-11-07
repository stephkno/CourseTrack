package server.data;

public class Campus {
    
    String campusName;
    String location;

    public Campus(String campusName, String location) {
        this.campusName = campusName;
        this.location = location;
    }

    public String getCampusName() { 
        return campusName; 
    }

    public String getLocation() { 
        return location; 
    }

}
