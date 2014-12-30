package com.xcy.net;

public interface ConnListener {
	public void OnFind(String ips);
	public void OnConnected();
	public void OnDisconnected();
	public void AddClient(String client);
	public void RemoveClient(String client);
	
	public void HandlerKey(int keycode,int action);
	public void HandlerTouch(int x,int y,int action);
}
