package com.zx.sms.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zx.sms.common.GlobalConstance;
import com.zx.sms.connect.manager.EndpointEntity;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 
 *
 */

public class MessageLogHandler extends ChannelDuplexHandler {
	private final Logger logger;
	private EndpointEntity entity;

	public MessageLogHandler(EndpointEntity entity) {
		this.entity = entity;
		if(entity!=null)
			logger = LoggerFactory.getLogger(String.format(GlobalConstance.loggerNamePrefix, entity.getId()));
		else
			logger = LoggerFactory.getLogger(MessageLogHandler.class);
	}
	public void handlerAdded(ChannelHandlerContext ctx) {
		logger.warn("handlerAdded . {}", entity);
	}
    @Override
	public void channelInactive(ChannelHandlerContext ctx) {
    	logger.warn("Connection close . {}", entity);
		ctx.fireChannelInactive();
	}
    
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		logger.debug("Receive:{}", msg);
		ctx.fireChannelRead(msg);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
		final Object finalmsg = msg;
		ctx.write(msg, promise);
		promise.addListener(new GenericFutureListener() {
			@Override
			public void operationComplete(Future future) {
				// 如果发送消息失败，记录失败日志
				if (!future.isSuccess()) {
					logger.error("ErrSend:{}",future.cause());
//					logger.error("ErrSend:{},cause by {}", finalmsg , future.cause().getMessage());
				}else{
					logger.debug("Send:{}", finalmsg);
				}
			}
		});
	}

}