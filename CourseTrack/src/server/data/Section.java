package server.data;

import global.LinkedList;

public class Section {
   
    String sectionId;
    Course course;
    Term term;
    Campus campus;
    LinkedList<MeetTime> meetTimes;
    LinkedList<Student> students;
    Admin instructor;
    int capacity;

    public Section(String sectionId, int capacity, Course course, Term term, Campus campus) {
        this.sectionId = sectionId;
        this.capacity = capacity;
        this.course = course;
        this.term = term;
        this.campus = campus;
        this.meetTimes = new LinkedList<>();
        this.students = new LinkedList<>();
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
