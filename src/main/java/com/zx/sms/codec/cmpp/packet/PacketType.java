package com.zx.sms.codec.cmpp.packet;

import io.netty.handler.codec.MessageToMessageCodec;


/**
 *
 * @author huzorro(huzorro@gmail.com)
 */
public interface PacketType {    
    long getCommandId();
    PacketStructure[] getPacketStructures();
    long getAllCommandId();
    MessageToMessageCodec getCodec();
}
