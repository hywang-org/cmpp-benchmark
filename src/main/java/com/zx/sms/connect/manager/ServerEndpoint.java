package com.zx.sms.connect.manager;


/**
 *@author Lihuanghe(18852780@qq.com)
 */
public interface ServerEndpoint {
	void addchild(EndpointEntity entity);
	void removechild(EndpointEntity entity);
	EndpointEntity getChild(String userName);
}
