package application;

import java.io.ByteArrayInputStream;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
 * Student Number : 217026768
 *
 */
public class DeliveryHome extends StackPane implements Comparable {
	//Order Details*************************************
	Label drugName = new Label("Drug Name: ");
	Label dName = new Label("To get from the manufacturer");
	
	Label lblAmount = new Label("Quantity: ");
	Label lblQuantity = new Label("2");
	
	Label PharmacyName = new Label("Pharmacy name: ");
	Label pharmaName = new Label("ABC pharmacy");
	//Order Details*************************************
	
	Label lblDeliveryID = new Label("Delivery_ID: ");
	Label lblCompanyName = new Label("Company Name; ");
	Label lblDateReceived = new Label("Start of delivery: ");
	Label lblDateDelivered = new Label("Date delivered: ");
	Label lblTempReceived = new Label ("Temprature at start: ");
	Label lblTempDelivered = new Label("Temprature at delivery: ");
	Label lblSign = new Label("Digital signature: ");
	
	TextField txtDeliveryID = new TextField();
	TextField txtCompanyName = new TextField();
	TextField txtTempReceived = new TextField();
	TextField txtTempDelivered = new TextField();
	TextField txtSign = new TextField();
	Button btnDone = new Button("Send block to peers");
	Button btnConnect = new Button("Connect to Peers");
	Button btnsendPharma = new Button("Send block to pharmacy");
	TextArea console = new TextArea();
	
	
	
	DatePicker startDate = new DatePicker();
	DatePicker endDate = new DatePicker();

	//GRAPH INFORMATION****************************************
	Vertex<Node> deliveryVertex = null;
	Graph<Node> deliveryGraph = null;
	//GRAPH INFORMATION****************************************
	
	//public/private key
	PrivateKey deliveryPrivateKey = null;
	PublicKey deliveryPublicKey = null;
	KeyPair pair = null;
	SecureRandom randomsec = null;
	KeyPairGenerator keyGenerator = null;
	
	//blockchain for the delivery company
	ArrayList<Block> deliveryChain;
	int strength = 5;
	Button btnVerify = new Button("Verify BlockChain");
	Button btnViewContent = new Button("View Graph/blockchain Content");
	
	
	public DeliveryHome()
	{
		startHome();
		//instantiating graph for the delivery company
		deliveryGraph = new Graph<Node>();
		//creating blockchain for the delivery company
		deliveryChain = new ArrayList<Block>();
		
		InetAddress address;
		//creating a vertex for delivery company on the graph
		try {
			address = InetAddress.getByName("10.0.0.9");
			Node node = new Node(7800,address);
			deliveryVertex = new Vertex<Node>(node);
			Vertex<Node> vertex = new Vertex<>(node);
			Edge<Node> newEdge = new Edge<Node>(0,deliveryVertex,vertex);
			vertex.addEdge(newEdge);
			//adding the vertex to the manufacturer's graph
			deliveryGraph.getVertices().add(vertex);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//private and public key generation for the delivery
		try {
			keyGenerator = KeyPairGenerator.getInstance("DSA", "SUN");
			randomsec =  SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGenerator.initialize(1024,randomsec);
			//setting up the public and the private key for the delivery
			pair = keyGenerator.generateKeyPair();
			deliveryPrivateKey = pair.getPrivate();
			deliveryPublicKey = pair.getPublic();
			
			
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchProviderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	
		btnDone.setOnAction(e->{

			placeOrder();
			sendBlockPharmacy();
		});
		btnConnect.setOnAction(e->{
			btnConnect.setText("Connected");
			console.setText("Connected to pharmacy and manufacturer");
			connectToManufacturer();
		});
		btnVerify.setOnAction(e->{
			VerifyBlockChain verifyChain = new VerifyBlockChain();
			boolean verify = (!VerifyBlockChain.verifyBlockChain(deliveryChain,strength));
			console.setText("The status is  " + verify +"\n" +"BlockChain is authentic \n" + "Block added to the block chain");
		});
		btnViewContent.setOnAction(e->{
			String content = "";
			for(Block b : deliveryChain)
			{
				content += b.toString() + "\n";
			}
			console.setText("The Block Chain has the following records \n"+content+ "\n"+
					"The graph Content \n" + deliveryGraph.toString());
		});

	}
//Gui for delivery starts here	
	public void startHome()
	{
		Accordion accordion = new Accordion();
		TitledPane tpAcceptOrder = new TitledPane();
		TitledPane tpOrderPane = new TitledPane();
		GridPane AcceptPane = acceptDelivery();
		AcceptPane.setStyle("-fx-background-color:#aefbfc");
		
		GridPane order =  orderDetails();
		order.setStyle("-fx-background-color:#cdf5c9");
		
		tpAcceptOrder.setText("Accept an Order");
		
		tpOrderPane.setText("Check orders");
		
		tpAcceptOrder.setContent(AcceptPane);
		tpOrderPane.setContent(order);
		
		
		//accordion.getPanes().add(tpOrderPane);
		accordion.getPanes().add(tpAcceptOrder);
		
		
		btnDone.setPrefWidth(300);
		btnConnect.setPrefWidth(300);
		btnVerify.setPrefWidth(300);
		btnViewContent.setPrefWidth(300);
		VBox layout1 = new VBox(20);   
		layout1.setPadding(new Insets(5,5,5,5));
		layout1.getChildren().addAll(accordion,btnConnect,btnDone,btnVerify,btnViewContent,console);
		layout1.setAlignment(Pos.TOP_CENTER);
		getChildren().add(layout1);
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
	
	private GridPane acceptDelivery()
	{
		GridPane acceptPane = new GridPane();
		
		acceptPane.setVgap(4); //Add some spacing
		acceptPane.setPadding(new Insets(5,10,10,10));
		
		acceptPane.add(lblDeliveryID, 0, 0);
		acceptPane.add(lblCompanyName, 0, 1);
		acceptPane.add(lblDateReceived, 0, 2);
		acceptPane.add(lblDateDelivered, 0, 3);
		acceptPane.add(lblTempReceived, 0, 4);
		acceptPane.add(lblTempDelivered, 0, 5);
		acceptPane.add(lblSign, 0, 6);
		
		acceptPane.add(txtDeliveryID, 1, 0);
		acceptPane.add(txtCompanyName, 1, 1);
		acceptPane.add(startDate, 1, 2);
		acceptPane.add(endDate, 1, 3);
		acceptPane.add(txtTempReceived, 1, 4);
		acceptPane.add(txtTempDelivered, 1, 5);
		acceptPane.add(txtSign, 1, 6);
		
		//acceptPane.add(btnDone, 1, 8);
		acceptPane.setAlignment(Pos.CENTER);
		return acceptPane;
		
		
	}
//Delivery Gui ends here
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	//function that starts a thread where the delivery company receives the orders
	private void connectToManufacturer() {
		ClientRecvThread recv = new ClientRecvThread();
		new Thread(recv).start();
	}
	//function that sends the details to the manufacturer
	private void placeOrder() {
		ClientSendManufThread send = new ClientSendManufThread();
		new Thread(send).start();

	}
	//function that sends the details to the pharmacy
	private void sendBlockPharmacy()
	{
		ClientSendPharmfThread sendpharma = new ClientSendPharmfThread();
		new Thread(sendpharma).start();
	}
	
	
	
	class ClientSendManufThread implements Runnable {
		 
		@Override
		public void run() {
			try {
				DatagramSocket ds = new DatagramSocket();
				String str = "";
				String Command = "SENDBLOCKManf";
				byte[] buf = null;
				while (true) {
					    str = txtCompanyName.getText() + " " + txtTempReceived.getText() +" " + txtTempDelivered.getText();
					    
						Block block1 = new Block(deliveryChain.get(deliveryChain.size()-1).getHash(), str);
						//mine the block and then send
						deliveryChain.get(deliveryChain.size()-1).mine(strength);
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						ObjectOutputStream os = new ObjectOutputStream(outputStream);
						os.writeObject(block1);
						byte[] data = outputStream.toByteArray();;
	 
						DatagramPacket dp = new DatagramPacket(data, data.length,new InetSocketAddress("10.0.0.9", 8000));
						ds.send(dp);
						buf = null;
						ds.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	class ClientSendPharmfThread implements Runnable {
		 
		@Override
		public void run() {
			try {
				DatagramSocket ds = new DatagramSocket();
				String str = "";
				String Command = "SENDBLOCKPharma";
				byte[] buf = null;
				while (true) {
					 
						str = txtCompanyName.getText() + " " + txtTempReceived.getText() +" " + txtTempDelivered.getText();
					
						Block block1 = new Block(deliveryChain.get(deliveryChain.size()-1).getHash() , str);
						deliveryChain.get(deliveryChain.size()-1).mine(strength);
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						ObjectOutputStream os = new ObjectOutputStream(outputStream);
						os.writeObject(block1);
						byte[] data = outputStream.toByteArray();
						
	 
						DatagramPacket dp = new DatagramPacket(data, data.length,new InetSocketAddress("10.0.0.9", 9000));
						ds.send(dp);
						buf = null;
						ds.close();

				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class ClientRecvThread implements Runnable {
		 
		@Override
		public void run() {
			byte[] buf = new byte[1024];
			String Command = "CONNECT";
			DatagramSocket ds;
			try {
				ds = new DatagramSocket(7900);
			
				String message = "";
				DatagramPacket dp = null;
				while(true){
					dp = new DatagramPacket(buf, buf.length);
					ds.receive(dp);
					
					InetAddress address = dp.getAddress();
					int port = dp.getPort();
					//***CODE FOR TESSTING
					byte[] data = dp.getData();
					ByteArrayInputStream in = new ByteArrayInputStream(data);
					ObjectInputStream is = new ObjectInputStream(in);
					try {
						Block recBlock = (Block)is.readObject();
						deliveryChain.add(recBlock);
						System.out.println("Block object received = "+recBlock.toString());
						console.setText("Block received : " + recBlock.toString()+"\n");
						
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//getting the port number and the address of the packet received and adding it to the node 
					Node node = new Node(port,address);
					deliveryVertex = new Vertex<Node>(node);
					Vertex<Node> vertex = new Vertex<>(node);
					
					Edge<Node> newEdge = new Edge<Node>(0,deliveryVertex,vertex);
					vertex.addEdge(newEdge);
					deliveryGraph.getVertices().add(vertex);
				
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	



}
