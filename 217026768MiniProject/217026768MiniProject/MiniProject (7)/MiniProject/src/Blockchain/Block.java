package Blockchain;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Date;

public class Block implements Serializable{
	
	public String hash;
	private String blockData;
	private long timeStamp;
	public String hashPrevious;
	private int nonce;
	
	public Block(String hashPrevious, String data)
	{
		this.blockData = data;
		this.hashPrevious = hashPrevious;
		this.hash =  calculateHash();
		this.timeStamp = new Date().getTime();
	}
	
	//Method to calculate hash using the toSha256 method
	public String calculateHash()
	{
		 String calculatedhash = Tosha256(hashPrevious+ Long.toString(timeStamp)+ Integer.toString(nonce)+ blockData);
         return calculatedhash;
		
	}
	
	//Method to encrypt a string message using sha-256 algorithm
	public static String Tosha256(String input)
    {
        try {
            MessageDigest sha= MessageDigest.getInstance("SHA-256");
            int x = 0;
  
            byte[] hash = sha.digest(input.getBytes("UTF-8"));
 
            StringBuffer hashBuffer = new StringBuffer();
  
            while (x < hash.length) {
                String hex = Integer.toHexString(0xff & hash[x]);
                if (hex.length() == 1)
                {
                	hashBuffer.append('0');
                }else
                {
                	hashBuffer.append(hex);
                }
             
                x++;
            }
  
            return hashBuffer.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	//function used to mine a block
	public String mine(int strength)
	{
	
		String goal = new String(new char[strength]).replace('\0', '0'); 
		while(!hash.substring( 0, strength).equals(goal)) {
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("Block Successfully Mined: " + hash);
		return hash;
	}
	
	//toString method show the contents of the block
	public String toString()
	{
		String output = "hash :" + hash + "data : "+ blockData + "timestamp:" + timeStamp + "hashprev: " + hashPrevious+"\n";
		return output;
	}

	public String getHash() {
		return hash;
	}

	public String getBlockData() {
		return blockData;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public String getHashPrevious() {
		return hashPrevious;
	}

	public int getNonce() {
		return nonce;
	}
	
	
	
	
}
