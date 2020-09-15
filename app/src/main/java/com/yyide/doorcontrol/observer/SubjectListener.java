package com.yyide.doorcontrol.observer;


public interface SubjectListener {
    void add(ObserverListener observerListener);
    void notifyObserver(String cardNo);
    void remove(ObserverListener observerListener);
}
