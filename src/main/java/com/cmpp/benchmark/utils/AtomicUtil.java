package com.cmpp.benchmark.utils;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicUtil {

	public static AtomicLong sendCount = new AtomicLong();
	public static AtomicLong reponseCount = new AtomicLong();
	public static AtomicLong sucCount = new AtomicLong();
	public static AtomicLong failCount = new AtomicLong();
	public static AtomicLong reportCount = new AtomicLong();
	
	
	public static void clear() {
		sendCount.set(0);
		reponseCount.set(0);
		sucCount.set(0);
		failCount.set(0);
		reportCount.set(0);
	}
}
