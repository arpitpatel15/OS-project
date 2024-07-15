import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class BackgroundImagePanel extends JPanel {
    private Image backgroundImage;

    public BackgroundImagePanel(String fileName) {
        try {
            backgroundImage = ImageIO.read(new File("osimg.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

public class Combined extends JFrame {

    public Combined() {
        setTitle("Combined GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350);
        setLocationRelativeTo(null);

        BackgroundImagePanel mainPanel = new BackgroundImagePanel("background.jpg"); // Replace "background.jpg" with your image file path
        mainPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); 

        JButton btnPriorityScheduling = new JButton("Priority Scheduling");
        btnPriorityScheduling.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PrioritySchedulingGUI prioritySchedulingGUI = new PrioritySchedulingGUI();
                prioritySchedulingGUI.setVisible(true);
            }
        });
        mainPanel.add(btnPriorityScheduling, gbc);

        JButton btnReaderWritersProblem = new JButton("Reader Writers Problem");
        gbc.gridy++;
        btnReaderWritersProblem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReaderWritersProblemGUI readerWriterProblemGUI = new ReaderWritersProblemGUI();
                readerWriterProblemGUI.setVisible(true);
            }
        });
        mainPanel.add(btnReaderWritersProblem, gbc);

        JButton btnFCFSDiskScheduling = new JButton("FCFS Disk Scheduling");
        gbc.gridy++;
        btnFCFSDiskScheduling.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FCFSDiskGUI fcfsDiskSchedulingGUI = new FCFSDiskGUI();
                fcfsDiskSchedulingGUI.setVisible(true);
            }
        });
        mainPanel.add(btnFCFSDiskScheduling, gbc);

        JButton btnLRUGUI = new JButton("LRU Page Replacement");
        gbc.gridy++;
        btnLRUGUI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LRUGUI lruGUI = new LRUGUI();
                lruGUI.setVisible(true);
            }
        });
        mainPanel.add(btnLRUGUI, gbc);

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Combined().setVisible(true);
            }
        });
    }
}
