package com.yyide.doorcontrol.mina;

import android.app.Activity;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.brocast.MinaMsg;
import com.yyide.doorcontrol.network.GetData;
import com.yyide.doorcontrol.utils.L;


import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;


/**
 * Created by Hao on 2017/9/6.
 */

public class MinaThread extends Thread {

    private MinaThread thread;

    private IoSession session = null;

    private IoConnector connector = null;

    private IoListener ioListener = null;

    private boolean isExit;

    public MinaThread(final Activity context) {
        thread = this;
        connector = new NioSocketConnector();
        // 设置链接超时时间
        connector.setConnectTimeoutMillis(10000);
        // 添加过滤器
        connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
        //设置空闲时间，发心跳
        connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        connector.setHandler(new MinaClientHandler(context));

        //设置默认连接远程服务器的IP地址和端口
        String port = SPUtils.getInstance().getString(BaseConstant.NUMBER, GetData.URL_IP);
        if (port.contains(":")) {
            String[] ip = port.split(":");
            L.d("==>"+ip[0]);
            connector.setDefaultRemoteAddress(new InetSocketAddress(ip[0], GetData.PORT));
        } else
        connector.setDefaultRemoteAddress(new InetSocketAddress(SPUtils.getInstance().getString(BaseConstant.NUMBER,GetData.URL_IP), GetData.PORT));
        // 监听客户端是否断线
        ioListener = new IoListener() {
            @Override
            public void sessionDestroyed(IoSession arg0) throws Exception {
                // TODO Auto-generated method stub
                super.sessionDestroyed(arg0);

                try {
                    connector.dispose();
                } catch (Exception e) {

                }
                thread = new MinaThread(context);
                thread.start();
            }
        };
        connector.addListener(ioListener);
    }

    @Override
    public void run() {
        do {
            try {
                ConnectFuture connect_future = connector.connect();
                connect_future.awaitUninterruptibly();// 等待连接创建完成
                session = connect_future.getSession();// 获得session
                //判断是否连接服务器成功
                if (session != null && session.isConnected()) {
                    WriteFuture write_future = session.write(MinaMsg.MinaMsg(BaseConstant.MINA_REG));
                    write_future.awaitUninterruptibly();
                    if (write_future.isWritten())
                        SessionManager.getInstance().setSeesion(session);
                    else {
                        session.closeNow().awaitUninterruptibly();
                        session = null;
                    }
                } else
                    session = null;
            } catch (Exception e) {
                Log.e("TAG", "Exception: "+ e.toString());
                if (session != null && session.isConnected())
                    session.closeNow().awaitUninterruptibly();
                session = null;
            }
            if (session == null) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
            }
        } while (session == null);

    }

    public MinaThread getCurrentThread() {
        return thread;
    }

    /*推出mina*/
    public void exitMina() {
        isExit = true;
        connector.removeListener(ioListener);
        SessionManager.getInstance().writeToServer(MinaMsg.MinaMsg(BaseConstant.MINA_EXIT));
        SessionManager.getInstance().closeSession();
        SessionManager.getInstance().removeSession();
    }
}
