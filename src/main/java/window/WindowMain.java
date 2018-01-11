package window;

import com.google.common.collect.Table;
import workers.Properties;
import workers.WorkerSheet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class WindowMain extends JFrame implements ActionListener {
    private JTable table;
    private JMenuBar menuBar;
    private JMenu pages;
    private JScrollPane scrollPane;
    private ArrayList<String> pageTitles;
    private String spreadsheetId = new Properties().load("/sheets.properties").getProperty("table");

    private WindowMain() throws IOException {
        super("Test Application");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        menuBar = new JMenuBar();
        pages = new JMenu("Pages");
//        String[] columnNames = {
//                "Name","Major"
//        };
        //List<List<Object>> data = workers.Sheet.getValues();
        menuBar.add(pages);
        pageTitles = WorkerSheet.getPages(spreadsheetId);
        for (String string: pageTitles) {
            JMenuItem menuItem = new JMenuItem(string);
            menuItem.addActionListener(this);
            pages.add(menuItem);
        }
        setJMenuBar(menuBar);
        String page = pageTitles.get(0);

        String[] columnNames = workers.WorkerSheet.getHeads(spreadsheetId, page);

        Object[][] data =  workers.WorkerSheet.getValues(spreadsheetId, page,"A2:B");

        TableModel tableModel = new DefaultTableModel(data,columnNames);
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(70, 150));
        table.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(table);

        add(scrollPane);
    }

    public static void main(String[] args) throws IOException {
        WindowMain app = new WindowMain();
        app.setVisible(true);
        app.pack();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String page = ((JMenuItem) event.getSource()).getText();
        String[] columnNames = null;
        Object[][] data = null;
        try {
            columnNames = workers.WorkerSheet.getHeads(spreadsheetId, page);
            data = workers.WorkerSheet.getValues(spreadsheetId, page, "A2:B");
        }catch (IOException exception){
            exception.printStackTrace();
            System.exit(1);
        }

        //DefaultTableModel model = (DefaultTableModel) table.getModel();

        table.setModel(new DefaultTableModel(data,columnNames));

        //table.removeColumn();
//        table = new JTable(data, columnNames);
//        table.setPreferredScrollableViewportSize(new Dimension(70, 150));
//        table.setFillsViewportHeight(true);
        //table.firePropertyChange();
        //scrollPane = new JScrollPane(table);
        //this.remove(this.);
        //remove(scrollPane);
        //add(scrollPane);
    }
}
