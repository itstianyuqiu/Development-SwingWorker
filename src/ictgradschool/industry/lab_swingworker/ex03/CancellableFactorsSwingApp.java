package ictgradschool.industry.lab_swingworker.ex03;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Simple application to calculate the prime factors of a given number N.
 * <p>
 * The application allows the user to enter a value for N, and then calculates
 * and displays the prime factors. This is a very simple Swing application that
 * performs all processing on the Event Dispatch thread.
 */
public class CancellableFactorsSwingApp extends JPanel {

    private JButton _startBtn;        // Button to start the calculation process.
    private JButton _cancelBtn;
    private JTextArea _factorValues;  // Component to display the result.
    PrimeFactorisationWorker worker;

    public CancellableFactorsSwingApp() {
        // Create the GUI components.
        JLabel lblN = new JLabel("Value N:");
        final JTextField tfN = new JTextField(20);

        _startBtn = new JButton("Compute");
        _cancelBtn = new JButton("Abort");
        _factorValues = new JTextArea();
        _factorValues.setEditable(false);


        // Add an ActionListener to the start button. When clicked, the
        // button's handler extracts the value for N entered by the user from
        // the textfield and find N's prime factors.
        _startBtn.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent event) {
                String strN = tfN.getText().trim();
                worker = new PrimeFactorisationWorker(Long.parseLong(strN));
                worker.execute();

                _startBtn.setEnabled(false);
                _cancelBtn.setEnabled(true);

                // Clear any text (prime factors) from the results area.
                _factorValues.setText(null);

                // Set the cursor to busy.
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                // Restore the cursor.
                setCursor(Cursor.getDefaultCursor());
            }
        });

        _cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                _cancelBtn.setEnabled(false);
                _startBtn.setEnabled(true);
                worker.cancel(true);


            }
        });

        // Construct the GUI.
        JPanel controlPanel = new JPanel();
        controlPanel.add(lblN);
        controlPanel.add(tfN);
        controlPanel.add(_startBtn);
        controlPanel.add(_cancelBtn);

        JScrollPane scrollPaneForOutput = new JScrollPane();
        scrollPaneForOutput.setViewportView(_factorValues);

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        add(scrollPaneForOutput, BorderLayout.CENTER);
        setPreferredSize(new Dimension(500, 300));
    }

    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("Prime Factorisation of N");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane.
        JComponent newContentPane = new CancellableFactorsSwingApp();
        frame.add(newContentPane);

        // Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private class PrimeFactorisationWorker extends SwingWorker<List<Long>, Void> {
        Long n;

        public PrimeFactorisationWorker(long n) {
            this.n = n;
        }

        @Override
        protected List<Long> doInBackground() throws Exception {
            List<Long> result = new ArrayList<>();

            for (long i = 2; i * i <= n; i++) {
                if (isCancelled()) {
                    break;
                }
                // If i is a factor of N, repeatedly divide it out
                while (n % i == 0) {
                    result.add(i);
                    n = n / i;
                }
            }

            // if biggest factor occurs only once, n > 1
            if (n > 1) {
                result.add(n);
            }
            return result;
        }

        @Override
        protected void done() {
            List<Long> result = new ArrayList<>();
            try {
                result = get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            for (Long item : result) {
                _factorValues.append(item + "\n");
            }

            _startBtn.setEnabled(false);


        }
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }


}

