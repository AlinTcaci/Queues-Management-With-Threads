package business_logic;

import model.Server;
import model.Task;

import java.util.List;

public class ConcreteStrategyTime implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task t) {
        int minim = Integer.MAX_VALUE;
        for (Server s : servers) {
            if (s.getWaitingPeriod().get() < minim) {
                minim = s.getWaitingPeriod().get();
            }
        }
        for (Server s : servers) {
            if (s.getWaitingPeriod().get() == minim) {
                s.addTask(t);
                break;
            }
        }
    }
}
