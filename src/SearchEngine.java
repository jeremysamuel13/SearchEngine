/*
 * @author Jeremy Samuel
 * E-mail: jeremy.samuel@stonybrook.edu
 * Stony Brook ID: 113142817
 * CSE 214
 * Recitation Section 3
 * Recitation TA: Dylan Andres
 * HW #7
 */

import java.io.FileNotFoundException;
import java.util.*;

/**
 * SearchEngine class
 * Allows for user to interact with the WebGraph
 */
public class SearchEngine {
    public static final String PAGES_FILE = "pages.txt";
    public static final String LINKS_FILE = "links.txt";
    private WebGraph web = new WebGraph();

    public static void main(String[] args) {

        //flag for program menu loop
        boolean active = true;
        //flag for gui
        boolean guiActive = false;

        Scanner scan = new Scanner(System.in);
        String input;
        SearchEngine engine = new SearchEngine();

        //menu strings
        String menu = "\n(AP) - Add a new page to the graph.\n" +
                "(RP) - Remove a page from the graph.\n" +
                "(AL) - Add a link between pages in the graph.\n" +
                "(RL) - Remove a link between pages in the graph.\n" +
                "(P)  - Print the graph.\n" +
                "(S)  - Search for pages with a keyword.\n" +
                "(Q)  - Quit.\n";
        String printTableMenu = "\n(I) Sort based on index (ASC)\n" +
                "(U) Sort based on URL (ASC)\n" +
                "(R) Sort based on rank (DSC)\n";


        //Tries to load pages and links file, creates a new WebGraph if files
        // cannot be found.
        System.out.println("Loading WebGraph data...");
        try{
            engine.web = WebGraph.buildFromFiles(PAGES_FILE, LINKS_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found. Make sure files are " +
                    "named correctly and put in the proper directory. Restart" +
                    " program after doing so.");
            System.out.println("Creating an empty WebGraph...");
            engine.web = new WebGraph();
        }
        System.out.println("Success!");


        //EXTRA CREDIT: GUI
        WebGUI gui;

        System.out.println("\nWould you like to view the GUI for the " +
                "WebGraph (It is to be used with the console)? (enter y/n): ");
        String inp = scan.nextLine();
        while(!inp.trim().equalsIgnoreCase("y") &
                !inp.trim().equalsIgnoreCase("n")){
            System.out.println("ERROR: Invalid input\n\nWould you like to use" +
                    " the GUI version of SearchEngine? (enter y/n): ");
            inp = scan.nextLine();
        }
        if(inp.toLowerCase().equalsIgnoreCase("y")) {
            System.out.println("\nLoading GUI...");
            gui = new WebGUI(engine.web);
            gui.init();
            guiActive = true;
        }else{
            gui = null;
        }


        //Looped Menu
        while(active){
            System.out.println(menu);
            System.out.println("Please select an option: ");
            input = scan.nextLine();

            switch (input.toLowerCase()) {
                //Add Page
                case "ap" -> {
                    System.out.println("Enter a URL:  ");
                    String urlToAdd = scan.nextLine();
                    System.out.println("Enter keywords (space-separated): ");
                    String keywords = scan.nextLine();
                    keywords = keywords.trim();
                    ArrayList<String> list = new ArrayList<>(Arrays.
                            asList(keywords.split(" ")));
                    try {
                        engine.web.addPage(urlToAdd, list);
                        System.out.println(urlToAdd + " successfully added to" +
                                " the WebGraph!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("ERROR: " + e.getMessage());
                    }
                }
                //Remove Page
                case "rp" -> {
                    System.out.println("Enter a URL: ");
                    String urlToRemove = scan.nextLine();
                    engine.web.removePage(urlToRemove);
                    System.out.println(urlToRemove + " has been removed from " +
                            "the graph!");
                }
                //Add Link
                case "al" -> {
                    System.out.println("Enter a source URL: ");
                    String srcToAdd = scan.nextLine();
                    System.out.println("Enter a destination URL: ");
                    String destToAdd = scan.nextLine();
                    try {
                        engine.web.addLink(srcToAdd, destToAdd);
                        System.out.println("Link successfully added from " +
                                srcToAdd + " to " + destToAdd);
                    } catch (IllegalArgumentException e) {
                        System.out.println("ERROR: " + e.getMessage());
                    }
                }
                //Remove Link
                case "rl" -> {
                    System.out.println("Enter a source URL: ");
                    String srcToRemove = scan.nextLine();
                    System.out.println("Enter a destination URL: ");
                    String destToRemove = scan.nextLine();
                    engine.web.removeLink(srcToRemove, destToRemove);
                    System.out.println("Linked removed from " + srcToRemove
                            + " to " + destToRemove + "!");
                }
                //Print
                case "p" -> {
                    System.out.println(printTableMenu);
                    System.out.println("Please select an option: ");
                    input = scan.nextLine();
                    switch (input.toLowerCase()) {
                        //Sort by index
                        case "i" -> engine.web.printTable
                                (new WebPageIndexComparator());
                        //Sort by URL
                        case "u" -> engine.web.printTable
                                (new WebPageURLComparator());
                        //sort by rank
                        case "r" -> engine.web.printTable
                                (new WebPageRankComparator());
                        default -> System.out.println("Invalid input");
                    }
                }
                //Search
                case "s" -> {
                    System.out.println("Search keyword: ");
                    String keyword = scan.nextLine();
                    ArrayList<WebPage> p = engine.web.findPages(keyword);
                    if(p.size() != 0) {
                        p.sort(new WebPageRankComparator());
                        System.out.println();
                        System.out.printf("%-6s | %-8s | %-20s%n",
                                "Rank", "PageRank", "URL");
                        System.out.println("-".repeat(34));
                        for (WebPage l : p) {
                            System.out.printf("%-6s | %-8s | %-20s%n",
                                    p.indexOf(l) + 1, l.getRank(), l.getUrl());
                        }
                    }else{
                        System.out.println("No search results found for " +
                                "the keyword " + keyword + ".");
                    }
                }
                //Quit
                case "q" ->{
                    active = false;
                    System.out.println("\nGoodbye.");
                    if(guiActive) {
                        gui.jf.setVisible(false);
                        gui.jf.dispose();
                    }
                }
                default -> System.out.println("Invalid Input");
            }

            //updates gui after every loop ONLY if the gui is active
            if(guiActive)
                gui.model.fireTableDataChanged();

        }


    }
}
