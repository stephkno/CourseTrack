package global.data;

import global.LinkedList;
import java.io.Serializable;
import server.data.Student;

public class Section implements Serializable {
   
    int id;
    static int nextId = 0;

    int number;
    Course course;
    Term term;
    Department department;
    MeetTime[] meetTimes;
    // transient
    LinkedList<Student> students;
    // transient
    LinkedList<Student> waitlist;

    String instructor;
    
    int capacity;
    int num_enrolled;
    int num_waitlisted;
    double percentageFull;

    public Section(int capacity, Course course, Term term, Department department, String instructor, MeetTime[] meetTimes) {
        
        this.capacity = capacity;
        this.course = course;
        this.term = term;
        this.department = department;

        this.students = new LinkedList<Student>();
        this.waitlist = new LinkedList<Student>();

        this.instructor = instructor;
        this.meetTimes = meetTimes;

        // add section to this term
        this.number = course.addSection(term, this);

        this.num_enrolled = 0;
        this.num_waitlisted = 0;
        
        this.id = Section.nextId++;

    }

    public static void setNextId(int id){
        nextId = id;
    }
    public static int getNextId(){
        return nextId;
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

    public String getName(){
        return course.getName();
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
        num_enrolled++;
        
    }

    public boolean hasOpenSeats(){
        return this.students.Length() < this.capacity;
    }

    public boolean isEnrolled(Student student){
        return students.Contains(student);
    }

    public int numStudents() { 
        return num_enrolled;
    }

    public void removeStudent(Student student) { 
        this.students.Remove(student); 
        num_enrolled--;
    }

    public int addWaitlist(Student student){
        this.waitlist.Push(student);
        num_waitlisted++;
        return this.waitlist.Length();
    }

    public boolean waitlisted(Student student){
        return this.waitlist.Contains(student);
    }
          
    public void removeWaitlist(Student student){
        this.waitlist.Remove(student);
    }

    public LinkedList<Student> getWaitlist(){
        return waitlist;
    }
    
    public int getWaitlistPosition(Student student){

        if(!waitlist.Contains(student)){
            return 0;
        }

        int i = 1;
        for(Student s : waitlist){
            if(s.equals(student)){
                return i;
            }
            i++;
        }
        
        return 0;

    }

    public int waitlistLength(){
        return waitlist.Length();
    }

    public Student popWaitlist(){
        Student student = waitlist.Get(0);
        waitlist.Remove(0);
        num_waitlisted--;
        return student;
    }
    
    public String getInstructor() { 
        return instructor; 
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean full(){
        return students.Length() >= capacity;
    }

    public boolean conflicts(MeetTime[] meetTimes){

        for(MeetTime meetTimeA : this.meetTimes)
            for(MeetTime meetTimeB : meetTimes)
                if(meetTimeA.overlaps(meetTimeB))
                    return true;
        
        return false;
    }
    
    // check that these two sections do not have overlapping meet times with same instructor
    public boolean conflicts(Section other){
        if(other == this) return true;
        if(instructor.equals(other.instructor) && conflicts(other.getMeetTimes())) return true;
        return false;
    }

    public boolean equals(Campus other){
        return id == other.id;
    }
    
    public double getPercentFull() {
        if (capacity == 0) return 0;
        return percentageFull = ((double) num_enrolled / (double) capacity) * 100.0;
    }

    public String toString(){
        String outstring = "";

        outstring += "Course: " + course.getName() + 
        " ID: " + id +
        "\nSection no: " + number + 
        "\nInstructor: " + instructor + 
        "\nCampus: " + department.getCampus().getName() + 
        "\nTotal Capacity: " + capacity + 
        "\nTotal Enrolled: " + num_enrolled + 
        "\nTotal Waitlisted: " + num_waitlisted + 
        "\n" + percentageFull + "% full\n";

        return outstring;
    }
}
