package global.data;
import java.io.Serializable;
import java.time.LocalTime;

public class MeetTime implements Serializable {

    public enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    Day day;
    LocalTime startTime;
    LocalTime endTime;

    public MeetTime() {}

    public MeetTime(Day day, LocalTime startTime, LocalTime endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Day getDay() { 
        return day; 
    }

    public LocalTime getStartTime() { 
        return startTime; 
    }

    public LocalTime getEndTime() { 
        return endTime; 
    }

    // returns true if this meet time overlaps with other
    public boolean overlaps(MeetTime other){
        
        if(other == null || this.day != other.day) return false;

        return this.startTime.isBefore(other.endTime) &&
            other.startTime.isBefore(this.endTime);

    }

}
