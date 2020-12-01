/*
 * @author Jeremy Samuel
 * E-mail: jeremy.samuel@stonybrook.edu
 * Stony Brook ID: 113142817
 * CSE 214
 * Recitation Section 3
 * Recitation TA: Dylan Andres
 * HW #7
 */

import java.io.*;
import java.util.*;

/**
 * WebGraph class
 * Holds all of the WebPages
 */
public class WebGraph {
    public static final int MAX_PAGES = 40;
    public int current_pages = -1;

    private final String tableHeader = "\n" + String.format("%-6s | %-22s | " +
                    "%-9s | %-22s | %-30s", "Index", "URL", "PageRank", "Links",
            "Keywords") + "\n" + "-".repeat(120);



    private ArrayList<WebPage> pages;

    //2d matrix
    private ArrayList<ArrayList<Integer>> edges;

    /**
     * Constructor
     */
    public WebGraph(){
        pages = new ArrayList<>();
        edges = new ArrayList<>(MAX_PAGES);
        for (int i = 0; i < MAX_PAGES; i++) {
            edges.add(new ArrayList<>(Collections.nCopies(40, 0)));
        }
    }

    /**
     * Creates a WebGraph using information from a linksFile and a pagesFile
     * @param pagesFile
     * The name of the file that contains the list of all the pages
     * @param linksFile
     * The name of the file that contains the list of all the links between
     * pages
     * @return
     * A WebGraph that has all the pages and their links
     * @throws FileNotFoundException
     * Throws exception when the given file is not found
     */
    public static WebGraph buildFromFiles(String pagesFile, String linksFile)
            throws FileNotFoundException {

        WebGraph x = new WebGraph();

        //adding pages
        Scanner scan = new Scanner(new File(pagesFile));
        while(scan.hasNextLine()){
            String line = scan.nextLine();
            line = line.trim();
            String[] split = line.split(" ");
            List<String> list = Arrays.asList(split);
            try {
                x.addPage(list.get(0), new ArrayList<>
                        (list.subList(1, list.size())));
            }catch (IllegalArgumentException e){
                System.out.println("ERROR: "  + e.getMessage());
            }

        }

        //adding links
        scan = new Scanner(new File(linksFile));
        while(scan.hasNextLine()){
            String line = scan.nextLine();
            line = line.trim();
            String[] split = line.split(" ");
            try {
                x.addLink(split[0], split[1]);
            }catch (IllegalArgumentException e){
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        x.updatePageRanks();

        return x;

    }

    /**
     * Retrieves a WebPage from the pages ArrayList that holds the same URL
     * as the given input
     * @param url
     * The URL of the WebPage to be retrieved
     * @return
     * The WebPage with the matching URL
     */
    public WebPage retrievePage(String url){
        if(current_pages == -1)
            return null;

        for (WebPage x: pages) {
            if(url.contains(".") && x.getUrl().equals(url)){
                return x;
            }
        }

        return null;
    }


    /**
     * Adds a page to the pages ArrayList and extends the usable parts of the
     * edges 2d matrix
     * @param url
     * The URL of the page to be added
     * @param keywords
     * The keywords of the page to be added
     * @throws IllegalArgumentException
     * Throws exception if URL is blank, if the page already exists in the
     * WebGraph, if the keywords provided are empty. or if the page capacity
     * has been reached
     */
    public void addPage(String url, ArrayList<String> keywords)
            throws IllegalArgumentException{
        keywords.removeAll(Collections.singleton(" "));
        keywords.removeAll(Collections.singleton(""));
        if(url.replaceAll(" ", "").isEmpty())
            throw new IllegalArgumentException("Invalid URL");
        else if (retrievePage(url) != null)
            throw new IllegalArgumentException(url + " already exists in the " +
                    "WebGraph. Could not add new WepPage.");
        else if(keywords.isEmpty())
            throw new IllegalArgumentException("Keywords are empty");
        else if(current_pages + 1 == MAX_PAGES)
            throw new IllegalArgumentException("Page capacity has been " +
                    "reached");


        pages.add(new WebPage(url, keywords, current_pages + 1));
        current_pages++;

        updatePageRanks();
    }

    /**
     * Adds link between two WebPages
     * @param source
     * The URL of the source
     * @param destination
     * The URL of the destination
     * @throws IllegalArgumentException
     * Throws exception if there is no existing WebPage found for either the
     * source or destination URLs
     */
    public void addLink(String source, String destination)
            throws IllegalArgumentException{
        WebPage src = retrievePage(source);
        WebPage dest = retrievePage(destination);

        if(src == null){
            throw new IllegalArgumentException(source + " could not be found " +
                    "in the WebGraph");
        }else if(dest == null){
            throw new IllegalArgumentException(destination + " could not be " +
                    "found in the WebGraph");
        }

        edges.get(src.getIndex()).set(dest.getIndex(), 1);

        updatePageRanks();

    }

    /**
     * Removes a page from the WebGraph. (Does nothing if page doesn't exists)
     * @param url
     * The URl of the page to be removed
     */
    public void removePage(String url){
        WebPage x = retrievePage(url);

        if(x != null){
            for (int i = pages.indexOf(x); i < pages.size(); i++) {
                pages.get(i).setIndex(pages.get(i).getIndex() - 1);
            }

            pages.remove(x);

            for (ArrayList<Integer> list: edges) {
                list.remove(x.getIndex() + 1);


                //maintains size of each sublist in edges
                list.add(0);
            }

            edges.remove(x.getIndex() + 1);

            //maintains the size of edges
            edges.add(new ArrayList<>(Collections.nCopies(40, 0)));

            current_pages--;
            updatePageRanks();
        }
    }

    /**
     * Removes a link between two WebPages (WebGraph remains unchanged if
     * source/destination is not found)
     * @param source
     * The source URL
     * @param destination
     * The destination URL
     */
    public void removeLink(String source, String destination){
        WebPage src = retrievePage(source);
        WebPage dest = retrievePage(destination);

        if(src != null && dest != null){
            edges.get(src.getIndex()).set(dest.getIndex(), 0);
        }

        updatePageRanks();

    }

    /**
     * Updates the ranks of all the WebPages in the WebGraph
     */
    public void updatePageRanks(){
        //reset all ranks
        for (WebPage q: pages) {
            q.setRank(0);
        }

        //find all new ranks
        for (int q = 0; q < current_pages + 1; q++) {
            for (int i = 0; i < current_pages + 1; i++) {
                pages.get(i).addToRank(edges.get(q).get(i));
            }
        }

        //prevents a page from having a rank of 0
        for (WebPage q: pages) {
            if(q.getRank() == 0)
                q.setRank(1);
        }

    }

    /**
     * Prints the table in a tabular form
     */
    public void printTable(){
        System.out.println(tableHeader);

        for (int i = 0; i < current_pages + 1; i++) {
            String x = pages.get(i).toString();
            String links = "";
            for (int j = 0; j < current_pages + 1; j++) {
                if(edges.get(i).get(j) == 1){
                    if(links.equals("")){
                        links  = links + j;
                    }else{
                        links = links + ", " + j;
                    }
                }
            }

            x = x.replace("***                   ", String.
                    format("%-22s", links));
            System.out.println(x);
        }

    }

    /**
     * Prints the table in a tabular form. The printed table is to be sorted
     * based on a given comparator
     * @param comparator
     * The comparator to be used to sort the table
     */
    public void printTable(Comparator<WebPage> comparator){
        ArrayList<WebPage> sortedPages = new ArrayList<>(pages);
        sortedPages.sort(comparator);

        System.out.println(tableHeader);

        for (int i = 0; i < current_pages + 1; i++) {
            String x = sortedPages.get(i).toString();
            String links = "";
            int index = sortedPages.get(i).getIndex();
            for (int j = 0; j < current_pages + 1; j++) {
                if(edges.get(index).get(j) == 1){
                    if(links.equals("")){
                        links  = links + j;
                    }else{
                        links = links + ", " + j;
                    }
                }
            }

            x = x.replace("***                   ", String.
                    format("%-22s", links));
            System.out.println(x);
        }


    }

    /**
     * Finds pages that contain a given keyword
     * @param keyword
     * The keyword to be used to search for a page
     * @return
     * An ArrayList of WebPages that contain this keyword
     */
    public ArrayList<WebPage> findPages(String keyword){
        ArrayList<WebPage> list = new ArrayList<>();
        for (WebPage p: pages) {
            if(p.getKeywords().contains(keyword.trim())){
                list.add(p);
            }
        }

        return list;

    }

    /**
     * Gets all the links of a given WebPage by using a given URL
     * @param url
     * The URL of the WebPage to be retrieved
     * @return
     * An ArrayList of Integers that show the index of all the links of the
     * given WebPage
     * @throws IllegalArgumentException
     * Throws exception when the WebPage does not exist in the WebGraph
     */
    public ArrayList<Integer> getLinksIndex(String url) throws
            IllegalArgumentException{
        updatePageRanks();
        WebPage w = retrievePage(url);
        if(w == null)
            throw new IllegalArgumentException("URL does not exist in the " +
                    "WebGraph");
        int index = w.getIndex();

        ArrayList<Integer> links = new ArrayList<>();

        for (int i = 0; i < current_pages + 1; i++) {
            if(edges.get(index).get(i) == 1){
                links.add(i);
            }
        }

        return links;
    }

    /**
     * Gets the pages
     * @return
     * The ArrayList of WebPages
     */
    public ArrayList<WebPage> getPages() {
        return pages;
    }

}
