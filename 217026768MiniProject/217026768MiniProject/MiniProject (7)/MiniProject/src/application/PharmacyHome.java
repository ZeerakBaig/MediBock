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
import java.util.StringTokenizer;

import Blockchain.Block;
import DataStructures.Node;
import application.Graph.Edge;
import application.Graph.Vertex;
//import application.pharmMain.ClientRecvThread;
//import application.pharmMain.ClientSendThread;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
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
public class PharmacyHome extends StackPane{
	
///Arraylist of block
	//Create order details**********************************************
	Block pharmacyBlock = null;
	
	Label lblDrugName = new Label("Drug Name: ");
	Label lblQuantity = new Label("Quantity");
	Label lblPharmacyName = new Label("Pharmacy Name: ");
	Label lblSignature  = new Label("Signature: ");
	Label lblStatus = new Label("");
	
	TextField txtDrugName = new TextField();
	TextField txtQuantity = new TextField();
	TextField txtPharmacyName = new TextField();	
	TextField txtSignature = new TextField();
	Button btnPlaceOrder = new Button("Send Order to Manufacturer");
	Button btnGet = new Button("Get");
	//Create order details**********************************************
	
	//Confirm Order details ********************************************
	Label lblLicenseNumber = new Label("License Number");
	Button btnCancel = new Button("Report");
	Label lblDrugID = new Label("Drug ID: ");
	
	TextField txtDrugID = new TextField();
	TextField txtLicenseNumber = new TextField();
    Button btnCheck = new Button("Check");
    TextArea txtDetails = new TextArea();
    Button btnConfirm = new Button("Connect to Peers");
    
    Label lblCompany = new Label("Company Name:");
    Label lblDateReceived = new Label("Date received:");
    Label lblTemprature = new Label("Temprature");
    
    Label lblCompanytext = new Label("");
    Label lblDateText = new Label("");
    Label lblTempText = new Label("");
    
    //Graph
	Graph<Node> pharmacyGraph = null;
	Vertex<Node> pharmacyVertex = null;
	TextArea console = new TextArea();
	ArrayList<Block> pharmacyChain;
	
	//Public and private keys
	PrivateKey pharmacyPrivateKey = null;
	PublicKey pharmacyPublicKey = null;
	KeyPair pair = null;
	SecureRandom randomsec = null;
	KeyPairGenerator keyGenerator = null;
    
    String qty = " ";
    int strength = 5;
    Button btnVerify = new Button("Verify BlockChain");
    Button btnViewContent = new Button("View Graph/blockchain Content");
    
	public PharmacyHome()
	{
		//creating a graph for the pharmacy
		pharmacyGraph = new Graph<Node>();
		//instantiating the blockchain for pharmacy
		pharmacyChain = new ArrayList<Block>();
		InetAddress address;
		//creating a vertex for the pharmacy that will be added to the graph
		try {
			address = InetAddress.getByName("10.0.0.9");
			Node node = new Node(9001,address);
			pharmacyVertex = new Vertex<Node>(node);
			Vertex<Node> vertex = new Vertex<>(node);
			Edge<Node> newEdge = new Edge<Node>(0,pharmacyVertex,vertex);
			vertex.addEdge(newEdge);
			//adding the vertex to the manufacturer's graph
			pharmacyGraph.getVertices().add(vertex);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//private and public key generation for the pharmacy
		try {
			keyGenerator = KeyPairGenerator.getInstance("DSA", "SUN");
			randomsec =  SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGenerator.initialize(1024,randomsec);
			//setting up the public and the private key for the pharmacy
			pair = keyGenerator.generateKeyPair();
			pharmacyPrivateKey = pair.getPrivate();
			pharmacyPublicKey = pair.getPublic();
			
			
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchProviderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		startHome();
		
		btnPlaceOrder.setOnAction(e->{
			placeOrder();
		});
		btnConfirm.setOnAction(e->{
			btnConfirm.setText("Connected");
			console.setText("Connected to delivery and manufacturer");
			connectToManufacturer();
		});
		btnVerify.setOnAction(e->{
			VerifyBlockChain verifyChain = new VerifyBlockChain();
			boolean verify =(!VerifyBlockChain.verifyBlockChain(pharmacyChain,strength));
			if(verify == true)
			{
				console.setText("The status is :  "+ verify + "\n"+ "BlockChain is authentic \n" + "Block added to blockChain");
			}
			
		});
		btnViewContent.setOnAction(e->{
			String content = "";
			for(Block b : pharmacyChain)
			{
				content += b.toString() + "\n";
			}
			console.setText("The Block Chain has the following records \n"+content+ "\n"+
					"The graph Content \n" + pharmacyGraph.toString());
		});
		
		
	}
	
		
	//GUI for the pharmacy
	public void startHome()
	{
		Accordion accordion = new Accordion();
		TitledPane tpCreateOrder = new TitledPane();
		TitledPane tpOrderPane = new TitledPane();
		TitledPane checkorderPane = new TitledPane();
		
		GridPane manuPane =  createOrderPane();
		manuPane.setStyle("-fx-background-color:#aefbfc");
		
		GridPane order =  confirmOrderPane() ;
		order.setStyle("-fx-background-color:#cdf5c9");
		
		GridPane checkorder = checkOrder();
		checkorder.setStyle("-fx-background-color:#cdf5c9");
		
		tpCreateOrder.setText("Check orders");
		
		tpOrderPane.setText("Create an Order");
		
		checkorderPane.setText("Check order");
		
		tpCreateOrder.setContent(order);
		tpOrderPane.setContent(manuPane);
		checkorderPane.setContent(checkorder);
		
		//accordion.getPanes().add(checkorderPane);
		accordion.getPanes().add(tpOrderPane);
		//accordion.getPanes().add(tpCreateOrder);
		
		btnConfirm.setPrefWidth(300);
		btnVerify.setPrefWidth(300);
		btnViewContent.setPrefWidth(300);
		
		VBox layout1 = new VBox(20);   
		layout1.setPadding(new Insets(5,5,5,5));
		layout1.getChildren().addAll(accordion,btnConfirm,btnVerify,btnViewContent,console);
		layout1.setAlignment(Pos.TOP_CENTER);
		getChildren().add(layout1);
		
	}
	
	public GridPane createOrderPane()
	{
		GridPane createOrderPane = new GridPane();
		createOrderPane.add(lblDrugName, 0, 0);
		createOrderPane.add(lblQuantity, 0, 1);
		createOrderPane.add(lblPharmacyName, 0, 2);
		createOrderPane.add(lblSignature, 0, 3);
		createOrderPane.add(btnPlaceOrder, 0, 4);
		
		createOrderPane.add(txtDrugName, 1, 0);
		createOrderPane.add(txtQuantity, 1, 1);
		createOrderPane.add(txtPharmacyName, 1, 2);
		createOrderPane.add(txtSignature, 1, 3);
		
		createOrderPane.add(lblStatus, 3, 0);
		
		
		
		return createOrderPane;
		
		
	}
	public GridPane checkOrder()
	{
		GridPane checkPane = new GridPane();
		checkPane.add(lblCompany, 0, 0);
		checkPane.add(lblDateReceived, 0, 1);
		checkPane.add(lblTemprature, 0, 2);
		
		checkPane.add(lblCompanytext, 1, 0);
		checkPane.add(lblDateText, 1, 1);
		checkPane.add(lblTempText, 1, 2);
		return checkPane;
	}
	public GridPane confirmOrderPane()
	{
		txtDetails.setPrefHeight(100);
		txtDetails.setPrefWidth(100);
		GridPane confirmPane = new GridPane();
		confirmPane.setPadding(new Insets(5,10,10,10));
		
		confirmPane.add(lblLicenseNumber, 0, 0);
		confirmPane.add(lblDrugID, 0, 1);
		confirmPane.add(btnCheck, 2, 1);
		
		confirmPane.add(txtLicenseNumber, 1, 0);
		confirmPane.add(txtDrugID, 1, 1);
		confirmPane.add(txtDetails, 0, 3);
		confirmPane.add(btnCancel, 0, 4);
		//confirmPane.add(btnConfirm, 1, 4);
		
		
		
		return confirmPane;
		
	}

	
	private void connectToManufacturer() {
		PharmacyRecvThread recv = new PharmacyRecvThread();
		new Thread(recv).start();
	}
	private void placeOrder() {
		PharmacySendThread send = new PharmacySendThread();
		new Thread(send).start();

	}
	
	
	
	class PharmacySendThread implements Runnable {
		 
		@Override
		public void run() {
			try {
				DatagramSocket ds = new DatagramSocket();
				String str = "";
				byte[] buf = null;
				while (true) {
			
					    String Command = "SENDBLOCKManf";
						str = txtDrugName.getText() + " " + txtQuantity.getText() +" " + txtPharmacyName.getText();
						//creating the genesis block
						Block block1 = new Block("00000", str);
						//adding the block to the pharmacy blockchain
						pharmacyChain.add(block1);
						pharmacyChain.get(0).mine(strength);
						
						
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						ObjectOutputStream os = new ObjectOutputStream(outputStream);
						os.writeObject(block1);
						byte[] data = outputStream.toByteArray();

	 
						DatagramPacket dp = new DatagramPacket(data, data.length,new InetSocketAddress("10.0.0.9", 9800));
						ds.send(dp);
						buf = null;
						ds.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class PharmacyRecvThread implements Runnable {
		 
		@Override
		public void run() {
			byte[] buf = new byte[1024];
			String Command = "CONNECT";
			DatagramSocket ds;
			try {
				ds = new DatagramSocket(9000);
			
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
						//verify here and then add the block
						pharmacyChain.add(recBlock);
						System.out.println("Block object received = "+recBlock.toString());
						console.setText("The block received is" + recBlock.toString()+"\n");
						
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//get the port numberand the address of the packet received and add it to the graph of the pharmacy 
					Node node = new Node(port,address);
					pharmacyVertex = new Vertex<Node>(node);
					Vertex<Node> vertex = new Vertex<>(node);
					
					Edge<Node> newEdge = new Edge<Node>(0,pharmacyVertex,vertex);
					vertex.addEdge(newEdge);
					pharmacyGraph.getVertices().add(vertex);
					
				
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}
