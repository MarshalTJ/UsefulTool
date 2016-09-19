package com.tj.impl;

//工程主入口
public class Test {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CustomerFactoryImpl factoryImpl = new CustomerFactoryImpl();
		new Thread(factoryImpl).start();
		
//		long spTime = 60 * 1000;
//		try {
//			Thread.sleep(spTime);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		factoryImpl.stop();
	}

}
