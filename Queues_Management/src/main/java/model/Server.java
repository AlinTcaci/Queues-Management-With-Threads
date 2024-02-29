package model;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    public Server(){
        this.waitingPeriod = new AtomicInteger(0);
        tasks = new LinkedBlockingQueue<>();
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }
    public BlockingQueue<Task> getTasks() {
        return tasks;
    }
    public int getNrOfTasks(){
        return tasks.size();
    }

    public void addTask(Task newTask){
        tasks.add(newTask);
        this.waitingPeriod.addAndGet(newTask.getServiceTime());
    }

    @Override
    public void run(){
        while(true){
            //take next task from queue
            //stop the thread for a time equal with the task's processing time
            //decrement the waitingPeriod
            if(!tasks.isEmpty()){
                try{
                    int serviceTime = tasks.peek().getServiceTime();
                    for(int i = 0; i < serviceTime; i++){
                        Thread.sleep(1000);
                        tasks.peek().setServiceTime(tasks.peek().getServiceTime() - 1);
                        if(this.waitingPeriod.get() > 0){
                            this.waitingPeriod.decrementAndGet();
                        }
                    }
                    tasks.take();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
