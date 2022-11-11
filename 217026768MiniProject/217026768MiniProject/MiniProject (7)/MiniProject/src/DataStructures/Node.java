package DataStructures;

import java.net.InetAddress;

public class Node implements Comparable<Node>{
	private int portNumber;
	private InetAddress address;
	
	

	public int getPortNumber() {
		return portNumber;
	}



	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}



	public InetAddress getAddress() {
		return address;
	}



	public void setAddress(InetAddress address) {
		this.address = address;
	}



	public Node(int portNumber, InetAddress address) {
		this.portNumber = portNumber;
		this.address = address;
	}



	@Override
	public int compareTo(Node o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String toString()
	{
		String str = "Port number : "+ portNumber +"  "+"Address: "+  address;
		return str;
	}
}