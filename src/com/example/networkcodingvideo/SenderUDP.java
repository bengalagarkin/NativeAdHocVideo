package com.example.networkcodingvideo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

//import com.mainactivity.MainActivity;

import android.util.Log;

public class SenderUDP {

	public native void SendUdpJNI( String ip, int port, String j_message, int is_broadcast);		
    static {
        System.loadLibrary("adhoc-jni");
    }
	
	
	//TODO: ORDER!!! 
	private String ip;
	private String msg;
	private byte[] byte_array;
	
	private DatagramSocket datagramSocket;
	private int receiverPort = 8888;
	private boolean use_ndk = true;
	
	final int duration = 2;

	public SenderUDP(String new_ip, String new_msg)
	{
		this.ip = new_ip;
		this.msg = new_msg;
	}
	public void ChangeTargetIp(String new_ip)
	{
		this.ip = new_ip;
	}
	
	public SenderUDP(String new_ip, byte[] rec_bytes)
	{
		this.ip = new_ip;
		this.msg = new String(rec_bytes);
		//this.byte_array = new byte[rec_bytes.length];
		//System.arraycopy(rec_bytes, 0, byte_array,0, rec_bytes.length);
	}
	
	public String sendMsg() throws IOException // TODO: better understand the try throw mechanism
	{
		// TODO: check data length
		String str = "";
		if (use_ndk) {
			
			try {			
				Log.i("SenderUDP.java","NDK: sending to IPAddress "+ip);
				SendUdpJNI(ip,receiverPort,msg,1); // Always broadcast
			} catch (StackOverflowError e) {
				Log.i("SenderUDP.java","CAUGHT 00");
				e.printStackTrace(); 
			}
		} else {
			InetAddress IPAddress = InetAddress.getByName(this.ip);
			DatagramPacket sendPacket;
			Log.i("SenderUDP.java","JAVA: sending to IPAddress "+IPAddress);
			datagramSocket = new DatagramSocket();
			
			datagramSocket.setBroadcast(true); // TODO: if .255 should be true
			
			sendPacket = new DatagramPacket(this.msg.getBytes(), this.msg.getBytes().length, IPAddress, receiverPort); // TODO: change 8888 to an integer value
			
			datagramSocket.send(sendPacket);
			datagramSocket.close();
		}
		
			
		return msg.getBytes().toString();
	}
	
	public void setTargetIp(String new_ip) {
		this.ip = new_ip;
	}
	public void setMessage(String new_msg) {
		this.msg = new_msg;
	}
	
}
