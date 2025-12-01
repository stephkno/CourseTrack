package global.data;
import java.io.Serializable;

public class StudentScheduleItem implements Serializable {
    
    Section section;
    int waitlistPosition;

    public StudentScheduleItem(Section section, int waitlistPosition) {
        this.section = section;
        this.waitlistPosition = waitlistPosition;
    }

    public String toString(){
      return section.toString() + " Waitlisted: " + waitlistPosition;
    }

}
