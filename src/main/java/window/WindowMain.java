package window;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class WindowMain extends JFrame {
    private JTable table;

    private WindowMain() throws IOException {
        super("Test Application");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

//        String[] columnNames = {
//                "Name","Major"
//        };



        //List<List<Object>> data = workers.Sheet.getValues();
        String spreadsheetId = "17B-dK9kHpdWW1X-0QxQkPH9VYG_1RGs6V2_XDZyzXhc";
        String page = "ЖКС2";

        String[] columnNames = workers.Sheet.getHeads(spreadsheetId, page);

        Object[][] data =  workers.Sheet.getValues(spreadsheetId, "ЖКС2","A2:B");

        table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(70, 150));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane);
    }

    public static void main(String[] args) throws IOException {
        WindowMain app = new WindowMain();
        app.setVisible(true);
        app.pack();
    }

}
