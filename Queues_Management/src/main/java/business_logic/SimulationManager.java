package business_logic;

import GUI.SimulationFrame;
import model.Server;
import model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SimulationManager implements Runnable {
    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int numberOfServers;
    public int numberOfClients;
    public int peakHour;
    public int maxPeakHour = -1;
    public int avgWaitingTime;
    public int avgServiceTime;
    public SelectionPolicy selectionPolicy; //entity responsible with queue management and client distribution
    private final Scheduler scheduler;
    private final SimulationFrame frame;  //frame for displaying simulation
    private final List<Task> generatedTasks = new ArrayList<>(); //pool of tasks (client shopping in the store)

    FileWriter file;
    {
        try{
            file = new FileWriter("test_file.txt");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    // initialize the scheduler
    // - create and start numberOfServices threads
    // - initialize selection strategy => createStrategy
    // generate numberOfClients clients using generateNRandomTasks()
    // and store them to generatedTasks()
    public SimulationManager(int timeLimit, int minArrivalTime, int maxArrivalTime, int minProcessingTime, int maxProcessingTime, int numberOfServers, int numberOfClients, SelectionPolicy selectionPolicy, SimulationFrame simulationFrame) {
        this.timeLimit = timeLimit;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.selectionPolicy = selectionPolicy;
        this.frame = simulationFrame;
        generateNRandomTasks();
        this.scheduler = new Scheduler(numberOfServers, generatedTasks.size(), selectionPolicy);
    }

    // generate N random tasks
    // - random processing time
    // minProcessingTime < processingTime < maxProcessingTime
    // - random arrivalTime
    // sort list with respect to arrivalTime
    public void generateNRandomTasks(){

        for(int i = 0; i < this.numberOfClients; i++){
            Task t = new Task(i, new Random().nextInt(minArrivalTime, maxArrivalTime), new Random().nextInt(minProcessingTime, maxProcessingTime));
            avgServiceTime += t.getServiceTime();
            this.generatedTasks.add(t);
        }
        generatedTasks.sort(Comparator.comparingInt(Task::getArrivalTime));
    }

    // iterate generatedTasks list and pick tasks that have the
    // arrivalTime equal with the currentTime
    // - send task to queue by calling the dispatchTask method from Scheduler
    // - delete client from list
    // update UI frame
    // wait an interval of 1 second
    @Override
    public void run(){
        int currentTime = 0;
        while(currentTime < timeLimit){

            List<Task> tasks = new ArrayList<>();
            for(Task t : generatedTasks){
                if(t.getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(t);
                    tasks.add(t);
                }
            }
            generatedTasks.removeAll(tasks);
            try {
                Thread.sleep(500);
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                file.write(print(currentTime));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            updateGUI(currentTime + 1);
            currentTime++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        frame.peakHourLabel.setText(String.valueOf(peakHour));
        frame.avgServiceTimeLabel.setText(String.format("%.2f",(float) avgServiceTime/numberOfClients));
        frame.avgWaitingTimeLabel.setText(String.format("%.2f",(float) avgWaitingTime/(numberOfClients - generatedTasks.size())));
        try {
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateGUI(int currentTime){
        // calculate avgWaitingTime
        for(Server s : scheduler.getServers()){
            avgWaitingTime += s.getNrOfTasks();
        }

        // calculate peak hour
        int maxim = 0;
        for(Server s : scheduler.getServers()){
            maxim += maxim + s.getNrOfTasks();
        }
        if(maxim > maxPeakHour){
            maxPeakHour = maxim;
            peakHour = currentTime;
        }

        // Update the waiting queue label
        frame.increaseTimeLabel.setText(Integer.toString(currentTime));
        StringBuilder waitingQueue = new StringBuilder("Clients: ");
        for(Task t : generatedTasks){
            waitingQueue.append(t.toString());
            if (generatedTasks.indexOf(t) != generatedTasks.size() - 1) {
                waitingQueue.append(", ");  // Append comma and space if t is not the last element
            }
        }
        if(generatedTasks.isEmpty()) {
            waitingQueue.append("EMPTY");
        }
        frame.waitingQueueLabel.setText(String.valueOf(waitingQueue));

        // update the server label
        StringBuilder servers = new StringBuilder();
        int i = 1;
        for(Server s : scheduler.getServers()){
            int indexOfTask = 0;
            servers.append("Queue ").append(i).append(": ");
            for(Task t : s.getTasks()){
                servers.append(t.toString());
                indexOfTask++;
                if(indexOfTask != s.getNrOfTasks()){
                    servers.append(", ");  // Append comma and space if t is not the last element
                }
            }
            if(s.getTasks().isEmpty()){
                servers.append("CLOSED");
            }
            servers.append("\n");
            i++;
        }
        frame.serversLabel.setText(String.valueOf(servers));
    }
    public String print(int time){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Time ").append(time+1).append("\n").append("Clients :");
        for(Task t : generatedTasks){
            stringBuilder.append(t.toString()).append(" ");
        }
        int i = 0;
        for(Server s : scheduler.getServers()){
            stringBuilder.append("\nQueue ").append(i+1).append(": ");
            for(Task t : s.getTasks()){
                stringBuilder.append(t.toString()).append(" ");
            }
            i++;
        }
        stringBuilder.append("\n\n");
        return stringBuilder.toString();
    }
}
