package global.data;

import global.LinkedList;
import java.io.Serializable;
import server.data.Admin;
import server.data.Student;

public class Section implements Serializable {
   
    int number;
    Course course;
    Term term;
    Campus campus;
    LinkedList<MeetTime> meetTimes;
    LinkedList<Student> students;
    Admin instructor;
    int capacity;

    public Section(int number, int capacity, Course course, Term term, Campus campus) {
        this.number = number;
        this.capacity = capacity;
        this.course = course;
        this.term = term;
        this.campus = campus;
        this.meetTimes = new LinkedList<>();
        this.students = new LinkedList<>();
    }

    public int getNumber() { 
        return number; 
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

    public LinkedList<MeetTime> getMeetTimes() { 
        return meetTimes; 
    }

    public void addMeetTime(MeetTime mt) { 
        this.meetTimes.Push(mt); 
    }

    public LinkedList<Student> getStudents() { 
        return students; 
    }

    public void addStudent(Student student) { 
        this.students.Push(student); 
    }

    public void removeStudent(Student student) { 
        this.students.Remove(student); 
    }

    public Admin getInstructor() { 
        return instructor; 
    }

    public int getCapacity() {
        return capacity;
    }

}
