package com.cmpp.benchmark.handler;

import cn.hutool.core.lang.Console;
import com.cmpp.benchmark.service.BenchmarkMonitor;
import com.zx.sms.codec.cmpp.msg.CmppDeliverRequestMessage;
import com.zx.sms.codec.cmpp.msg.CmppDeliverResponseMessage;
import com.zx.sms.codec.cmpp.msg.CmppSubmitRequestMessage;
import com.zx.sms.codec.cmpp.msg.CmppSubmitResponseMessage;
import com.zx.sms.handler.api.AbstractBusinessHandler;
import com.zx.sms.session.cmpp.SessionState;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

@Sharable
public class MessageReceiveHandler extends AbstractBusinessHandler {

    @Override
    public String name() {
        return "MessageReceiveHandler-smsBiz";
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt == SessionState.Connect) {
            //System.out.println("连接服务端成功");
        }
        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof CmppDeliverRequestMessage) {
            CmppDeliverRequestMessage e = (CmppDeliverRequestMessage) msg;
            //Console.log("222:", new String(e.getMsgContentBytes(), "iso-10646-ucs-2"));
            CmppDeliverResponseMessage responseMessage = new CmppDeliverResponseMessage(e.getHeader().getSequenceId());
            BenchmarkMonitor.incrReceiveDeliverMsgCount();
            responseMessage.setResult(0);
            ctx.channel().writeAndFlush(responseMessage);

        } else if (msg instanceof CmppDeliverResponseMessage) {
            // CmppDeliverResponseMessage e = (CmppDeliverResponseMessage) msg;

        } else if (msg instanceof CmppSubmitRequestMessage) {
            CmppSubmitRequestMessage e = (CmppSubmitRequestMessage) msg;
            CmppSubmitResponseMessage resp = new CmppSubmitResponseMessage(e.getHeader().getSequenceId());
            // resp.setResult(RandomUtils.nextInt()%1000 <10 ? 8 : 0);
            resp.setResult(0);
            ctx.channel().writeAndFlush(resp);
        } else if (msg instanceof CmppSubmitResponseMessage) {
            CmppSubmitResponseMessage e = (CmppSubmitResponseMessage) msg;
            BenchmarkMonitor.incrReceiveSubmitRespCount();
            long result = e.getResult();
            if (result == 0) {
                //	MainWindow.label_sucCount.setText(StrUtil.format("提交成功:{}", AtomicUtil.sucCount.incrementAndGet()));
            } else {
                //	MainWindow.label_reponseCount.setText(StrUtil.format("提交失败:{}", AtomicUtil.failCount.incrementAndGet()));
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    public MessageReceiveHandler clone() throws CloneNotSupportedException {
        return (MessageReceiveHandler) super.clone();
    }

}
