import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
public class LRUGUI extends JFrame {
    private JTextField inputField;
    private JTextField capacityField;
    private JButton calculateButton;
    private JLabel resultLabel;
    private DefaultTableModel tableModel;
    private JTable pageTable;
    public LRUGUI() {
        setTitle("LRU Page Replacement Algorithm");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel inputLabel = new JLabel("Page References (comma-separated):");
        inputField = new JTextField(20);
        JLabel capacityLabel = new JLabel("Capacity:");
        capacityField = new JTextField(5);
        calculateButton = new JButton("Calculate");
        inputPanel.add(inputLabel);
        inputPanel.add(inputField);
        inputPanel.add(capacityLabel);
        inputPanel.add(capacityField);
        inputPanel.add(calculateButton);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        add(inputPanel, c);
        JPanel resultPanel = new JPanel(new FlowLayout());
        resultLabel = new JLabel();
        resultPanel.add(resultLabel);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        add(resultPanel, c);
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Page Fault #");
        tableModel.addColumn("Page Fault");
        tableModel.addColumn("Memory Contents");
        pageTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(pageTable);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 2;
        add(tableScrollPane, c);
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculatePageFaults();
            }
        });
    }
    private void calculatePageFaults() {
        try {
            String input = inputField.getText();
            String[] inputArray = input.split(",");
            int[] pages = new int[inputArray.length];
            for (int i = 0; i < inputArray.length; i++) {
                pages[i] = Integer.parseInt(inputArray[i].trim());
            }

            int capacity = Integer.parseInt(capacityField.getText().trim());

            int pageFaults = pageFaults(pages, pages.length, capacity);
            resultLabel.setText("Page Faults: " + pageFaults);
        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid input. Please enter valid integers.");
        }
    }

    private int pageFaults(int pages[], int n, int capacity) {
        HashSet<Integer> s = new HashSet<>(capacity);
        HashMap<Integer, Integer> indexes = new HashMap<>();
        int page_faults = 0;
        for (int i = 0; i < n; i++) {
            if (s.size() < capacity) {
                if (!s.contains(pages[i])) {
                    s.add(pages[i]);
                    page_faults++;
                    updateTable(page_faults, pages[i], s);
                }
                indexes.put(pages[i], i);
            } else {
                if (!s.contains(pages[i])) {
                    int lru = Integer.MAX_VALUE, val = Integer.MIN_VALUE;
                    Iterator<Integer> itr = s.iterator();
                    while (itr.hasNext()) {
                        int temp = itr.next();
                        if (indexes.get(temp) < lru) {
                            lru = indexes.get(temp);
                            val = temp;
                            }
                        }
                        s.remove(val);
                        indexes.remove(val);
                        s.add(pages[i]);
                        page_faults++;
                        updateTable(page_faults, pages[i], s);
                    }
                    indexes.put(pages[i], i);
                }
            }
            return page_faults;
        }
    
        private void updateTable(int pageFaultNumber, int pageFault, HashSet<Integer> memory) {
            Object[] rowData = new Object[3];
            rowData[0] = pageFaultNumber;
            rowData[1] = pageFault;
            rowData[2] = memory.toString();
            tableModel.addRow(rowData);
        }
        public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    LRUGUI gui = new LRUGUI();
                    gui.setVisible(true);
                }
            });
        }
    }
    
