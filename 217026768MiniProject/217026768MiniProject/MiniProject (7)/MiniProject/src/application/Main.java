package application;

import java.util.Collection;

import Blockchain.Block;
import application.Graph.Edge;
import application.Graph.Vertex;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;



public class Main extends Application {
	Scene scene1, scene2;

	Button btnManufacturer;
	Button btnDelivery;
	Button btnPharmacy;
	
	@Override
	public void start(Stage primaryStage) {		
	    startup();
		
	// Gui is here**********************************************************    

	LoginUI mLogin = new LoginUI();
	
    btnManufacturer.setOnAction(e->{
    	ManufacturerHome mMain = new ManufacturerHome(); 
    	Scene ManufacturerScene = new Scene(mMain,400,500);
    	primaryStage.setTitle("Manufacturer Home");
    	primaryStage.setScene(ManufacturerScene);
     	//scene2 = new Scene(mLogin,400,500);
    	//primaryStage.setScene(scene2);
    });
    btnDelivery.setOnAction(e->{
    	DeliveryHome dMain = new DeliveryHome();
		Scene DeliveryScene = new Scene(dMain,400,500);
		primaryStage.setTitle("Delivery Home");
		primaryStage.setScene(DeliveryScene);
    	//scene2 = new Scene(mLogin,400,500);
    	//primaryStage.setScene(scene2);
    });
    btnPharmacy.setOnAction(e->{
    	PharmacyHome pHome = new PharmacyHome();
		Scene PharmacyScene = new Scene(pHome,400,500);
		primaryStage.setTitle("Pharmacy Home");
		primaryStage.setScene(PharmacyScene);
    	//scene2 = new Scene(mLogin,400,500);
    	//primaryStage.setScene(scene2);
    });
    
	// Gui ends here**********************************************************  
   
    
    
	primaryStage.setScene(scene1);
	primaryStage.show();
	}
	
	//gui that presents a user with different user types to select from
	public void startup()
	{
		Label lbluserType = new Label("Select Your User Type");
		lbluserType.setFont(Font.font(null, FontWeight.BOLD, 36));
	 
		btnManufacturer = new Button("Manufacturer");
		btnDelivery = new Button("Delivery");
		btnPharmacy = new Button("Pharmacy");
		
		btnPharmacy.setPrefWidth(300);
		btnDelivery.setPrefWidth(300);
		btnManufacturer.setPrefWidth(300);
		
		VBox layout1 = new VBox(20);     
		layout1.setPadding(new Insets(5,5,5,5));
		layout1.setAlignment(Pos.CENTER);
		layout1.getChildren().addAll(lbluserType,btnManufacturer, btnDelivery, btnPharmacy);
		scene1= new Scene(layout1, 500, 500);
	}

	public static void main(String[] args) {
	launch(args);
	}
	    
}
