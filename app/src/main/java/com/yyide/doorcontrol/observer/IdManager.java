package com.yyide.doorcontrol.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */

public class IdManager implements IdSubjectListener {

    private static IdManager idManager;

    //观察者接口集合
    private List<IdListener> list = new ArrayList<>();

    /**
     * 单例
     */
    public static IdManager getInstance() {
        if (null == idManager) {
            synchronized (IdManager.class) {
                if (null == idManager) {
                    idManager = new IdManager();
                }
            }
        }
        return idManager;
    }

    /**
     * 加入监听队列
     */
    @Override
    public void add(IdListener idListener) {
        list.add(idListener);
    }

    /**
     * 通知观察者刷新数据
     */
    @Override
    public void notifyObserver(String id) {
        for (IdListener observerListener : list) {
            observerListener.observerUpData(id);
        }
    }

    /**
     * 监听队列中移除
     */
    @Override
    public void remove(IdListener idListener) {
        if (list.contains(idListener)) {
            list.remove(idListener);
        }
    }
}
