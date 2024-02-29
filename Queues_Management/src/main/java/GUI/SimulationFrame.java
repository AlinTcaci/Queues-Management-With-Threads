package GUI;

import business_logic.SelectionPolicy;
import business_logic.SimulationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimulationFrame extends JFrame {

    private JPanel contentPane;
    private JTextField minArrivalTimeLabel;
    private JTextField minProcessingTimeLabel;
    private JTextField nrOfQueuesLabel;
    private JComboBox strategyBox;
    private JTextField maxArrivalTimeLabel;
    private JTextField maxProcessingTimeLabel;
    private JTextField nrOfClientsLabel;
    private JTextField simulationTimeLabel;
    private JButton startButton;
    public JLabel increaseTimeLabel;
    public JLabel peakHourLabel;
    public JLabel avgWaitingTimeLabel;
    public JLabel avgServiceTimeLabel;
    public JLabel waitingQueueLabel;
    public JTextArea serversLabel;

    public SimulationFrame() {
        add(contentPane);
        pack();
        setMinimumSize(new Dimension(650, 700));
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStartButton();
            }
        });

    }
    private void onStartButton(){
        int simulationTime = -1;
        int minArrivalTime = -1;
        int maxArrivalTime = -1;
        int minProcessingTime = -1;
        int maxProcessingTime = -1;
        int numberOfServers = -1;
        int numberOfClients = -1;
        SelectionPolicy policy = SelectionPolicy.valueOf(getStrategy());

        try{
            if((simulationTime = getSimulationTimeLabel()) < 0){
                throw new Exception("SimulationTime must be positive");
            }
            if((minArrivalTime = getMinArrivalTimeLabel()) < 0){
                throw new Exception("MinArrivalTime must be positive");
            }
            if((maxArrivalTime = getMaxArrivalTimeLabel()) < 0){
                throw new Exception("MaxArrivalTime must be positive");
            }
            if((minProcessingTime = getMinProcessingTimeLabel()) < 0){
                throw new Exception("MinProcessingTime must be positive");
            }
            if((maxProcessingTime = getMaxProcessingTimeLabel()) < 0){
                throw new Exception("MaxProcessingTime must be positive");
            }
            if((numberOfClients = getNrOfClients()) < 0){
                throw new Exception("NrOfClients must be positive");
            }
            if((numberOfServers = getNrOfServers()) < 0){
                throw new Exception("NrOfServers must be positive");
            }
        }catch (Exception e){
            String errorMessage = e.getMessage();
            JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        }

        SimulationManager simulationManager = new SimulationManager(simulationTime, minArrivalTime, maxArrivalTime, minProcessingTime, maxProcessingTime, numberOfServers, numberOfClients, policy, this);
        Thread t = new Thread(simulationManager);
        t.start();
    }
    private int getMinArrivalTimeLabel() throws NumberFormatException {
        String labelText = minArrivalTimeLabel.getText();
        if (labelText.isEmpty()) {
            throw new NumberFormatException("MinArrivalTime label is empty");
        }
        return Integer.parseInt(labelText);
    }
    private int getSimulationTimeLabel() throws NumberFormatException {
        String labelText = simulationTimeLabel.getText();
        if (labelText.isEmpty()) {
            throw new NumberFormatException("SimulationTime label is empty");
        }
        return Integer.parseInt(labelText);
    }
    private int getMaxArrivalTimeLabel() throws NumberFormatException {
        String labelText = maxArrivalTimeLabel.getText();
        if (labelText.isEmpty()) {
            throw new NumberFormatException("MaxArrivalTime label is empty");
        }
        return Integer.parseInt(labelText);
    }
    private int getMinProcessingTimeLabel() throws NumberFormatException {
        String labelText = minProcessingTimeLabel.getText();
        if (labelText.isEmpty()) {
            throw new NumberFormatException("MinProcessingTime label is empty");
        }
        return Integer.parseInt(labelText);
    }
    private int getMaxProcessingTimeLabel() throws NumberFormatException {
        String labelText = maxProcessingTimeLabel.getText();
        if (labelText.isEmpty()) {
            throw new NumberFormatException("MaxProcessingTime label is empty");
        }
        return Integer.parseInt(labelText);
    }
    private int getNrOfServers() throws NumberFormatException {
        String labelText = nrOfQueuesLabel.getText();
        if (labelText.isEmpty()) {
            throw new NumberFormatException("NrOfQueues label is empty");
        }
        return Integer.parseInt(labelText);
    }
    private int getNrOfClients() throws NumberFormatException {
        String labelText = nrOfClientsLabel.getText();
        if (labelText.isEmpty()) {
            throw new NumberFormatException("NrOfClients label is empty");
        }
        return Integer.parseInt(labelText);
    }
    public String getStrategy() {
        return (String) this.strategyBox.getSelectedItem();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    SimulationFrame frame = new SimulationFrame();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
