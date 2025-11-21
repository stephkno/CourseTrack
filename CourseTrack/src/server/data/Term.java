package server.data;

public class Term {
    
    public enum Season {
        SPRING, SUMMER, FALL, WINTER
    }

    Season season;
    int year;

    public Term(Season season, int year) {
        this.season = season;
        this.year = year;
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

}
