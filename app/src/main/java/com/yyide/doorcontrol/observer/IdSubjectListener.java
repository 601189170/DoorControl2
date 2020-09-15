package com.yyide.doorcontrol.observer;


public interface IdSubjectListener {

    void add(IdListener listener);

    void notifyObserver(String id);

    void remove(IdListener idListener);
}
