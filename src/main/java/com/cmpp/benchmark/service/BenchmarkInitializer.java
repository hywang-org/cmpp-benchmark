package com.cmpp.benchmark.service;

import cn.hutool.core.thread.ThreadUtil;
import com.cmpp.benchmark.config.CmppProperties;
import com.cmpp.benchmark.handler.MessageReceiveHandler;
import com.zx.sms.codec.cmpp.msg.CmppSubmitRequestMessage;
import com.zx.sms.common.util.MsgId;
import com.zx.sms.connect.manager.EndpointConnector;
import com.zx.sms.connect.manager.EndpointEntity;
import com.zx.sms.connect.manager.EndpointManager;
import com.zx.sms.connect.manager.cmpp.CMPPClientEndpointEntity;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component
@DependsOn("cmppProperties")
public class BenchmarkInitializer implements CommandLineRunner {

    private final EndpointManager endpointManager = EndpointManager.INS;

    @Override
    public void run(String... args) throws InterruptedException {
        connCmppServer();
        TimeUnit.SECONDS.sleep(5);
        EndpointConnector<?> endpointConnector = endpointManager.getEndpointConnector(CmppProperties.getCmppUsername());
        String[] mobiles = CmppProperties.getMobiles();
        for (String mobile : mobiles) {
            CmppSubmitRequestMessage request = buildMsg(mobile);
            endpointConnector.asynwrite(request).addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("短信消息发送成功");
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
        msg.setMsgContent(CmppProperties.getMsg());
        msg.setRegisteredDelivery((short) 1);
        msg.setServiceId("hello");
        msg.setMsgid(new MsgId());
        msg.setServiceId("");
        msg.setSrcId("106909009002");
        msg.setMsgsrc("109002");
        return msg;
    }

    private void connCmppServer() {
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
        ThreadUtil.sleep(1000);
        for (int i = 0; i < client.getMaxChannels(); i++)
            endpointManager.openEndpoint(client);
    }
}
