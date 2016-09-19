package com.tj.person;

import com.tj.shop.IShop;

public interface IWorker extends Runnable{
	public void startWork(IShop shop);
	public void stopWork();
}
