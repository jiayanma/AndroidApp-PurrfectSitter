package com.example.team21.ServerCall;

import java.util.List;

public class PeopleResponse {
    private int count;
    private String next;
    private String previous;
    List<People> results;

    public PeopleResponse(int count, String next, String previous, List<People> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public int getCount() {return count;}
    public String getNext() {return next;}
    public String getPrevious() {return previous;}
    public List<People> getResults() {return results;}

    public void setCount(int count) {
        this.count = count;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public void setResults(List<People> results) {
        this.results = results;
    }
}
