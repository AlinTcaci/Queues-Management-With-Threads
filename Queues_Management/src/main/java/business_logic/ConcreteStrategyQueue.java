package business_logic;

import model.Server;
import model.Task;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task t) {
        int minim = Integer.MAX_VALUE;
        for (Server s : servers) {
            if (s.getNrOfTasks() < minim) {
                minim = s.getNrOfTasks();
            }
        }
        for (Server s : servers) {
            if (minim == s.getNrOfTasks()) {
                s.addTask(t);
                break;
            }
        }
    }
}
