package com.cmpp.benchmark.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Component("cmppProperties")
public class CmppProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmppProperties.class);

    @Value("${cmpp.host:127.0.0.1}")
    private String cmppHost;
    @Value("${cmpp.port:7890}")
    private int cmppPort;
    @Value("${cmpp.username}")
    private String cmppUsername;
    @Value("${cmpp.password}")
    private String cmppPassword;
    @Value("${cmpp.serviceId}")
    private String cmppServiceId;
    @Value("${cmpp.spId}")
    private String spId;
    @Value("${cmpp.connCount}")
    private int connCount;
    @Value("${cmpp.speed}")
    private int speed;
    @Value("${cmpp.mobiles}")
    private String mobiles;
    @Value("${cmpp.msg}")
    private String msg;

    private static Map<String, Object> properties = new HashMap<>();

    @PostConstruct
    private void save() {
        properties.put("cmppHost", cmppHost);
        properties.put("cmppPort", cmppPort);
        properties.put("cmppUsername", cmppUsername);
        properties.put("cmppPassword", cmppPassword);
        properties.put("cmppServiceId", cmppServiceId);
        properties.put("spId", spId);
        properties.put("connCount", connCount);
        properties.put("speed", speed);
        properties.put("mobiles", mobiles);
        properties.put("msg", msg);
        print();
    }

    public static String[] getMobiles() {
        String mobileStr = (String) properties.get("mobiles");
        return mobileStr.split(",");
    }

    public static String getMsg() {
        return (String) properties.get("msg");
    }

    public static int getSpeed() {
        return (int) properties.get("speed");
    }

    public static Integer getConnCount() {
        return (int) properties.get("connCount");
    }

    public static String getSpId() {
        return (String) properties.get("spId");
    }

    public static String getCmppServiceId() {
        return (String) properties.get("cmppServiceId");
    }

    public static String getCmppPassword() {
        return (String) properties.get("cmppPassword");
    }

    public static String getCmppHost() {
        return (String) properties.get("cmppHost");
    }

    public static int getCmppPort() {
        return (int) properties.get("cmppPort");
    }

    public static String getCmppUsername() {
        return (String) properties.get("cmppUsername");
    }

    private void print() {
        Set<Entry<String, Object>> datas = properties.entrySet();
        for (Entry<String, Object> data : datas) {
            LOGGER.info(data.getKey() + "=" + data.getValue());
        }
    }
}
