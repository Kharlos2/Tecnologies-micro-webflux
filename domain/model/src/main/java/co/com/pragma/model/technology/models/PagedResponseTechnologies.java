package co.com.pragma.model.technology.models;

import java.util.List;

public class PagedResponseTechnologies {

    private long count;
    private int page;
    private int size;
    private List<Technology> technologies;

    public PagedResponseTechnologies(long count, int page, int size, List<Technology> technologies) {
        this.count = count;
        this.page = page;
        this.size = size;
        this.technologies = technologies;
    }

    // Getters y Setters
    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<Technology> technologies) {
        this.technologies = technologies;
    }

}
