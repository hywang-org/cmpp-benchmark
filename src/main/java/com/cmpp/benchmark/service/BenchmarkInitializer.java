package com.cmpp.benchmark.service;

import com.cmpp.benchmark.config.CmppProperties;
import com.cmpp.benchmark.handler.MessageReceiveHandler;
import com.zx.sms.codec.cmpp.msg.CmppSubmitRequestMessage;
import com.zx.sms.common.util.MsgId;
import com.zx.sms.connect.manager.EndpointConnector;
import com.zx.sms.connect.manager.EndpointEntity;
import com.zx.sms.connect.manager.EndpointManager;
import com.zx.sms.connect.manager.cmpp.CMPPClientEndpointEntity;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@DependsOn("cmppProperties")
public class BenchmarkInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkInitializer.class);

    private final EndpointManager endpointManager = EndpointManager.INS;

    private static final ThreadPoolExecutor BENCHMARK_EXECUTOR
            = new ThreadPoolExecutor(100, 1000, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

    private static final int sendMsgCount = 500;
    private static final int connCount = 1;


    @Override
    public void run(String... args) {
        try {
            startBenchmark(connCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startBenchmark(int connCount) throws InterruptedException {
        for (int i = 0; i < connCount; i++) {
            BENCHMARK_EXECUTOR.submit(() -> {
                ChannelFuture channelFuture = connCmppServer();
                BenchmarkMonitor.incrConnTotalCount();
                if (channelFuture != null) {
                    channelFuture.addListener(future -> {
                        if (future.isSuccess()) {
                            BenchmarkMonitor.incrConnSuccessCount();
                            BENCHMARK_EXECUTOR.submit(() -> {
                                try {
                                    benchSendMsg();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });

                        } else {
                            LOGGER.info("连接失败");
                        }
                    });
                }
            });
            TimeUnit.MILLISECONDS.sleep(5);
        }
    }

    private void benchSendMsg() throws InterruptedException {
        int i = 0;
        while (i++ < sendMsgCount) {
            sendMsg();
            TimeUnit.MILLISECONDS.sleep(200);
        }
    }

    private void sendMsg() {
        EndpointConnector<?> endpointConnector = endpointManager
                .getEndpointConnector(CmppProperties.getCmppUsername());
        String[] mobiles = CmppProperties.getMobiles();
        for (String mobile : mobiles) {
            CmppSubmitRequestMessage request = buildMsg(mobile);
            BenchmarkMonitor.incrSendMsgTotalCount();
            ChannelFuture asynWriteFuture = endpointConnector.asynwrite(request);
            asynWriteFuture.addListener(sendFuture -> {
                if (sendFuture.isSuccess()) {
                    //System.out.println("短信消息发送成功");
                    BenchmarkMonitor.incrSendMsgSuccessCount();
                } else {
                    System.out.println("短信消息发送失败");
                }
            });
        }
    }

    private CmppSubmitRequestMessage buildMsg(String mobile) {
        CmppSubmitRequestMessage msg = new CmppSubmitRequestMessage();
        msg.setLinkID("0000");
        msg.setDestterminalId(mobile);
        msg.setMsgContent(CmppProperties.getMsg() + System.nanoTime());
        msg.setRegisteredDelivery((short) 1);
        msg.setServiceId(CmppProperties.getCmppServiceId());
        msg.setMsgid(new MsgId());
        msg.setSrcId(CmppProperties.getSpId());
        msg.setMsgsrc(CmppProperties.getCmppUsername());
        return msg;
    }

    private ChannelFuture connCmppServer() {
        CMPPClientEndpointEntity client = new CMPPClientEndpointEntity();
        //用户名
        client.setId(CmppProperties.getCmppUsername());
        client.setHost(CmppProperties.getCmppHost());
        client.setPort(CmppProperties.getCmppPort());
        //用户名
        client.setUserName(CmppProperties.getCmppUsername());
        client.setPassword(CmppProperties.getCmppPassword());
        //业务代码
        client.setServiceId(CmppProperties.getCmppServiceId());
        client.setMaxChannels(CmppProperties.getConnCount().shortValue());
        client.setVersion((short) 0x20);
        client.setWriteLimit(CmppProperties.getSpeed());
        client.setGroupName("benchmark");
        client.setChartset(Charset.forName("utf-8"));
        client.setRetryWaitTimeSec((short) 30);
        client.setMaxRetryCnt((short) 0);
        client.setUseSSL(false);
        client.setReSendFailMsg(false);
        client.setSupportLongmsg(EndpointEntity.SupportLongMessage.BOTH);
        client.setBusinessHandlerSet(Collections.singletonList(new MessageReceiveHandler()));
        endpointManager.addEndpointEntity(client);
        return endpointManager.openEndpointNew(client);
    }
}
