package com.tj.person;

import com.tj.shop.IShop;

public interface ICustomer {
	public void setServerTime(long time);
	public long getServerTime();
	public void startServer(IWaiter waiter);
	public void endServer(IWaiter waiter);
	public void enterShop(IShop shop);
	public void leaveShop(IShop shop);
}
