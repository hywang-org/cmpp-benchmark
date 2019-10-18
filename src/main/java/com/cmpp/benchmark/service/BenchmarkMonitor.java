package com.cmpp.benchmark.service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 作者： chengli
 * 日期： 2019/10/18 9:51
 */
public class BenchmarkMonitor {

    private static final AtomicInteger connTotalCount = new AtomicInteger(0);
    private static AtomicInteger connSuccessCount = new AtomicInteger(0);
    private static AtomicInteger sendMsgTotalCount = new AtomicInteger(0);
    private static AtomicInteger sendMsgSuccessCount = new AtomicInteger(0);
    private static AtomicInteger receiveDeliverMsgCount = new AtomicInteger(0);
    private static AtomicInteger receiveSubmitRespCount = new AtomicInteger(0);

    static {
        Timer monitor = new Timer();
        monitor.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(String.format("发起总数：%s,连接成功:%s,发送消息总数：%s，发送成功：%s,submitRespCount:%s,deliverMsgCount:%s",
                        connTotalCount, connSuccessCount, sendMsgTotalCount, sendMsgSuccessCount, receiveSubmitRespCount, receiveDeliverMsgCount));
            }
        }, 0, 1000);
    }


    public static void incrConnTotalCount() {
        connTotalCount.incrementAndGet();
    }

    public static void incrConnSuccessCount() {
        connSuccessCount.incrementAndGet();
    }

    public static void incrSendMsgTotalCount() {
        sendMsgTotalCount.incrementAndGet();
    }

    public static void incrSendMsgSuccessCount() {
        sendMsgSuccessCount.incrementAndGet();
    }

    public static void incrReceiveDeliverMsgCount() {
        receiveDeliverMsgCount.incrementAndGet();
    }

    public static void incrReceiveSubmitRespCount() {
        receiveSubmitRespCount.incrementAndGet();
    }
}
