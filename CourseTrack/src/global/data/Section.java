package global.data;

import global.LinkedList;
import global.Log;
import java.io.Serializable;
import server.data.Admin;
import server.data.Student;

public class Section implements Serializable {
   
    int id;
    static int nextId = 0;

    int number;
    Course course;
    Term term;
    Department department;
    MeetTime[] meetTimes;
    LinkedList<Student> students;
    LinkedList<Student> waitlist;
    String instructor;
    int capacity;

    public Section(int capacity, Course course, Term term, Department department, String instructor, MeetTime[] meetTimes) {
        
        this.capacity = capacity;
        this.course = course;
        this.term = term;
        this.department = department;
        this.students = new LinkedList<>();
        this.waitlist = new LinkedList<>();
        this.id = Section.nextId++;
        this.instructor = instructor;
      
        this.meetTimes = meetTimes;
        // add section to this term
        this.number = course.addSection(term, this);
    
    }

    public int getId(){
        return id;
    }

    public boolean Search(String q){
        
        q = q.toLowerCase();
        if (course.getName().toLowerCase().contains(q)) return true;
        if (instructor.toLowerCase().contains(q)) return true;

        return false;
    
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

    public Department getDepartment() {
        return department; 
    }

    public MeetTime[] getMeetTimes() { 
        return meetTimes; 
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

    public int addWaitlist(Student student){
        this.waitlist.Push(student);
        return this.waitlist.Length()-1;
    }

    public String getInstructor() { 
        return instructor; 
    }

    public int getCapacity() {
        return capacity;
    }

    public int waitlistLength(){
        return waitlist.Length();
    }

    public Student popWaitlist(){
        Student student = waitlist.Get(0);
        waitlist.Remove(0);
        return student;
    }
    
    public boolean full(){
        return students.Length() >= capacity;
    }

    public boolean conflicts(MeetTime[] meetTimes){

        for(MeetTime meetTimeA : getMeetTimes()){
            for(MeetTime meetTimeB : meetTimes){
                if(meetTimeA.overlaps(meetTimeB)){
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public String toString(){
        String outstring = "";
        long percentage = ( (long)students.Length() / (long)capacity ) * 100;
        outstring += "Course: " + course.getName() + " \nSection no: " + number + "\nInstructor: " + instructor + "\nCampus: " + department.getCampus().getName() + "\nTotal Capacity: " + capacity + "\nTotal Enrolled:" + students.Length() + "\n" + percentage + "% full\n";
        return outstring;
    }
}
