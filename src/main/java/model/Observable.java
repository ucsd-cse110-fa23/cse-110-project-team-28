package model;

public interface Observable {
    public void setObserver(Observer observer);

    public void notifyObservers();
}
