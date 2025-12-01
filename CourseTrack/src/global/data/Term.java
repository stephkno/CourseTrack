package global.data;
import global.LinkedList;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import global.Log;

public class Term implements Serializable {
    
    public enum Season {
        SPRING, SUMMER, FALL, WINTER
    }

    Season season;
    int year;

    static LinkedList<Term> terms = new LinkedList<>();
    // transient
    LinkedList<Section> sections = new LinkedList<>();
    
    public static LinkedList<Term> get() {
        return terms;
    }

    public static Term get(Term term) {

        // search for term
        for(Term t : terms){
            if(t.equals(term)){
                return t;
            }
        }

        // add new term
        Term newTerm = new Term(term);
        terms.Push(newTerm);
        return newTerm;

    }

    public static boolean exists(Term.Season season, int year) {
        for(Term term : terms){
            if(term.equals(new Term(season, year))){
                return true;
            }
        }
        return false;
    }

    public static void save(ObjectOutputStream objectStream) {
        try {
            objectStream.writeObject(terms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void load(ObjectInputStream objectStream) {
        try {
            terms = (LinkedList<Term>)objectStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Term(Season season, int year) {
        this.season = season;
        this.year = year;
    }

    private Term(Term other) {
        this.season = other.season;
        this.year = other.year;
    }

    // add section must verify that no two sections with same instructor meet at same time
    public boolean addSection(Section section){
        for(Section s : sections){
            if(s.conflicts(section)){
                return false;
            }
        }
        sections.Push(section);
        return true;
    }

    public boolean removeSection(Section section){

        Log.Msg(sections.Length());

        return sections.Remove(section);
    
    }

    public LinkedList<Section> getSections(){
        return sections;
    }

    public Section getSection(int id){
        // get section from term 
        for(Section s : getSections()){
            if(s.getId() == id){
                return s;
            }
        }
        return null;
    }

    public Season getSeason() { 
        return season; 
    }

    public int getYear() { 
        return year; 
    }

    public String getDisplayName() {
        return season + " " + year;
    }

    public boolean equals(Term other) {
        return season == other.season && year == other.year;
    }

    public Term shallowCopy(){
        return new Term(this.season, this.year);
    }

    public String toString(){
        String outstring = season + " " + year + "\n";
        if(sections == null) return outstring;

        for(Section section : sections){
            outstring += section.toString();
        }
        return outstring;
    }
    
    //get previous term
    public Term getPreviousTerm() {
        Season prevSeason = null;
        int prevYear = year;

        switch (season) {
            case SPRING:
                prevSeason = Season.WINTER;
                break;
            case SUMMER:
                prevSeason = Season.SPRING;
                break;
            case FALL:
                prevSeason = Season.SUMMER;
                break;
            case WINTER:
                prevSeason = Season.FALL;
                prevYear = year - 1;
                break;
            default:
                return null;
        }

        for (Term term : terms) {
            if (term.season == prevSeason && term.year == prevYear) {
                return term;
            }
        }

        return null;
    }

    //find matching section in other previous term
    private Section findMatchingSectionInPrevTerm(Term otherTerm, Section section) {
        if (otherTerm == null || section == null) {
            return null;
        }

        int courseId = section.getCourse().getId();
        int number = section.getNumber();

        for (Section s : otherTerm.getSections()) {
            if (s.getCourse().getId() == courseId &&
                s.getNumber() == number) {
                return s;
            }
        }

        return null;
    }

    //math for % change in enrollment
    public double getEnrollmentPercentageChange(Section section) {
        Term previousTerm = getPreviousTerm();
        Section previousSection = findMatchingSectionInPrevTerm(previousTerm, section);

        if (previousSection == null) {
            return 0.0;
        }

        int previousEnrolled = previousSection.numStudents();
        int currentEnrolled = section.numStudents();

        if (previousEnrolled == 0) {
            return 0.0; // avoid divide-by-zero
        }

        return ((double)(currentEnrolled - previousEnrolled) / (double) previousEnrolled) * 100.0;
    }

    //math for % change in waitlist
    public double getWaitlistPercentageChange(Section section) {
        Term previousTerm = getPreviousTerm();
        Section previousSection = findMatchingSectionInPrevTerm(previousTerm, section);

        if (previousSection == null) {
            return 0.0;
        }

        int previousWaitlist = previousSection.waitlistLength();
        int currentWaitlist = section.waitlistLength();

        if (previousWaitlist == 0) {
            return 0.0; // avoid divide-by-zero
        }

        return ((double)(currentWaitlist - previousWaitlist) / (double) previousWaitlist) * 100.0;
    }
}
