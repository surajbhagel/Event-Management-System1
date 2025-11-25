package ems;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

/*
 Simple Swing GUI that uses EventManager (file-backed) so code runs without DB.
 To run:
 javac -d out src/ems/*.java
 java -cp out ems.EventManagementApp
*/
public class EventManagementApp {
    private final EventManager manager;
    private final JFrame frame = new JFrame("Event Management System");
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID","Name","Date","Venue","Type"},0);
    private final JTable table = new JTable(tableModel);

    private final JTextField idField = new JTextField(6);
    private final JTextField nameField = new JTextField(12);
    private final JTextField dateField = new JTextField(10);
    private final JTextField venueField = new JTextField(12);
    private final JComboBox<String> typeBox = new JComboBox<>(new String[]{"PUBLIC","PRIVATE"});

    public EventManagementApp(String storagePath) {
        manager = new EventManager(storagePath);
        initGUI();
        refreshTable();
        frame.setSize(900,500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void initGUI() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("ID:")); top.add(idField);
        top.add(new JLabel("Name:")); top.add(nameField);
        top.add(new JLabel("Date:")); top.add(dateField);
        top.add(new JLabel("Venue:")); top.add(venueField);
        top.add(new JLabel("Type:")); top.add(typeBox);

        JButton addBtn = new JButton("Add");
        JButton viewBtn = new JButton("View All");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton searchBtn = new JButton("Search");

        top.add(addBtn); top.add(viewBtn); top.add(updateBtn); top.add(deleteBtn); top.add(searchBtn);

        addBtn.addActionListener(e -> onAdd());
        viewBtn.addActionListener(e -> { manager.loadFromFile(); refreshTable(); });
        updateBtn.addActionListener(e -> onUpdate());
        deleteBtn.addActionListener(e -> onDelete());
        searchBtn.addActionListener(e -> onSearch());

        JPanel center = new JPanel(new BorderLayout());
        table.setFillsViewportHeight(true);
        center.add(new JScrollPane(table), BorderLayout.CENTER);

        frame.getContentPane().add(top, BorderLayout.NORTH);
        frame.getContentPane().add(center, BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if (e.getClickCount()==2){
                    int r = table.getSelectedRow();
                    if (r>=0){
                        idField.setText(tableModel.getValueAt(r,0).toString());
                        nameField.setText(tableModel.getValueAt(r,1).toString());
                        dateField.setText(tableModel.getValueAt(r,2).toString());
                        venueField.setText(tableModel.getValueAt(r,3).toString());
                        typeBox.setSelectedItem(tableModel.getValueAt(r,4).toString());
                    }
                }
            }
        });
    }

    private void onAdd(){
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String date = dateField.getText().trim();
            String venue = venueField.getText().trim();
            String type = (String) typeBox.getSelectedItem();
            Event e = "PUBLIC".equals(type) ? new PublicEvent(id,name,date,venue) : new PrivateEvent(id,name,date,venue);
            manager.addEvent(e);
            JOptionPane.showMessageDialog(frame, "Event added.");
            refreshTable();
            clearInputs();
        } catch(NumberFormatException nfe){
            JOptionPane.showMessageDialog(frame, "Invalid ID.");
        } catch(Exception ex){
            JOptionPane.showMessageDialog(frame, "Add failed: " + ex.getMessage());
        }
    }

    private void onUpdate(){
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String date = dateField.getText().trim();
            String venue = venueField.getText().trim();
            String type = (String) typeBox.getSelectedItem();
            Event e = "PUBLIC".equals(type) ? new PublicEvent(id,name,date,venue) : new PrivateEvent(id,name,date,venue);
            manager.updateEvent(e);
            JOptionPane.showMessageDialog(frame, "Event updated.");
            refreshTable();
            clearInputs();
        } catch(NumberFormatException nfe){
            JOptionPane.showMessageDialog(frame, "Invalid ID.");
        } catch(Exception ex){
            JOptionPane.showMessageDialog(frame, "Update failed: " + ex.getMessage());
        }
    }

    private void onDelete(){
        try {
            int id = Integer.parseInt(idField.getText().trim());
            manager.deleteEvent(id);
            JOptionPane.showMessageDialog(frame, "Event deleted.");
            refreshTable();
            clearInputs();
        } catch(NumberFormatException nfe){
            JOptionPane.showMessageDialog(frame, "Invalid ID.");
        } catch(Exception ex){
            JOptionPane.showMessageDialog(frame, "Delete failed: " + ex.getMessage());
        }
    }

    private void onSearch(){
        try {
            int id = Integer.parseInt(idField.getText().trim());
            Event e = manager.getEventById(id);
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{e.getId(), e.getName(), e.getDate(), e.getVenue(), e.getType()});
        } catch(NumberFormatException nfe){
            JOptionPane.showMessageDialog(frame, "Invalid ID.");
        } catch(Exception ex){
            JOptionPane.showMessageDialog(frame, "Search failed: " + ex.getMessage());
        }
    }

    private void refreshTable(){
        try {
            List<Event> list = manager.getAllEvents();
            tableModel.setRowCount(0);
            for (Event e : list) {
                tableModel.addRow(new Object[]{e.getId(), e.getName(), e.getDate(), e.getVenue(), e.getType()});
            }
        } catch(Exception ex){
            JOptionPane.showMessageDialog(frame, "Refresh failed: " + ex.getMessage());
        }
    }

    private void clearInputs(){
        idField.setText(""); nameField.setText(""); dateField.setText(""); venueField.setText("");
    }

    public static void main(String[] args) {
        // storage file path inside current folder
        String storage = "events.dat";
        // create folder 'data' to store file
        try { new File("data").mkdirs(); storage = "data/events.dat"; } catch(Exception ignored){}
        SwingUtilities.invokeLater(() -> new EventManagementApp(storage));
    }
}