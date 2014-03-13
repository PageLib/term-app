package fr.pagelib.termapp.wsc;

import javax.print.attribute.standard.PageRanges;

public class PrintingJob {

    String path;
    String name;
    Integer copies;
    PageRanges pages;
    Boolean color;

    public String toString() {
        return String.format("<PrintJob for %s>", getPath());
    }

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

    public Integer getPageCount() {
        Integer pageCount = 0;
        for(int[] range:getPages().getMembers()){
            pageCount += range[1] + 1 -range[0];
        }
        return pageCount;
    }
    public Integer getPageColor() {
        return color ? getPageCount()*copies : 0;
    }

    public Integer getPageGreyLevel() {
        return !color ? getPageCount()*copies : 0;
    }
}
