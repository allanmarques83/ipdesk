package client.remote;

import java.io.*;
import java.net.*;

import client.resources.Utils;

public class Client
{
	private String MAC_ADDRESS, IP_ADDRESS, OS_SYSTEM;

    public Client() {

        this.setOsSystem(Utils.getOsSystem());
        
        this.setIpAddress(OS_SYSTEM.equals("linux") ? Utils.getLinuxIpAddress() : 
            Utils.getWindowsIpAddress());
        
        this.setClientMac(Utils.getMacAddress());
    }

	private void setOsSystem(String os) {
		OS_SYSTEM = os;
	}

	private void setIpAddress(String ipAddress) {
		IP_ADDRESS = ipAddress;
	}

	private void setClientMac(String mac) {
		MAC_ADDRESS= mac;
	}

	public String getClientMac() {
		return MAC_ADDRESS;
	}
}