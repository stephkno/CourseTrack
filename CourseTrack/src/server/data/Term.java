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

    public void setSeason(Season season) { 
        this.season = season; 
    }

    public int getYear() { 
        return year; 
    }

    public void setYear(int year) { 
        this.year = year; 
    }

    public String getDisplayName() {
        return season + " " + year;
    }
}
