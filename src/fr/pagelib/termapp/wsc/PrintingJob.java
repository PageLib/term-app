package fr.pagelib.termapp.wsc;

import javax.print.attribute.standard.PageRanges;

public class PrintingJob {

    String path;
    String name;
    Integer copies;
    PageRanges pages;
    Boolean color;

    /**
     * Getters and setters
     */

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    public PageRanges getPages() {
        return pages;
    }

    public void setPages(PageRanges pages) {
        this.pages = pages;
    }

    public Boolean getColor() {
        return color;
    }

    public void setColor(Boolean color) {
        this.color = color;
    }
}
