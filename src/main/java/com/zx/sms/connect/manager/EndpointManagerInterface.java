package com.zx.sms.connect.manager;

import io.netty.channel.ChannelFuture;

import java.util.List;
import java.util.Set;

public interface EndpointManagerInterface{
	void openEndpoint(EndpointEntity entity) ;
	ChannelFuture openEndpointNew(EndpointEntity entity) ;
	void close(EndpointEntity entity);
	void openAll() throws Exception;
	void addEndpointEntity(EndpointEntity entity);
	void remove(String id);
	Set<EndpointEntity> allEndPointEntity();
	EndpointConnector getEndpointConnector(EndpointEntity entity);
	EndpointConnector getEndpointConnector(String entity);
	EndpointEntity getEndpointEntity(String id);
	void addAllEndpointEntity(List<EndpointEntity> entities);
	void close();
	
	void startConnectionCheckTask(); //启动自动重连任务
	void stopConnectionCheckTask();  //关闭自动重连任务
}
