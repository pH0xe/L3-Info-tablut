package structure;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private final List<Observer> observers;

    public Observable() {
        observers = new ArrayList<>();
    }

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void update() {
        for (Observer observer : observers) {
            observer.update();
        }

    }
}
