package global.data;
import global.LinkedList;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Term implements Serializable {
    
    public enum Season {
        SPRING, SUMMER, FALL, WINTER
    }

    Season season;
    int year;

    static LinkedList<Term> terms = new LinkedList<>();
    transient LinkedList<Section> sections = new LinkedList<>();
    
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

    public String toString(){
        String outstring = season + " " + year + "\n";
        if(sections == null) return outstring;

        for(Section section : sections){
            outstring += section.toString();
        }
        return outstring;
    }

}
