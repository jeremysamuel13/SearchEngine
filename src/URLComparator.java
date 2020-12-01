/*
 * @author Jeremy Samuel
 * E-mail: jeremy.samuel@stonybrook.edu
 * Stony Brook ID: 113142817
 * CSE 214
 * Recitation Section 3
 * Recitation TA: Dylan Andres
 * HW #7
 */

import java.util.Comparator;

/**
 * URLComparator class
 * Used to alphabetically sort WebPage by URL (ASCENDING)
 */
public class URLComparator{
    /**
     * Chooses a certain comparator based on flag
     * @param flag
     * 1 -> comparator used to compare webpages
     * 2 -> comparator used to compare the variable within the WebPage (used
     * in WebGUI CLASS)
     * @return
     * A comparator based on the flag provided
     */
    public static Comparator<?> comparatorChooser(int flag){
        if(flag == 1){
            return new WebPageURLComparator();
        }else if (flag == 2){
            return new WebGUIURLComparator();
        }else{
            throw new IllegalArgumentException("Invalid Flag");
        }
    }
}

class WebPageURLComparator implements Comparator<WebPage>{
    @Override
    public int compare(WebPage o1, WebPage o2) {
        return compareLetter(o1.getUrl(),o2.getUrl(),0);
    }

    public int compareLetter(String o1, String o2,int index){
        int x = Character.compare(o1.trim().toLowerCase().
                charAt(index), o2.trim().toLowerCase().charAt(index));
        if(x != 0)
            return x;
        try {
            return compareLetter(o1, o2, index + 1);
        }catch (Exception e){
            return 0;
        }

    }
}

class WebGUIURLComparator implements Comparator<String>{

    @Override
    public int compare(String o1, String o2) {
        return compareLetter(o1,o2,0);
    }

    public int compareLetter(String o1, String o2,int index){
        int x = Character.compare(o1.trim().toLowerCase().
                charAt(index), o2.trim().toLowerCase().charAt(index));
        if(x != 0)
            return x;
        try {
            return compareLetter(o1, o2, index + 1);
        }catch (Exception e){
            return 0;
        }

    }
}
