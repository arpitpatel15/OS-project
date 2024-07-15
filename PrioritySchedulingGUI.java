import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;;
public class PrioritySchedulingGUI extends JFrame {
    JTextField tfNumProcesses;
    JButton btnSubmit;
    JTable table;
    DefaultTableModel model;
    JLabel lblAvgTurnaroundTime;
    JLabel lblAvgWaitingTime;
    JLabel ganttchart;
    static JTextArea ganttchartFigure;
    static List<Integer> inputList = new ArrayList<>();
    public PrioritySchedulingGUI() {
        setTitle("Priority Scheduling GUI");
        setSize(600, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.GRAY);
        JLabel lblNumProcesses = new JLabel("Enter the number of processes:");
        tfNumProcesses = new JTextField(10);
        btnSubmit = new JButton("Submit");
        btnSubmit.setBackground(Color.LIGHT_GRAY);
        panel.add(lblNumProcesses);
        panel.add(tfNumProcesses);
        panel.add(btnSubmit);
        model = new DefaultTableModel();
        model.addColumn("Process ID");
        model.addColumn("Arrival Time");
        model.addColumn("Burst Time");
        model.addColumn("Priority");
        model.addColumn("Finish Time");
        model.addColumn("Turnaround Time");
        model.addColumn("Waiting Time");
        table = new JTable(model);
        table.setBackground(Color.LIGHT_GRAY);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(panel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        ganttchart = new JLabel("Gantt chart: ");
        ganttchartFigure = new JTextArea();
        lblAvgTurnaroundTime = new JLabel("Average Turnaround Time: ");
        lblAvgWaitingTime = new JLabel("Average Waiting Time: ");
        JPanel bottomPanel = new JPanel(new GridLayout(4, 1));
        bottomPanel.add(ganttchart);
        bottomPanel.add(ganttchartFigure);
        bottomPanel.add(lblAvgTurnaroundTime);
        bottomPanel.add(lblAvgWaitingTime);
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int numProcesses = Integer.parseInt(tfNumProcesses.getText());
                int[] pid = new int[numProcesses];
                int[] at = new int[numProcesses];
                int[] bt = new int[numProcesses];
                int[] wt = new int[numProcesses];
                int[] tat = new int[numProcesses];
                int[] p = new int[numProcesses];
                for (int i = 0; i < numProcesses; i++) {
                    String[] rowData = new String[7];
                    rowData[0] = "P" + i;
                    rowData[1] = JOptionPane.showInputDialog("Enter arrival time for Process ID " + i + ":");
                    rowData[2] = JOptionPane.showInputDialog("Enter burst time for Process ID " + i + ":");
                    rowData[3] = JOptionPane.showInputDialog("Enter priority for Process ID " + i + ":");
                    model.addRow(rowData);
                    pid[i] = i;
                    at[i] = Integer.parseInt(rowData[1]);
                    bt[i] = Integer.parseInt(rowData[2]);
                    p[i] = Integer.parseInt(rowData[3]);
                }
                priority(pid, at, bt, wt, tat, p, numProcesses);
                List<Integer> outputList = removeConsecutiveDuplicates(inputList);
                for(Integer num : outputList)
                {
                    ganttchartFigure.append("| P" + num+" |");
                }
                int totalTurnaroundTime = 0;
                int totalWaitingTime = 0;
                for (int i = 0; i < numProcesses; i++) {
                    table.getModel().setValueAt(at[i] + tat[i], i, 4);
                    table.getModel().setValueAt(tat[i], i, 5);
                    table.getModel().setValueAt(wt[i], i, 6);
                    totalTurnaroundTime += tat[i];
                    totalWaitingTime += wt[i];
                }
                double avgTurnaroundTime = (double) totalTurnaroundTime / numProcesses;
                double avgWaitingTime = (double) totalWaitingTime / numProcesses;
                lblAvgTurnaroundTime.setText("Average Turnaround Time: " + avgTurnaroundTime);
                lblAvgWaitingTime.setText("Average Waiting Time: " + avgWaitingTime);   
            }
        });
    }
    static void priority(int[] pid, int[] at, int[] bt, int[] wt, int[] tat, int[] p, int size) {
        int[] remaining_time = new int[size];
        int current_time = 0;
        int completed = 0;
        for (int i = 0; i < size; i++) {
            remaining_time[i] = bt[i];
        }
        while (completed < size) {
            int selected = -1;
            int highest_priority = Integer.MAX_VALUE;
            for (int i = 0; i < size; i++) {
                if (at[i] <= current_time && remaining_time[i] > 0 && p[i] < highest_priority) {
                    selected = i;
                    highest_priority = p[i];
                }
            }
            if (selected == -1) {
                current_time++;
            } 
            else {
                inputList.add(selected);
                remaining_time[selected]--;
                current_time++;
                if (remaining_time[selected] == 0) {
                    completed++;
                    int finish_time = current_time;
                    int turnaround_time = finish_time - at[selected];
                    int waiting_time = turnaround_time - bt[selected];
                    wt[selected] = waiting_time;
                    tat[selected] = turnaround_time;
                }
            }
        }
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (pid[j] > pid[j + 1]) {
                    int temp = pid[j];
                    pid[j] = pid[j + 1];
                    pid[j + 1] = temp;

                    temp = at[j];
                    at[j] = at[j + 1];
                    at[j + 1] = temp;

                    temp = bt[j];
                    bt[j] = bt[j + 1];
                    bt[j + 1] = temp;

                    temp = wt[j];
                    wt[j] = wt[j + 1];
                    wt[j + 1] = temp;

                    temp = tat[j];
                    tat[j] = tat[j + 1];
                    tat[j + 1] = temp;

                    temp = p[j];
                    p[j] = p[j + 1];
                    p[j + 1] = temp;
                }
            }
        }
    }
    public static List<Integer> removeConsecutiveDuplicates(List<Integer> inputList) {
        List<Integer> outputList = new ArrayList<>();
        if (inputList.isEmpty()) {
            return outputList;
        }
        outputList.add(inputList.get(0));
        for (int i = 1; i < inputList.size(); i++) {
            if (!inputList.get(i).equals(inputList.get(i - 1))) {
                outputList.add(inputList.get(i));
            }
        }
        return outputList;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PrioritySchedulingGUI().setVisible(true);
            }
        });
    }
}