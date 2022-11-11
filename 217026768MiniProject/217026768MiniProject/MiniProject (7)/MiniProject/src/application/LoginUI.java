package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginUI extends StackPane{
	TextField txtName;
	TextField txtLicense;
	Button btnLogin;
	public LoginUI()
	{
		GridPane loginPane = new GridPane();
		Label lblLisenceNumber = new Label("Lisence Number ");
	    txtLicense = new TextField();
		
		Label lblName = new Label("Company Name ");
	    txtName = new TextField();
		
		
		Label label1= new Label("Login");
	    btnLogin = new Button("Login");
	    btnLogin.setPrefWidth(250);
		loginPane.setVgap(4); //Add some spacing
		loginPane.setPadding(new Insets(5,5,5,5)); //Add some padding
		loginPane.add(lblLisenceNumber,0,0);
		loginPane.add(txtLicense, 1, 0);
		
		loginPane.add(lblName, 0, 1);
		loginPane.add(txtName, 1, 1);
		
		//loginPane.add(btnLogin, 0, 2);
		loginPane.setAlignment(Pos.CENTER);
		//button1.setOnAction(e -> PrimaryStage.setScene(scene2));   
		VBox layout1 = new VBox(20);     
		layout1.getChildren().addAll(loginPane,btnLogin);
		layout1.setAlignment(Pos.CENTER);
		getChildren().add(layout1);
	}

}

