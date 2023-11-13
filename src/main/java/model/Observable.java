package model;

public interface Observable {
    public void setObserver(Observer observer);

    public void removeObserver();

    public void notifyObservers();
}
