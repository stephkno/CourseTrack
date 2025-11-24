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
    static LinkedList<Section> sections = new LinkedList<>();
    
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

    public boolean addSection(Section section){
        if(sections.Contains(section)) return false;
        sections.Push(section);
        return true;
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

    public boolean Equals(Term other) {
        return season == other.season && year == other.year;
    }

    public String toString(){
        String outstring = season + " " + year + "\n";
        for(Section section : sections){
            outstring += section.toString();
        }
        return outstring;
    }

}
