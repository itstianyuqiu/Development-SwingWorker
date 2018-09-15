package ictgradschool.industry.lab_swingworker.ex01;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

public class AwesomeProgram implements ActionListener {
    private JLabel progressLabel = new JLabel();
    private JLabel myLabel = new JLabel();
    private JButton myButton = new JButton();


    /**
     * Called when the button is clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        myButton.setEnabled(false);
// Start the SwingWorker running
        MySwingWorker worker = new MySwingWorker();
        worker.execute();  //instead of worker.doInBackground();


    }

    // When the SwingWorker has finished, display the result in
// myLabel.


    public int doStuffAndThings() {
        int something = 0;
        for (int i = 0; i < 100; i++) {
            something += i * i;
        }

        return something;
    }


    private class MySwingWorker extends SwingWorker<Integer, Integer> {

        protected Integer doInBackground() throws Exception {
            int result = 0;
            for (int i = 0; i < 100; i++) {
// Do some long-running stuff
                result += doStuffAndThings();
// Report intermediate results
                publish(i);
            }
            return result;
        }

         protected void process(int i){
            progressLabel.setText("Progress: " + i + "%");

         }

        public void done() {
            int result = 0;
            try {
                result = get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            myButton.setEnabled(true);
            myLabel.setText("Result: " + result);
        }


    }
}