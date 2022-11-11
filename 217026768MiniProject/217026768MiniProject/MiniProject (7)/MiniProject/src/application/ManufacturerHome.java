package application;

import java.io.BufferedReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import Blockchain.Block;
import DataStructures.Node;
import application.Graph.Edge;
import application.Graph.Vertex;
//import application.manfMain.ServerRecvThread;
//import application.manfMain.ServerSendThread;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 
 * @author Zeerak Baig 
 * Student number : 217026768
 *
 */
public class ManufacturerHome extends StackPane implements Comparable{
	
	//Order details***********************************************
	Label drugName = new Label("Drug Name: ");
	Label dName = new Label();
	
	Label lblAmount = new Label("Quantity: ");
	Label lblQuantity = new Label();
	
	Label PharmacyName = new Label("Pharmacy name: ");
	Label pharmaName = new Label();
	//Order details***********************************************
	
	
	Label lblmanufacturerName = new Label("Manufacturer Name ");
	TextField txtmanufacturerName = new TextField();
	
	//Manufacturer ID
	Label lblmanufacturerID = new Label("Manufacturer ID ");
	TextField txtmanufacturerID = new TextField();
	
	//Label for drug
	Label lblDrug= new Label("Drug Information");
	
	//Drug information
	Label lbldrugName = new Label("Drug Name");
	Label lblDrugID = new Label("Drug ID");
	Label lblDOM = new Label("Date of Manufacturing");
	Label lblExpiryDate = new Label("Expiry Date");
	Label lblMaxTemp = new Label("Maximum Temprature");
	Label lblDelivery = new Label("Quantity");
	Label lblSign = new Label("Signature");
	TextArea console = new TextArea();
	
	TextField txtdrugName = new TextField();
	TextField txtDrugID = new TextField();
	//TextField txtDOM = new TextField();
	//TextField txtExpire = new TextField();
	TextField txtMax = new TextField();
	//TextField txtDelivery = new TextField();
	TextField txtSign = new TextField();
	Button btnDone;
	Button btnGet;
	Button btnVerify;
	Button btnViewContent;
	
	//DatePicker Expirydate = new DatePicker();
	//DatePicker dateOfManufacturing = new DatePicker();
	TextField Expirydate = new TextField();
	TextField  dateOfManufacturing = new TextField();
	
	DatePicker dispatchDate = new DatePicker();
	TextField txtQuantity = new TextField();
	String orderDetails;
	
	//DatagramSocket ds = null;
	//DatagramSocket ds2 = null;
	
	PrivateKey manfPrivateKey = null;
	PublicKey manfPublicKey = null;
	KeyPair pair = null;
	SecureRandom randomsec = null;
	KeyPairGenerator keyGenerator = null;
	
	Vertex<Node> manufacturerVertex = null;
	Graph<Node> manufacturerGraph = null;
	ArrayList<Block> manufacturerChain;
	
	int strength = 5;
	
	
	public ManufacturerHome()
	{
		manufacturerGraph = new Graph<Node>();
		InetAddress address;
		//creating a vertex of manufacturer that is going to be added to the graph 
		try {
			address = InetAddress.getByName("10.0.0.9");
			Node node = new Node(9999,address);
			manufacturerVertex = new Vertex<Node>(node);
			Vertex<Node> vertex = new Vertex<>(node);
			Edge<Node> newEdge = new Edge<Node>(0,manufacturerVertex,vertex);
			vertex.addEdge(newEdge);
			//adding the vertex to the manufacturer's graph
			manufacturerGraph.getVertices().add(vertex);
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//creating a blockChain for the manufaturer 
		manufacturerChain = new ArrayList<Block>();
		
		try {
			keyGenerator = KeyPairGenerator.getInstance("DSA", "SUN");
			randomsec =  SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGenerator.initialize(1024,randomsec);
			//setting up the public and the private key for the manufacturer
			pair = keyGenerator.generateKeyPair();
			manfPrivateKey = pair.getPrivate();
			manfPublicKey = pair.getPublic();
			
			
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchProviderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Starting the main homepage of manufacturer
		startHome();
        //button that sends data to delivery and pharmacy
		btnDone.setOnAction(e->{
			//sendds data to pharmacy and the delivery company
			senddata();
			senddeliverydata();
		});
		btnGet.setOnAction(e->{
			btnGet.setText("Connected");
			console.setText("Connected to pharmacy and delivery");
			receiveData();
		});
		btnVerify.setOnAction(e->{
			VerifyBlockChain verifyChain = new VerifyBlockChain();
			boolean verify = (!VerifyBlockChain.verifyBlockChain(manufacturerChain,strength));
			console.setText("The status is "+ verify + "\n"+ "The blockchain is verified \n" + "Block added to the blockChain");

		});
		btnViewContent.setOnAction(e->{
			String content = "";
			for(Block b : manufacturerChain)
			{
				content += b.toString() + "\n";
			}
			console.setText("The Block Chain has the following records \n"+content+ "\n"+
					"The graph Content \n" + manufacturerGraph.toString());
		});
			
		
	}
	//Gui for the manufacturer********************************
	public void startHome()
	{
		btnDone = new Button("Send block to Peers");
		btnGet = new Button("Connect to Peers");
		btnVerify = new Button("Verify BlockChain");
		btnViewContent = new Button("View Graph/blockchain Content");
		Accordion accordion = new Accordion();
		TitledPane tpCreateOrder = new TitledPane();
		TitledPane tpOrderPane = new TitledPane();
		GridPane manuPane = createDelivery();
		manuPane.setStyle("-fx-background-color:#aefbfc");
		
		GridPane order =  orderDetails();
		order.setStyle("-fx-background-color:#cdf5c9");
		
		tpCreateOrder.setText("Create an Order");
		
		tpOrderPane.setText("Check orders");
		
		tpCreateOrder.setContent(manuPane);
		tpOrderPane.setContent(order);
		
		
		//accordion.getPanes().add(tpOrderPane);
		accordion.getPanes().add(tpCreateOrder);
		
		btnGet.setPrefWidth(300);
		btnDone.setPrefWidth(300);
		btnVerify.setPrefWidth(300);
		btnViewContent.setPrefWidth(300);
		VBox layout1 = new VBox(20);   
		layout1.setPadding(new Insets(5,5,5,5));
		layout1.getChildren().addAll(accordion,btnGet,btnDone,btnVerify,btnViewContent,console);
		layout1.setAlignment(Pos.TOP_CENTER);
		getChildren().add(layout1);
	}
	
	
	//Create order gridpane
	private GridPane createDelivery()
	{
		GridPane manufacturerPane= new GridPane();
		manufacturerPane.setHgap(10);
		manufacturerPane.setVgap(10);
		
		Button btnDone = new Button("Send");
		lblDrug.setStyle("-fx-font-weight: bold");
		manufacturerPane.setVgap(4); //Add some spacing
		manufacturerPane.setPadding(new Insets(5,10,10,10)); //Add some padding
		manufacturerPane.add(lblmanufacturerName,0,0);
		manufacturerPane.add(txtmanufacturerName, 1, 0);
		
		manufacturerPane.add(lblmanufacturerID, 0, 1);
		manufacturerPane.add(txtmanufacturerID, 1, 1);
		
		//Form details*****************************************************************
		manufacturerPane.add(lblDrug, 0, 4);
		manufacturerPane.add(lbldrugName, 0, 6);
		manufacturerPane.add(lblDrugID, 0, 8);
		manufacturerPane.add(lblDOM, 0, 10);
		manufacturerPane.add(lblExpiryDate, 0, 12);
		manufacturerPane.add(lblMaxTemp, 0, 14);
		manufacturerPane.add(lblDelivery, 0, 16);
		manufacturerPane.add(lblSign, 0, 18);
		
		manufacturerPane.add(txtdrugName, 1,6);
		manufacturerPane.add(txtDrugID, 1, 8);
		manufacturerPane.add(dateOfManufacturing, 1, 10);
		manufacturerPane.add(Expirydate, 1, 12);
		manufacturerPane.add(txtMax, 1, 14);
		manufacturerPane.add(txtQuantity, 1, 16);
		manufacturerPane.add(txtSign, 1, 18);
	
		//manufacturerPane.add(btnDone, 1, 20);
		manufacturerPane.setAlignment(Pos.CENTER); 
		return manufacturerPane;
	}
	
	private GridPane orderDetails()
	{
		GridPane orderPane = new GridPane();
		orderPane.add(drugName, 0, 0);
		orderPane.add(dName, 1, 0);
		
		orderPane.add(lblAmount, 0, 1);
		orderPane.add(lblQuantity, 1, 1);
		
		orderPane.add(PharmacyName, 0,2);
		orderPane.add(pharmaName, 1, 2);
		return orderPane;
		
	}
	//Gui for the manufacturer********************************
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	//Function thats starts a send thread for sending data to the pharmacy
	private void senddata() {
		 // Server starts 2 threads, 1 send, 1 receive
		ServerSendThread send = new ServerSendThread();
		new Thread(send).start();
	}

	//Function that starts the receiving information thread for the manufacturer
	public void receiveData()
	{
		ServerRecvThread recv = new ServerRecvThread();
		new Thread(recv).start();
	}
	
	//function used to start a thread that sends data to the delivery company
	public void senddeliverydata()
	{
		ServerSendDeliveryThread deliveryThread = new ServerSendDeliveryThread();
		new Thread(deliveryThread).start();
	}
	
	//sends data to the pharmacy
	class ServerSendThread implements Runnable{
		 
		@Override
		public void run() {
			try {
				DatagramSocket ds = new DatagramSocket();
				String str = "";
				String Command = "SENDBLOCKpharmacy";
				byte[] buf = null;
				while (true) {
				    //Creating a manufacturer vertex on the graph with manufacturers port and address
					InetAddress address = InetAddress.getByName("10.0.0.9");
					Node node = new Node(9000,address);
					manufacturerVertex = new Vertex<Node>(node);
					Vertex<Node> vertex = new Vertex<>(node);
						
					Edge<Node> newEdge = new Edge<Node>(0,manufacturerVertex,vertex);
					vertex.addEdge(newEdge);
					//adding the vertex to the manufacturer's graph
					manufacturerGraph.getVertices().add(vertex);
					
					str = txtdrugName.getText()+" " +txtDrugID.getText()+ " "+dateOfManufacturing.getText()+ " "+ Expirydate.getText()+" " +txtMax.getText();
					//Creating of genesis block
					Block block1 = new Block(manufacturerChain.get(manufacturerChain.size()-1).getHash(),str);
					//mining the block
					manufacturerChain.get(manufacturerChain.size()-1).mine(strength);
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					ObjectOutputStream os = new ObjectOutputStream(outputStream);
					os.writeObject(block1);
					byte[] data = outputStream.toByteArray();
					DatagramPacket sendPacket = new DatagramPacket(data, data.length,new InetSocketAddress(node.getAddress(), node.getPortNumber()));
					ds.send(sendPacket);
					buf = null;
					ds.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	class ServerSendDeliveryThread implements Runnable{
		 
		@Override
		public void run() {
			try {
				DatagramSocket ds = new DatagramSocket();
				String str = "";
				String Command = "SENDBLOCKdelivery";
				byte[] buf = null;
				while (true) {

					InetAddress address = InetAddress.getByName("10.0.0.9");
					Node node = new Node(7900,address);
					manufacturerVertex = new Vertex<Node>(node);
					Vertex<Node> vertex = new Vertex<>(node);
						
					Edge<Node> newEdge = new Edge<Node>(0,manufacturerVertex,vertex);
					vertex.addEdge(newEdge);
					//adding the vertex to the manufacturer's graph
					manufacturerGraph.getVertices().add(vertex);
					
					str = txtdrugName.getText()+" " +txtDrugID.getText()+ " "+dateOfManufacturing.getText()+ " "+ Expirydate.getText()+" " +txtMax.getText();
					//creating a new block 
					Block block1 = new Block(manufacturerChain.get(manufacturerChain.size()-1).getHash(), str);
					//mining the block before sending to the delivery company
					manufacturerChain.get(manufacturerChain.size()-1).mine(strength);
					
					//sending the block over to the peers
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					ObjectOutputStream os = new ObjectOutputStream(outputStream);
					os.writeObject(block1);
					
					byte[] data = outputStream.toByteArray();
					DatagramPacket sendPacket = new DatagramPacket(data, data.length,new InetSocketAddress(node.getAddress(), node.getPortNumber()));
					ds.send(sendPacket);
		
					buf = null;
					ds.close();
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	//Server receives the block from peers over here
	class ServerRecvThread implements Runnable{
		 
		@Override
		public void run() {
			byte[] buf = new byte[1024];
			String command = "CONNECT";
			DatagramSocket ds;
			try {
				ds = new DatagramSocket(9800);
			
				String message = "";
				DatagramPacket dp = null;
				while(true){
					dp = new DatagramPacket(buf, buf.length);
					ds.receive(dp);
					InetAddress address = dp.getAddress();
					int port = dp.getPort();

					byte[] data = dp.getData();
					ByteArrayInputStream in = new ByteArrayInputStream(data);
					ObjectInputStream is = new ObjectInputStream(in);
					int i = 1;
					try {
						Block recBlock = (Block)is.readObject();
						//manufacturerChain.add(i, recBlock);
						manufacturerChain.add(recBlock);
						System.out.println("Block object received from pharmacy = "+recBlock.toString());
						console.setText("The block received is+ "+recBlock.toString()+"\n");

						
						
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					Node node = new Node(port,address);
					manufacturerVertex = new Vertex<Node>(node);
					Vertex<Node> vertex = new Vertex<>(node);
					
					Edge<Node> newEdge = new Edge<Node>(0,manufacturerVertex,vertex);
					vertex.addEdge(newEdge);
					manufacturerGraph.getVertices().add(vertex);

					
					System.out.println("Graph: \n" + manufacturerGraph.toString());
					System.out.println("Connected to address" + address + " On Port number "+ port );
				
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
 
   }
	
	


}

