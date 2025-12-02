package global.data;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Random;

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

    // generate random meet times
    public static MeetTime[] random() {
        Random rand = new Random();
        Day[] days = Day.values();

        Day day1 = days[rand.nextInt(days.length)];
        Day day2;
        do {
            day2 = days[rand.nextInt(days.length)];
        } while (day2 == day1);

        LocalTime start = LocalTime.of(rand.nextInt(8, 17), 0);
        LocalTime end = start.plusMinutes(75);

        return new MeetTime[] {
            new MeetTime(day1, start, end),
            new MeetTime(day2, start, end)
        };
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
