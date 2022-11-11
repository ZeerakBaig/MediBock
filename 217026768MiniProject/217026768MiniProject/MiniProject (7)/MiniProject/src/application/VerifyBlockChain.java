package application;
import Blockchain.Block;
import java.util.ArrayList;



public class VerifyBlockChain {
	
	//Class containing a single static method that verifies the blockchain contents
	public static boolean verifyBlockChain(ArrayList<Block> chain, int strength)
	{
		String str = new String(new char[strength]).replace('\0', '0');
		boolean check = true;
		for(int x=0 ; x < chain.size();x++)
		{
			String hashPrev = x==0 ? "0" : chain.get(x - 1).getHash();
			//comparing the hashPrev of the current block with the hash of the previous block in the blockchain
			check = chain.get(x).getHash().equals(chain.get(x).calculateHash()) && hashPrev.equals(chain.get(x).getHashPrevious())
					&& chain.get(x).getHash().substring(0,strength).equals(str);
			if(!check)
			{
				return false;
			}
			
		}
		return true;
	}

}
