package com.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class BluetoothController {
	private static StreamConnection conn = null;
	private static PrintStream out = null;
	private static InputStreamReader in = null;
	
	public static void connect(String url) {		
		try {
			conn = (StreamConnection) 
			          Connector.open(url + "");
			out = new PrintStream(conn.openOutputStream());
			in = new InputStreamReader(conn.openInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendMessage(String message) {
		out.println(message);
	}
	
	public static String receiveMessage() {
		String message = "";
		try {
			while(in.ready()) {
				message = message + in.read(); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	public static void closeConnection() {
		try {
			conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
