/*
 * @author Jeremy Samuel
 * E-mail: jeremy.samuel@stonybrook.edu
 * Stony Brook ID: 113142817
 * CSE 214
 * Recitation Section 3
 * Recitation TA: Dylan Andres
 * HW #7
 */

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Graphical representation of the WebGraph. It is to be used along with the
 * console.
 * NOTE: GUI UPDATES EVERYTIME THERE IS AN ALTERATION IN THE WEBGRAPH
 */
public class WebGUI extends JPanel {

    final static int WIDTH = 1000;
    final static int HEIGHT = 600;

    JTable jt;
    JFrame jf;
    WebPageTableModel model;
    JTableHeader jth;

    /**
     * Constructor for WebGUI
     * @param web
     * The WebGraph to be visually represented
     */
    public WebGUI(WebGraph web){
        model = new WebPageTableModel(web);
        jf = new JFrame();
        jt = new JTable(model);
    }

    /**
     * Sets all the settings of the JTable, JFrame and other components
     */
    public void init(){
        jf.setLayout(new BorderLayout());

        jf.setTitle("Search Engine");
        jf.setSize(new Dimension(WIDTH, HEIGHT));
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        sort();

        jth = jt.getTableHeader();
        jth.setBackground(Color.WHITE);
        jth.setForeground(Color.BLACK);
        jth.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        jth.setReorderingAllowed(false);
        jth.setResizingAllowed(false);

        jt.setGridColor(Color.BLACK);
        jt.setSize(new Dimension(WIDTH, HEIGHT));
        jt.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        jf.setLocationRelativeTo(null);
        jf.setAlwaysOnTop(true);

        JScrollPane jsp = new JScrollPane(jt);
        jsp.setPreferredSize(new Dimension(WIDTH, HEIGHT - 75));

        jf.add(jsp, BorderLayout.CENTER);

        JLabel jl = new JLabel("Press the column headers to sort " +
                "(only works for index, url and pagerank).");
        jl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        jl.setHorizontalAlignment(SwingConstants.CENTER);
        jf.add(jl, BorderLayout.PAGE_END);


        jf.setVisible(true);
        jf.setAlwaysOnTop(false);
    }

    /**
     * Sets comparator for each column and if each column is sortable or not
     */
    public void sort(){
        TableRowSorter<WebPageTableModel> sorter = new TableRowSorter<>(model);
        sorter.setComparator(0, IndexComparator.
                comparatorChooser(2));
        sorter.setComparator(1, URLComparator.
                comparatorChooser(2));
        sorter.setComparator(2, RankComparator.
                comparatorChooser(2));
        sorter.setSortable(3, false);
        sorter.setSortable(4, false);
        jt.setRowSorter(sorter);
    }

    /**
     * Table model for WebGUI JTable
     */
    static class WebPageTableModel extends AbstractTableModel{

        private final String[] ColumnNames = {"Index", "URL", "PageRank",
                "Links", "Keywords"};
        ArrayList<WebPage> p;
        WebGraph wg;

        @Override
        public String getColumnName(int column) {
            return ColumnNames[column];
        }

        public WebPageTableModel(WebGraph g){
            p = g.getPages();
            wg = g;
        }

        @Override
        public int getRowCount() {
            return p.size();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0 -> {
                    return p.get(rowIndex).getIndex();
                }
                case 1 -> {
                    return p.get(rowIndex).getUrl();
                }
                case 2 -> {
                    return p.get(rowIndex).getRank();
                }
                case 3 -> {
                    try {
                        return wg.getLinksIndex(p.get(rowIndex).getUrl()).
                                toString().replace("[", "").
                                replace("]", "");
                    }catch (IllegalArgumentException ignored){
                        return "";
                    }
                }
                case 4 -> {
                    return p.get(rowIndex).getKeywords().toString().substring
                            (1, p.get(rowIndex).getKeywords().toString().
                                    length() - 1);
                }
                default -> {
                    return null;
                }
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }


    }
}
