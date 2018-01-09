/*
* Взято с
* https://ru.wikibooks.org/wiki/Java/%D0%9F%D0%B5%D1%80%D0%B2%D0%BE%D0%B5_%D0%BE%D0%BA%D0%BD%D0%BE
 */

import java.awt.*;
//import java.awt.event.*;
import javax.swing.*;

public class CrowsCalc extends JFrame {
    private int crows = 0;
    private JLabel countLabel;
    private JButton addCrow;
    private JButton removeCrow;

    private CrowsCalc(){
        super("Crow calculator");
        //Подготавливаем компоненты объекта
        countLabel = new JLabel("Crows:" + crows);
        addCrow = new JButton("Add Crow");
        removeCrow = new JButton("Remove Crow");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Подготавливаем временные компоненты
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        //Расставляем компоненты по местам
        add(countLabel, BorderLayout.NORTH); //О размещении компонент поговорим позже
        buttonsPanel.add(addCrow);
        buttonsPanel.add(removeCrow);

        add(buttonsPanel, BorderLayout.SOUTH);
        initListeners();
    }

    private void initListeners() {
        addCrow.addActionListener(e -> {
            crows = crows + 1;   /* Добавляем одну ворону  */
            updateCrowCounter(); /* Сообщаем аппликации, что количество ворон изменилось  */
        });
        removeCrow.addActionListener(e -> {
            if (crows > 0) {
                crows = crows - 1;
                updateCrowCounter(); /* Сообщаем аппликации, что количество ворон изменилось  */
            } else{
                JOptionPane.showMessageDialog(this,
                        "Crows are finished yet!");
            }
        });
    }
    private void updateCrowCounter() {
        countLabel.setText("Crows:" + crows);
    }

    public static void main(String[] args) {
        CrowsCalc app = new CrowsCalc();
        app.setVisible(true);
        app.pack(); //Эта команда подбирает оптимальный размер в зависимости от содержимого окна
    }
}