package com.zx.sms.common;

import com.zx.sms.config.PropertiesUtils;
import com.zx.sms.handler.cmpp.*;
import com.zx.sms.session.cmpp.SessionState;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;
import org.marre.sms.SmsAlphabet;
import org.marre.sms.SmsDcs;
import org.marre.sms.SmsMsgClass;

import java.nio.charset.Charset;

public interface GlobalConstance {
	int MaxMsgLength = 140;
	String emptyString = "";
	byte[] emptyBytes= new byte[0];
	String[] emptyStringArray= new String[0];
  
    Charset defaultTransportCharset = Charset.forName(PropertiesUtils.getdefaultTransportCharset());
    SmsDcs defaultmsgfmt = SmsDcs.getGeneralDataCodingDcs(SmsAlphabet.ASCII, SmsMsgClass.CLASS_UNKNOWN);
    CmppActiveTestRequestMessageHandler activeTestHandler =  new CmppActiveTestRequestMessageHandler();
    CmppActiveTestResponseMessageHandler activeTestRespHandler =  new CmppActiveTestResponseMessageHandler();
    CmppTerminateRequestMessageHandler terminateHandler =  new CmppTerminateRequestMessageHandler();
    CmppTerminateResponseMessageHandler terminateRespHandler = new CmppTerminateResponseMessageHandler();

    CmppServerIdleStateHandler idleHandler = new CmppServerIdleStateHandler();
    AttributeKey<SessionState> attributeKey = AttributeKey.newInstance(SessionState.Connect.name());
    BlackHoleHandler blackhole = new BlackHoleHandler();
    String IdleCheckerHandlerName = "IdleStateHandler";
    String loggerNamePrefix = "entity.%s";
    String codecName = "codecName";
    AttributeKey<ChannelPromise> CONN_SUCCESS_PROMISE = AttributeKey.valueOf("connSuccessPromise");
}
