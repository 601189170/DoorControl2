package com.yyide.doorcontrol.mina;


import org.apache.mina.core.session.IoSession;


public class SessionManager {

    private static SessionManager mInstance = null;
    private IoSession mSession;

    public static SessionManager getInstance() {
        if (mInstance == null) {
            synchronized (SessionManager.class) {
                if (mInstance == null) {
                    mInstance = new SessionManager();
                }
            }
        }
        return mInstance;
    }

    private SessionManager() {
    }

    public void setSeesion(IoSession session) {
        this.mSession = session;
    }

    public void writeToServer(Object msg) {
        if (mSession != null && mSession.isConnected()) {
            try {
                mSession.write(msg);
            } catch (Exception e) {
            }
        }
    }

    public void closeSession() {
        if (mSession != null) {
            mSession.closeOnFlush();
        }
    }

    public void removeSession() {
        this.mSession = null;
    }
}