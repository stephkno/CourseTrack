package server.data;

import java.util.ArrayList;
import java.util.List;

public class Section {
   
    String sectionId;
    Course course;
    Term term;
    Campus campus;
    List<MeetTime> meetTimes;
    List<Student> students;
    Admin instructor;
    int capacity;

    public Section(String sectionId, int capacity, Course course, Term term, Campus campus) {
        this.sectionId = sectionId;
        this.capacity = capacity;
        this.course = course;
        this.term = term;
        this.campus = campus;
        this.meetTimes = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public String getSectionId() { 
        return sectionId; 
    }

    public Course getCourse() { 
        return course; 
    }

    public Term getTerm() { 
        return term; 
    }

    public Campus getCampus() { 
        return campus; 
    }

    public List<MeetTime> getMeetTimes() { 
        return meetTimes; 
    }

    public void addMeetTime(MeetTime mt) { 
        this.meetTimes.add(mt); 
    }

    public List<Student> getStudents() { 
        return students; 
    }

    public void addStudent(Student student) { 
        this.students.add(student); 
    }

    public void removeStudent(Student student) { 
        this.students.remove(student); 
    }

    public Admin getInstructor() { 
        return instructor; 
    }

    public int getCapacity(){
        return capacity;
    }

}
