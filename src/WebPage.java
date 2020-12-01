/*
 * @author Jeremy Samuel
 * E-mail: jeremy.samuel@stonybrook.edu
 * Stony Brook ID: 113142817
 * CSE 214
 * Recitation Section 3
 * Recitation TA: Dylan Andres
 * HW #7
 */

import java.util.ArrayList;

/**
 * WebPage class
 */
public class WebPage{
    private String url;
    private int index;
    private int rank;
    private ArrayList<String> keywords;

    /**
     * Constructor with parameters
     * @param url
     * The url of the WebPage
     * @param keywords
     * The keywords relating to the WebPage
     */
    public WebPage(String url, ArrayList<String> keywords, int current_pages){
        this.index = current_pages;
        this.url = url;
        this.keywords = keywords;
    }

    /**
     * Gets the page rank of the WebPage (depends on the WebGraph
     * updatePageRanks() method
     * @return
     * The rank of the WebPage
     */
    public int getRank() {
        return rank;
    }

    /**
     * Gets the index of the WebPage
     * @return
     * The index of the WebPage
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the URL of the WebPage
     * @return
     * The URL of the WebPage
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the WebPage's rank
     * @param rank
     * The rank of the WebPage
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Adds a specified amount to the WebPage's rank
     * @param add
     * The amount to be added to the WebPage's rank
     */
    public void addToRank(int add){
        this.rank = this.rank + add;
    }

    /**
     * Setter for the WebPage's index
     * @param index
     * The index of the WebPage
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Gets the keywords of the WebPage
     * @return
     * The keywords of the WebPage
     */
    public ArrayList<String> getKeywords() {
        return keywords;
    }

    /**
     * Gets a string that represents the WebPage in a single row
     * @return
     * A string that represents the WebPage in a single row
     */
    public String toString(){
        return String.format("%-6d | %-22s | %-9d | %-22s | %-30s", index, url,
                rank, "***", keywords.toString().substring(1,
                        keywords.toString().length()-1));
    }

}
