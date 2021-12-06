package com.example.alzhapp;

import java.util.Observable;

public class BroadcastObserver extends Observable {
    private static BroadcastObserver instance = new BroadcastObserver();

    public static BroadcastObserver getInstance(){
        return instance;
    }

    private BroadcastObserver(){}

    public void updateValue(Object data) {
        synchronized (this) {
            setChanged();
            notifyObservers(data);
        }
    }
}
