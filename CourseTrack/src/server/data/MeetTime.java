package server.data;
import java.io.Serializable;

public class MeetTime implements Serializable {

    public enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    Day day;
    String startTime;
    String endTime;
    String room;

    public MeetTime() {}

    public MeetTime(Day day, String startTime, String endTime, String room) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
    }

    public Day getDay() { 
        return day; 
    }

    public String getStartTime() { 
        return startTime; 
    }

    public String getEndTime() { 
        return endTime; 
    }

    public String getRoom() { 
        return room; 
    }

}
