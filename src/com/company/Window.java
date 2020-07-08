package com.company;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Window extends JFrame {
    private int verticalLastValue = 0;
    private int verticalLastValueForTree = 0;
    private int horizontalLastValueForTree = 0;

    public Window() throws FileNotFoundException {
        super("Task7");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(300, 200);
        setSize(745, 285);
        setResizable(false);
        JPanel panel = new JPanel();
        add(panel);
        panel.setLayout(null);
        panel.add(table());
        panel.add(addAndRemovePanel(panel));
        panel.add(filePanel(panel));
        panel.add(graphPanel());
        setVisible(true);
    }

    public JPanel graphPanel() {
        JPanel panel = new JPanel();
        panel.setLocation(1, 1);
        panel.setSize(380, 140);
        panel.setLayout(null);
        JPanel panel1 = new JPanel();
        panel1.setLocation(110, 70);
        panel1.setSize(400, 160);
        panel1.setLayout(null);
        panel1.add(panel);
        panel.setBorder(new EtchedBorder());
        return panel1;
    }

    public JScrollBar verBar(JPanel panel, JPanel panel1, Component treePaint) {
        JScrollBar verticalBar = new JScrollBar(JScrollBar.VERTICAL, 0, panel.getHeight(), 0, treePaint.getHeight() + 10);
        verticalBar.addAdjustmentListener(e -> {
            for (int i = 0; i < ((JPanel) panel1.getComponentAt(1, 1)).getComponentCount(); i++) {
                if (!((JPanel) panel1.getComponentAt(1, 1)).getComponent(i).equals(verticalBar)) {
                    ((JPanel) panel1.getComponentAt(1, 1)).getComponent(i).setLocation(
                            ((JPanel) panel1.getComponentAt(1, 1)).getComponent(i).getX(),
                            (e.getValue() - verticalLastValueForTree > 0) ?
                                    ((JPanel) panel1.getComponentAt(1, 1)).getComponent(i).getY() - Math.abs(e.getValue() - verticalLastValueForTree) :
                                    ((JPanel) panel1.getComponentAt(1, 1)).getComponent(i).getY() + Math.abs(e.getValue() - verticalLastValueForTree)
                    );
                }
            }
            verticalLastValueForTree = e.getValue();
        });
        verticalBar.setLocation(panel1.getWidth() - 20, 0);
        verticalBar.setSize(20, panel1.getHeight() - 20);
        return verticalBar;
    }

    public JScrollBar horBar(JPanel panel, JPanel panel1, Component graphPaint) {
        JScrollBar horizontalBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, panel.getWidth(), 0, graphPaint.getWidth() + 10);
        horizontalBar.addAdjustmentListener(e -> {
            for (int i = 0; i < ((JPanel) panel1.getComponentAt(1, 1)).getComponentCount(); i++) {
                if (!((JPanel) panel1.getComponentAt(1, 1)).getComponent(i).equals(horizontalBar)) {
                    ((JPanel) panel1.getComponentAt(1, 1)).getComponent(i).setLocation(
                            (e.getValue() - horizontalLastValueForTree > 0) ?
                                    ((JPanel) panel1.getComponentAt(1, 1)).getComponent(i).getX() - Math.abs(e.getValue() - horizontalLastValueForTree) :
                                    ((JPanel) panel1.getComponentAt(1, 1)).getComponent(i).getX() + Math.abs(e.getValue() - horizontalLastValueForTree),
                            ((JPanel) panel1.getComponentAt(1, 1)).getComponent(i).getY()
                    );
                }
            }
            horizontalLastValueForTree = e.getValue();
        });
        horizontalBar.setLocation(0, panel1.getHeight() - 20);
        horizontalBar.setSize(panel1.getWidth() - 20, 20);
        return horizontalBar;
    }

    public JPanel addAndRemovePanel(JPanel panel) {
        JPanel addAndRemovePanel = new JPanel();
        addAndRemovePanel.setLayout(null);
        addAndRemovePanel.setLocation(10, 10);
        addAndRemovePanel.setSize(90, 225);
        addAndRemovePanel.setBorder(new EtchedBorder());
        addAndRemovePanel.add(new SpecialButton("+", 30, 30, 10, 10, new SpecialButton.ButtonListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = (JTable) ((JScrollPane) panel.getComponentAt(110, 10)).getViewport().getView();
                DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                dtm.setColumnCount(dtm.getColumnCount() + 1);
                for (int i = 0; i < table.getColumnCount(); i++) {
                    table.getColumnModel().getColumn(i).setMinWidth(50);
                    table.getColumnModel().getColumn(i).setMaxWidth(50);
                }
            }
        }));
        addAndRemovePanel.add(new SpecialButton("-", 30, 30, 50, 10, new SpecialButton.ButtonListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = (JTable) ((JScrollPane) panel.getComponentAt(110, 10)).getViewport().getView();
                if (table.getColumnCount() > 1) {
                    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                    dtm.setColumnCount(dtm.getColumnCount() - 1);
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        table.getColumnModel().getColumn(i).setMinWidth(50);
                        table.getColumnModel().getColumn(i).setMaxWidth(50);
                    }
                }
            }
        }));
        addAndRemovePanel.add(new SpecialButton("Connection", 70, 30, 10, 50, new SpecialButton.ButtonListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Graph graph = new Graph();
                    JTable table = (JTable) ((JScrollPane) panel.getComponentAt(110, 10)).getViewport().getView();
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        String[] strings = String.valueOf(table.getValueAt(0, i)).split("-");
                        if (strings.length == 1) {
                            graph.addVertex(Integer.parseInt(strings[0]));
                        } else if (strings.length == 2) {
                            graph.addEdge(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid data format");
                            break;
                        }
                    }
                    if (graph.connectedness()) {
                        JOptionPane.showMessageDialog(null, "Graph is connected");
                    } else {
                        JOptionPane.showMessageDialog(null, "Graph is unconnected");
                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "Invalid data format");
                }
            }
        }));
        addAndRemovePanel.add(new SpecialButton("Build graph", 70, 30, 10, 185, new SpecialButton.ButtonListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (((JPanel) panel.getComponentAt(110, 70).getComponentAt(1, 1)).getComponents().length != 0) {
                        for (int i = 0; i < ((JPanel) panel.getComponentAt(110, 70).getComponentAt(1, 1)).getComponents().length; i++) {
                            ((JPanel) panel.getComponentAt(110, 70).getComponentAt(1, 1)).remove(i);
                            i--;
                        }
                    }
                    for (int i = 0; i < ((JPanel) panel.getComponentAt(110, 70)).getComponents().length; i++) {
                        if (((JPanel) panel.getComponentAt(110, 70)).getComponents()[i] instanceof JScrollBar) {
                            ((JScrollBar) ((JPanel) panel.getComponentAt(110, 70)).getComponents()[i]).setValue(0);
                            ((JPanel) panel.getComponentAt(110, 70)).remove(i);
                            i--;
                        }
                    }
                    Graph graph = new Graph();
                    JTable table = (JTable) ((JScrollPane) panel.getComponentAt(110, 10)).getViewport().getView();
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        String[] strings = String.valueOf(table.getValueAt(0, i)).split("-");
                        if (strings.length == 1) {
                            graph.addVertex(Integer.parseInt(strings[0]));
                        } else if (strings.length == 2) {
                            graph.addEdge(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid data format");
                            break;
                        }
                    }
                    ((JPanel) panel.getComponentAt(110, 70).getComponentAt(1, 1)).add(graph.draw(0, 0));
                    if (
                            panel.getComponentAt(110, 70).getComponentAt(1, 1).getComponentAt(0, 0).getWidth() >
                                    panel.getComponentAt(110, 70).getComponentAt(1, 1).getWidth()
                    ) {
                        ((JPanel) panel.getComponentAt(110, 70)).add(horBar(
                                (JPanel) panel.getComponentAt(110, 70).getComponentAt(1, 1),
                                (JPanel) panel.getComponentAt(110, 70),
                                panel.getComponentAt(110, 70).getComponentAt(1, 1).getComponentAt(0, 0)
                        ));
                    }
                    if (
                            panel.getComponentAt(110, 70).getComponentAt(1, 1).getComponentAt(0, 0).getHeight() >
                                    panel.getComponentAt(110, 70).getComponentAt(1, 1).getHeight()
                    ) {
                        ((JPanel) panel.getComponentAt(110, 70)).add(verBar(
                                (JPanel) panel.getComponentAt(110, 70).getComponentAt(1, 1),
                                (JPanel) panel.getComponentAt(110, 70),
                                panel.getComponentAt(110, 70).getComponentAt(1, 1).getComponentAt(0, 0)
                        ));
                    }
                    panel.setVisible(false);
                    panel.setVisible(true);
                    JOptionPane.showMessageDialog(null, "При повторном нажатии граф будет отрисован иначе");
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "Invalid data format");
                }
            }
        }));
        return addAndRemovePanel;
    }

    public JScrollPane table() {
        JTable table = new JTable(1, 15);
        table.setTableHeader(null);
        table.setCellSelectionEnabled(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setBorder(new EtchedBorder());
        table.setRowHeight(30);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setMaxWidth(50);
            table.getColumnModel().getColumn(i).setMinWidth(50);
        }
        JScrollPane pane = new JScrollPane(table);
        pane.setBorder(new EtchedBorder());
        pane.setSize(400, 51);
        pane.setLocation(110, 10);
        return pane;
    }

    public void verticalBar(JPanel fileList, ButtonGroup group) {
        JScrollBar verticalBar = new JScrollBar(JScrollBar.VERTICAL, 0, 1, 0, (group.getButtonCount() - 11) * 15);
        verticalBar.setSize(15, 181);
        verticalBar.setLocation(165, 0);
        verticalBar.addAdjustmentListener(e -> {
            for (int j = 0; j < fileList.getComponents().length; j++) {
                if (!fileList.getComponent(j).equals(verticalBar)) {
                    fileList.getComponent(j).setLocation(
                            fileList.getComponent(j).getX(),
                            (e.getValue() - verticalLastValue > 0) ?
                                    fileList.getComponent(j).getY() - Math.abs(e.getValue() - verticalLastValue) :
                                    fileList.getComponent(j).getY() + Math.abs(e.getValue() - verticalLastValue));
                }
            }
            verticalLastValue = e.getValue();
        });
        fileList.add(verticalBar);
    }

    public JPanel filePanel(JPanel panel) throws FileNotFoundException {
        JPanel filePanel = new JPanel();
        filePanel.setSize(200, 225);
        filePanel.setLocation(520, 10);
        filePanel.setBorder(new EtchedBorder());
        filePanel.setLayout(null);
        ArrayList<File> listOfFile = new ArrayList<>();
        File file = new File("FileList.txt");
        JPanel fileList = new JPanel();
        fileList.setSize(180, 181);
        fileList.setLocation(10, 35);
        fileList.setBorder(new EtchedBorder());
        fileList.setLayout(null);
        Scanner fileReader = new Scanner(file);
        ButtonGroup group = new ButtonGroup();
        int i = 0;
        while (fileReader.hasNextLine()) {
            String fileName = fileReader.nextLine();
            listOfFile.add(new File("files\\" + fileName));
            group.add(new SpecialRadioButton(fileName, fileList, fileName.length() * 10, 15 * i++, false));
        }
        if (group.getButtonCount() > 11) {
            verticalBar(fileList, group);
        }
        filePanel.add(fileList);
        fileReader.close();
        filePanel.add(new SpecialButton("Open", 80, 25, 10, 5, new SpecialButton.ButtonListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < listOfFile.size(); i++) {
                    if (((JRadioButton) fileList.getComponent(i)).isSelected()) {
                        try {
                            Scanner readText = new Scanner(listOfFile.get(i));
                            String[] strings = readText.nextLine().split(" ");
                            JTable table = (JTable) ((JScrollPane) panel.getComponentAt(110, 10)).getViewport().getView();
                            DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                            dtm.setColumnCount(strings.length);
                            for (int j = 0; j < strings.length; j++) {
                                table.setValueAt(strings[j], 0, j);
                                table.getColumnModel().getColumn(j).setMinWidth(50);
                                table.getColumnModel().getColumn(j).setMaxWidth(50);
                            }
                            readText.close();
                        } catch (FileNotFoundException fileNotFoundException) {
                            fileNotFoundException.printStackTrace();
                        }
                    }
                }
            }
        }));
        filePanel.add(new SpecialButton("Save", 80, 25, 110, 5, new SpecialButton.ButtonListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = (JTable) ((JScrollPane) panel.getComponentAt(110, 10)).getViewport().getView();
                JFrame frame = new JFrame();
                frame.setResizable(false);
                frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setSize(200, 180);
                frame.setLayout(null);
                JTextField textField = new JTextField();
                frame.add(textField);
                textField.setSize(165, 30);
                textField.setLocation(10, 50);
                JLabel label = new JLabel("Write file name:");
                frame.add(label);
                label.setSize(180, 30);
                label.setLocation(35, 10);
                label.setFont(new Font("LOL", Font.BOLD, 14));
                frame.add(new SpecialButton("Ok", 40, 30, 70, 90, new SpecialButton.ButtonListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            File newFile = new File("files\\" + textField.getText() + ".txt");
                            listOfFile.add(newFile);
                            FileWriter writer = new FileWriter(newFile);
                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < table.getColumnCount(); i++) {
                                builder.append(table.getValueAt(0, i)).append(" ");
                            }
                            StringBuilder builder1 = new StringBuilder();
                            Scanner readFile = new Scanner(new File("FileList.txt"));
                            int fileCount = 0;
                            while (readFile.hasNextLine()) {
                                builder1.append(readFile.nextLine()).append("\n");
                                fileCount++;
                            }
                            builder1.append(textField.getText()).append(".txt").append("\n");
                            FileWriter writer1 = new FileWriter("FileList.txt");
                            writer1.write(String.valueOf(builder1));
                            writer.write(String.valueOf(builder));
                            writer.close();
                            writer1.close();
                            frame.dispose();
                            JRadioButton radioButton = new SpecialRadioButton(textField.getText() + ".txt", fileList, (textField.getText() + ".txt").length() * 10, 15 * group.getButtonCount() - verticalLastValue, false);
                            group.add(radioButton);
                            radioButton.setVisible(false);
                            radioButton.setVisible(true);
                            if (fileCount == 11) {
                                verticalBar(fileList, group);
                            } else if (fileCount > 11) {
                                ((JScrollBar) fileList.getComponentAt(165, 0)).setMaximum(((JScrollBar) fileList.getComponentAt(165, 0)).getMaximum() + 15);
                            }
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }));
            }
        }));
        return filePanel;
    }

    public static class SpecialRadioButton extends JRadioButton {
        SpecialRadioButton(String name, JPanel panel, int width, int y, boolean select) {
            super(name, select);
            setSize(width, 15);
            setLocation(2, 5 + y);
            setFocusable(false);
            setBackground(null);
            setContentAreaFilled(false);
            setFont(new Font("Lol", Font.BOLD, 10));
            panel.add(this);
        }
    }
}