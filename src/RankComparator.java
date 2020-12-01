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
 * RankComparator class
 * Used to numerically sort WebPage by PageRank (Descending)
 */
public class RankComparator{
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
            return new WebPageRankComparator();
        }else if (flag == 2){
            return new WebGUIRankComparator();
        }else{
            throw new IllegalArgumentException("Invalid Flag");
        }
    }
}


class WebPageRankComparator implements Comparator<WebPage>{
    @Override
    public int compare(WebPage o1, WebPage o2) {
        return (-1) * Integer.compare(o1.getRank(), o2.getRank());
    }
}

class WebGUIRankComparator implements Comparator<Integer>{

    @Override
    public int compare(Integer o1, Integer o2) {
        return (-1) * Integer.compare(o1, o2);
    }
}