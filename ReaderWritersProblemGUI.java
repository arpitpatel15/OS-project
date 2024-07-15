import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;
public class ReaderWritersProblemGUI extends JFrame {
    private final Semaphore readLock = new Semaphore(1);
    private final Semaphore writeLock = new Semaphore(1);
    private int readCount = 0;
    private JTextArea textArea;
    public ReaderWritersProblemGUI() {
        setTitle("Reader Writers Problem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        textArea = new JTextArea();
        textArea.setBackground(Color.white);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.setBackground(Color.WHITE);
        JButton readButton = new JButton("Read");
        readButton.setBackground(Color.lightGray);
        readButton.setForeground(Color.BLACK);
        readButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new Read()).start();
            }
        });
        buttonPanel.add(readButton);
        JButton writeButton = new JButton("Write");
        writeButton.setBackground(Color.lightGray);
        writeButton.setForeground(Color.BLACK);
        writeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new Write()).start();
            }
        });
        buttonPanel.add(writeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    class Read implements Runnable {
        public void run() {
            try {
                readLock.acquire();
                readCount++;
                if (readCount == 1) {
                    writeLock.acquire();
                }
                readLock.release();
                appendText(Thread.currentThread().getName() + " is Reading");
                Thread.sleep(1500);
                appendText(Thread.currentThread().getName() + " has Finished Reading");
                readLock.acquire();
                readCount--;
                if (readCount == 0) {
                    writeLock.release();
                }
                readLock.release();
            } catch (InterruptedException e) {
                appendText(e.getMessage());
            }
        }
    }
    class Write implements Runnable {
        public void run() {
            try {
                writeLock.acquire();
                appendText(Thread.currentThread().getName() + " is Writing");
                Thread.sleep(2500);
                appendText(Thread.currentThread().getName() + " has Finished Writing");
                writeLock.release();
            } catch (InterruptedException e) {
                appendText(e.getMessage());
            }
        }
    }
    private void appendText(String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                textArea.append(text + "\n");
            }
        });
    }    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ReaderWritersProblemGUI frame = new ReaderWritersProblemGUI();
                frame.setVisible(true);
            }
        });
    }
}