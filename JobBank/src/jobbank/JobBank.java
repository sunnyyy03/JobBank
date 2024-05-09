package jobbank;

// JavaFX Imports

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// JBDC Imports

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JobBank extends Application
{
	// JavaFX global variables
	private Stage primaryStage; // Object for main window
	
	// SQL connection global variables
	private Connection conn; // Connection to TMU SQL database
	private String username;
	private String password;
			
	public void start(Stage primaryStage)
	{
		// Initialize GUI
		this.primaryStage = primaryStage; // Set the stage
		
		Label welcomeMessage = new Label("Welcome to the Job Bank Database Manager!"); // Create welcome label
		
		// Create text fields
		
		TextField userField = new TextField(); // Text field for user to enter their username
        PasswordField passField = new PasswordField(); // Text field for user to enter their password
		
		// Login Notification Label
        Label loginFail = new Label(); //Label to notify the customer if login was unsuccessful
		// Login Button
        Button loginButton = new Button("Login"); //Create login button for the user to attemp a login
		
		// Create master vertical box containing all window elements
        VBox vbox = new VBox(40, welcomeMessage, new Label("Username:"), userField, new Label("Password:"), passField, loginButton, loginFail); //Create vertical box containing all elements
        vbox.setStyle("-fx-padding: 10"); // Set padding to 10 pixels
		
		// Button Actions
        
        loginButton.setOnAction(event -> // Detect if "Login" button is clicked
        {
            username = userField.getText(); // Get user input from username text field
            password = passField.getText(); // Get user input from password text field
			
			try
			{
				conn = DriverManager.getConnection("jdbc:oracle:thin:@oracle.scs.ryerson.ca:1521:orcl", username, password); // Attempt connection to OracleSQL database using credentials
				showMenuScreen(primaryStage);
			}
			catch(SQLException e) // Check if user inputs invalid credentials
			{
				loginFail.setText("Incorrect username or password! Please try again."); //Print error
				userField.clear(); //Clear username text field for next login attempt
                passField.clear(); //Clear password text field for next login attempt
			}
        });
        
        //Create the scene
        Scene scene = new Scene(vbox, 500, 500); //Draw scene with window resolution of 500x500 using master vbox
        primaryStage.setTitle("Job Bank GUI Manager - Login"); //Set window title
        primaryStage.setScene(scene); //Set the scene
        primaryStage.show(); //Show the scene
		
	}
	
	private void showMenuScreen(Stage primaryStage)
	{
		// Create buttons
		Button dropTablesButton = new Button("Drop Tables");
        Button createTablesButton = new Button("Create Tables");
        Button populateTablesButton = new Button("Populate Tables");
		Button queryTablesButton = new Button("Queries Menu");
		Button logoutButton = new Button("Logout");
		
		// Create drop notification
		Label dropNotification = new Label(); //Label to notify the customer if login was unsuccessful
		
		// Create master vertical box containing all window elements
		VBox vbox = new VBox(10, dropTablesButton, createTablesButton, populateTablesButton, queryTablesButton, logoutButton, dropNotification);
        vbox.setAlignment(Pos.CENTER);
		
		//Button Actions
        dropTablesButton.setOnAction(event ->
		{
			dropNotification.setText("Tables dropped from database.");
			
			try
			{
				String[] tables = {
					"HR_EMPLOYEE",
					"USER",
					"EMPLOYER",
					"ADMINISTRATOR",
					"APPLICANT",
					"JOB",
					"LOCATION",
					"JOB_QUALIFICATIONS",
					"RESUME",
					"APPLICANT_QUALIFICATIONS",
					"APPLICATION"
				};

				for (String table: tables)
				{
					String sql = "DROP TABLE \"" + table + "\" CASCADE CONSTRAINTS";
					PreparedStatement statement = conn.prepareStatement(sql);
					statement.executeUpdate();
					statement.close();
				}
			}
			catch(SQLException e) {}
		});
		
		createTablesButton.setOnAction(event ->
		{
			dropNotification.setText("Tables added to database.");
			
			try
			{
				// Create HR_EMPLOYEE table
				String sql =
					"CREATE TABLE \"HR_EMPLOYEE\"(" + 
					"\"employeeID\" SMALLINT PRIMARY KEY," +
					"\"first_name\" VARCHAR(20) NOT NULL," +
					"\"last_name\" VARCHAR(20) NOT NULL," +
					"\"phoneNumber\" VARCHAR(12) DEFAULT '-1' UNIQUE" + 
					")";
				
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.executeUpdate();
				
				// Create USER table
				sql = 
					"CREATE TABLE \"USER\"(" +
					"\"userID\" SMALLINT PRIMARY KEY," +
					"\"username\" VARCHAR2(20) NOT NULL UNIQUE," +
					"\"password\" VARCHAR2(20) NOT NULL" +
					")";
				
				statement = conn.prepareStatement(sql);
				statement.executeUpdate();
				
				// Create EMPLOYER table
				sql = 
					"CREATE TABLE \"EMPLOYER\"(" +
					"\"employerID\" SMALLINT PRIMARY KEY," +
					"\"userID\" SMALLINT REFERENCES \"USER\"(\"userID\")," +
					"\"first_name\" VARCHAR2(20) NOT NULL," +
					"\"last_name\" VARCHAR2(20) NOT NULL," +
					"\"age\" SMALLINT DEFAULT '-1'" +
					")";
				
				statement = conn.prepareStatement(sql);
				statement.executeUpdate();
				
				// Create ADMINISTRATOR table
				sql = 
					"CREATE TABLE \"ADMINISTRATOR\"(" +
					"\"administratorID\" SMALLINT PRIMARY KEY," +
					"\"userID\" SMALLINT REFERENCES \"USER\"(\"userID\")," +
					"\"first_name\" VARCHAR2(20) NOT NULL," +
					"\"last_name\" VARCHAR2(20) NOT NULL" +
					")";
				
				statement = conn.prepareStatement(sql);
				statement.executeUpdate();
				
				// Create APPLICANT table
				sql = 
					"CREATE TABLE \"APPLICANT\"(" + 
					"\"applicantID\" SMALLINT PRIMARY KEY," +
					"\"userID\" SMALLINT REFERENCES \"USER\"(\"userID\")," +
					"\"first_name\" VARCHAR2(20) NOT NULL," +
					"\"last_name\" VARCHAR2(20) NOT NULL," +
					"\"age\" SMALLINT DEFAULT '-1'" +
					")";
				
				statement = conn.prepareStatement(sql);
				statement.executeUpdate();
				
				// Create JOB table
				sql = 
					"CREATE TABLE \"JOB\"(" + 
					"\"jobID\" SMALLINT PRIMARY KEY," +
					"\"employerID\" SMALLINT REFERENCES \"EMPLOYER\"(\"employerID\")," +
					"\"company\" VARCHAR2(40) NOT NULL," +
					"\"profession\" VARCHAR2(40) NOT NULL," +
					"\"salary\" INTEGER NOT NULL," + 
					"\"date_posted\" DATE NOT NULL," + 
					"\"deadline_date\" DATE NOT NULL," + 
					"\"duration\" VARCHAR2(20) DEFAULT 'Unknown'," + 
					"\"arrangement\" VARCHAR2(20) DEFAULT 'Unknown'" +
					")";
				
				statement = conn.prepareStatement(sql);
				statement.executeUpdate();
				
				// Create LOCATION table
				sql = 
					"CREATE TABLE \"LOCATION\"(" + 
					"\"locationID\" SMALLINT PRIMARY KEY," +
					"\"jobID\" SMALLINT REFERENCES \"JOB\"(\"jobID\")," +
					"\"number\" SMALLINT DEFAULT '-1'," +
					"\"street\" VARCHAR2(40) DEFAULT 'N/A'," +
					"\"zip_code\" VARCHAR2(7) DEFAULT 'N/A'" + 
					")";
				
				statement = conn.prepareStatement(sql);
				statement.executeUpdate();
				
				// Create JOB_QUALIFICATIONS table
				sql = 
					"CREATE TABLE \"JOB_QUALIFICATIONS\"(" + 
					"\"job_qualificationsID\" SMALLINT PRIMARY KEY," +
					"\"jobID\" SMALLINT REFERENCES \"JOB\"(\"jobID\")," +
					"\"experienceLevel\" VARCHAR2(40) DEFAULT 'N/A'," +
					"\"educationLevel\" VARCHAR2(40) DEFAULT 'N/A'" + 
					")";
				
				statement = conn.prepareStatement(sql);
				statement.executeUpdate();
				
				// Create RESUME table
				sql = 
					"CREATE TABLE \"RESUME\"(" + 
					"\"resumeID\" SMALLINT PRIMARY KEY," +
					"\"applicantID\" SMALLINT REFERENCES \"APPLICANT\"(\"applicantID\")" +
					")";
				
				statement = conn.prepareStatement(sql);
				statement.executeUpdate();
				
				// Create APPLICANT_QUALIFICATIONS table
				sql = 
					"CREATE TABLE \"APPLICANT_QUALIFICATIONS\"(" + 
					"\"applicant_qualificationsID\" SMALLINT PRIMARY KEY," +
					"\"resumeID\" SMALLINT REFERENCES \"RESUME\"(\"resumeID\")," +
					"\"experienceLevel\" VARCHAR2(40) DEFAULT 'N/A'," +
					"\"educationLevel\" VARCHAR2(40) DEFAULT 'N/A'" + 
					")";
				
				statement = conn.prepareStatement(sql);
				statement.executeUpdate();
				
				// Create APPLICATION table
				sql = 
					"CREATE TABLE \"APPLICATION\"(" + 
					"\"applicationID\" SMALLINT PRIMARY KEY," +
					"\"applicantID\" SMALLINT REFERENCES \"APPLICANT\"(\"applicantID\")," +
					"\"resumeID\" SMALLINT REFERENCES \"RESUME\"(\"resumeID\")," +
					"\"status\" VARCHAR2(20) NOT NULL," +
					"\"submissionDate\" DATE NOT NULL" +
					")";
				
				statement = conn.prepareStatement(sql);
				statement.executeUpdate();
				statement.close();
			}
			catch (SQLException e){e.printStackTrace();}
		});
		
		populateTablesButton.setOnAction(event ->
		{
			dropNotification.setText("Populated tables.");
			
			try
			{
				String[] populateStatements = {
					// Insert users into USER table
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (1234, 'Albert', 'Albert234')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (5678, 'Uganda', 'Ug134')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (9012, 'ewri', '3j44')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (3456, 'geg', 'ge78')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (7890, 'sdad', 'ds1')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (2345, 'fdsf', 'sd3d')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (6789, 'dafda', '3ds21')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (4321, 'daf', '8ds9')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (8765, 'fkdf', '8fd7')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (5432, 'dksf', 'ds087')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (9876, 'iwoq', '4df5')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (2109, 'popo', 'fd54d')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (6543, 'jndfsf', 'dsf24')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (1098, 'were', 'dfsd')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (8761, 'eghr', 'vb15')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (5430, 'dfsh', 'bdb6')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (2104, 'sdof', 'qre6')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (8760, 'djfks', 'web5')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (1230, 'iowhf', 'web4')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (5671, 'djfnd', 'asd')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (9011, 'fdsfds', 'podfj')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (2340, 'dgsre', 'o8973')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (5670, 'gfgdh', 'op53')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (9010, 'dsgfds4', 'op23')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (4320, 'fgpl', 'p7bvl')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (8762, 'dsfp', 'werq2')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (1097, 'gerpo', 'mksd8')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (5431, 'lmao', 'yure6')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (2108, 'rofl', 'iu36')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (9875, 'xdxf', 'io034')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (6544, 'wopr', 'lmao09')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (2101, 'womp', 'lmao07')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (8763, 'bando', 's3b')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (4322, 'pwr', 'w3b')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (5433, 'resis', 'l3b')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (6545, 'ohm', 'r3b')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (1096, 'gen', 'g3n')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (9874, 'seb', 'bdb6')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (5672, 'web', 'pihf8')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (4323, 'leb', 'yg34')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (9013, 'feb', '8yaf')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (1095, 'greb', '98ew')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (8764, 'abdi', '23gytwf')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (2341, 'wabdi', 'moh78')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (2102, 'labdi', 'agd63')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (5434, 'sunny', 'wfe23')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (5673, 'night', 'dgb23')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (9877, 'day', '985dsa')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (4324, 'evening', '45yafh')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (1235, 'username1', 'password1')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (5679, 'username2', 'password2')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (9014, 'username3', 'password3')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (3457, 'username4', 'password4')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (7891, 'username5', 'password5')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (2346, 'username6', 'password6')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (6790, 'username7', 'password7')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (4325, 'username8', 'password8')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (8766, 'username9', 'password9')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (5435, 'username10', 'password10')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (9878, 'username11', 'password11')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (2110, 'username12', 'password12')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (6546, 'username13', 'password13')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (1099, 'username14', 'password14')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (8767, 'username15', 'password15')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (5436, 'username16', 'password16')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (2105, 'username17', 'password17')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (8768, 'username18', 'password18')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (1231, 'username19', 'password19')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (5674, 'username20', 'password20')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (9015, 'username21', 'password21')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (2342, 'username22', 'password22')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (5675, 'username23', 'password23')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (9016, 'username24', 'password24')",
					"INSERT INTO \"USER\"(\"userID\", \"username\", \"password\") VALUES (9017, 'username25', 'password25')",
					
					// Insert administrators into ADMINISTRATOR table
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (1, 1234, 'AdminFirstName1', 'AdminLastName1')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (2, 5678, 'AdminFirstName2', 'AdminLastName2')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (3, 9012, 'AdminFirstName3', 'AdminLastName3')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (4, 3456, 'AdminFirstName4', 'AdminLastName4')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (5, 7890, 'AdminFirstName5', 'AdminLastName5')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (6, 2345, 'AdminFirstName6', 'AdminLastName6')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (7, 6789, 'AdminFirstName7', 'AdminLastName7')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (8, 4321, 'AdminFirstName8', 'AdminLastName8')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (9, 8765, 'AdminFirstName9', 'AdminLastName9')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (10, 5432, 'AdminFirstName10', 'AdminLastName10')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (11, 9876, 'AdminFirstName11', 'AdminLastName11')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (12, 2109, 'AdminFirstName12', 'AdminLastName12')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (13, 6543, 'AdminFirstName13', 'AdminLastName13')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (14, 1098, 'AdminFirstName14', 'AdminLastName14')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (15, 8761, 'AdminFirstName15', 'AdminLastName15')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (16, 5430, 'AdminFirstName16', 'AdminLastName16')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (17, 2104, 'AdminFirstName17', 'AdminLastName17')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (18, 8760, 'AdminFirstName18', 'AdminLastName18')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (19, 1230, 'AdminFirstName19', 'AdminLastName19')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (20, 5671, 'AdminFirstName20', 'AdminLastName20')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (21, 9011, 'AdminFirstName21', 'AdminLastName21')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (22, 2340, 'AdminFirstName22', 'AdminLastName22')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (23, 5670, 'AdminFirstName23', 'AdminLastName23')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (24, 9010, 'AdminFirstName24', 'AdminLastName24')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (25, 4320, 'AdminFirstName25', 'AdminLastName25')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (26, 8762, 'AdminFirstName26', 'AdminLastName26')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (27, 1097, 'AdminFirstName27', 'AdminLastName27')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (28, 5431, 'AdminFirstName28', 'AdminLastName28')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (29, 2108, 'AdminFirstName29', 'AdminLastName29')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (30, 9875, 'AdminFirstName30', 'AdminLastName30')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (31, 6544, 'AdminFirstName31', 'AdminLastName31')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (32, 2101, 'AdminFirstName32', 'AdminLastName32')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (33, 8763, 'AdminFirstName33', 'AdminLastName33')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (34, 4322, 'AdminFirstName34', 'AdminLastName34')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (35, 5433, 'AdminFirstName35', 'AdminLastName35')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (36, 6545, 'AdminFirstName36', 'AdminLastName36')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (37, 1096, 'AdminFirstName37', 'AdminLastName37')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (38, 9874, 'AdminFirstName38', 'AdminLastName38')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (39, 5672, 'AdminFirstName39', 'AdminLastName39')",
					"INSERT INTO \"ADMINISTRATOR\"(\"administratorID\", \"userID\", \"first_name\", \"last_name\") VALUES (40, 4323, 'AdminFirstName40', 'AdminLastName40')",
					
					// Insert employers into EMPLOYER table
					"INSERT INTO \"EMPLOYER\"(\"employerID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (1, 1235, 'EmployerFirstName1', 'EmployerLastName1', 36)",
					"INSERT INTO \"EMPLOYER\"(\"employerID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (2, 5679, 'EmployerFirstName2', 'EmployerLastName2', 36)",
					"INSERT INTO \"EMPLOYER\"(\"employerID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (3, 9014, 'EmployerFirstName3', 'EmployerLastName3', 36)",
					"INSERT INTO \"EMPLOYER\"(\"employerID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (4, 3457, 'EmployerFirstName4', 'EmployerLastName4', 36)",
					"INSERT INTO \"EMPLOYER\"(\"employerID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (5, 7891, 'EmployerFirstName5', 'EmployerLastName5', 36)",
					"INSERT INTO \"EMPLOYER\"(\"employerID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (6, 2346, 'EmployerFirstName6', 'EmployerLastName6', 36)",
					"INSERT INTO \"EMPLOYER\"(\"employerID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (7, 6790, 'EmployerFirstName7', 'EmployerLastName7', 36)",
					"INSERT INTO \"EMPLOYER\"(\"employerID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (8, 4325, 'EmployerFirstName8', 'EmployerLastName8', 36)",
					"INSERT INTO \"EMPLOYER\"(\"employerID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (9, 8766, 'EmployerFirstName9', 'EmployerLastName9', 36)",
					"INSERT INTO \"EMPLOYER\"(\"employerID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (10, 5435, 'EmployerFirstName10', 'EmployerLastName10', 36)",
					
					// Insert applicants into APPLICANT table
					"INSERT INTO \"APPLICANT\"(\"applicantID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (1, 9878, 'ApplicantFirstName1', 'ApplicantLastName1', 25)",
					"INSERT INTO \"APPLICANT\"(\"applicantID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (2, 2110, 'ApplicantFirstName2', 'ApplicantLastName2', 28)",
					"INSERT INTO \"APPLICANT\"(\"applicantID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (3, 6546, 'ApplicantFirstName3', 'ApplicantLastName3', 30)",
					"INSERT INTO \"APPLICANT\"(\"applicantID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (4, 1099, 'ApplicantFirstName4', 'ApplicantLastName4', 32)",
					"INSERT INTO \"APPLICANT\"(\"applicantID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (5, 8767, 'ApplicantFirstName5', 'ApplicantLastName5', 27)",
					"INSERT INTO \"APPLICANT\"(\"applicantID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (6, 5436, 'ApplicantFirstName6', 'ApplicantLastName6', 29)",
					"INSERT INTO \"APPLICANT\"(\"applicantID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (7, 2105, 'ApplicantFirstName7', 'ApplicantLastName7', 26)",
					"INSERT INTO \"APPLICANT\"(\"applicantID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (8, 8768, 'ApplicantFirstName8', 'ApplicantLastName8', 31)",
					"INSERT INTO \"APPLICANT\"(\"applicantID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (9, 1231, 'ApplicantFirstName9', 'ApplicantLastName9', 34)",
					"INSERT INTO \"APPLICANT\"(\"applicantID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (10, 5674, 'ApplicantFirstName10', 'ApplicantLastName10', 33)",
					"INSERT INTO \"APPLICANT\"(\"applicantID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (11, 9015, 'ApplicantFirstName11', 'ApplicantLastName11', 22)",
					"INSERT INTO \"APPLICANT\"(\"applicantID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (12, 2342, 'ApplicantFirstName12', 'ApplicantLastName12', 35)",
					"INSERT INTO \"APPLICANT\"(\"applicantID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (13, 5675, 'ApplicantFirstName13', 'ApplicantLastName13', 23)",
					"INSERT INTO \"APPLICANT\"(\"applicantID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (14, 9016, 'ApplicantFirstName14', 'ApplicantLastName14', 37)",
					"INSERT INTO \"APPLICANT\"(\"applicantID\", \"userID\", \"first_name\", \"last_name\", \"age\") VALUES (15, 9017, 'ApplicantFirstName15', 'ApplicantLastName15', 20)",
					
					//Insert jobs into JOB table
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(1, 1, 'Walmart', 'Cashier', 30000, TO_DATE('092323', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '1 Year', 'In Person')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(2, 1, 'Walmart', 'Greeter', 28000, TO_DATE('092423', 'MMDDYY'), TO_DATE('121123', 'MMDDYY'), '1 Year', 'In Person')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(3, 2, 'Amazon', 'Warehouse Associate', 35000, TO_DATE('092523', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '2 Years', 'Remote')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(4, 2, 'Amazon', 'Delivery Driver', 40000, TO_DATE('092623', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '1 Year', 'In Person')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(5, 3, 'Google', 'Software Engineer', 80000, TO_DATE('092723', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '3 Years', 'Remote')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(6, 3, 'Google', 'Data Scientist', 75000, TO_DATE('092823', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '2 Years', 'In Person')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(7, 4, 'Microsoft', 'Software Developer', 75000, TO_DATE('092923', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '2 Years', 'Remote')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(8, 4, 'Microsoft', 'Project Manager', 85000, TO_DATE('093023', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '3 Years', 'In Person')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(9, 5, 'Apple', 'iOS Developer', 80000, TO_DATE('093023', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '2 Years', 'Remote')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(10, 5, 'Apple', 'Graphic Designer', 60000, TO_DATE('092523', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '1 Year', 'In Person')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(11, 6, 'Facebook', 'Social Media Manager', 70000, TO_DATE('092523', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '2 Years', 'Remote')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(12, 6, 'Facebook', 'Product Designer', 78000, TO_DATE('092523', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '2 Years', 'In Person')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(13, 7, 'Tesla', 'Mechanical Engineer', 90000, TO_DATE('092523', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '3 Years', 'Remote')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(14, 7, 'Tesla', 'Quality Assurance', 70000, TO_DATE('092523', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '2 Years', 'In Person')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(15, 8, 'Netflix', 'Content Writer', 60000, TO_DATE('092523', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '1 Year', 'Remote')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(16, 8, 'Netflix', 'Video Editor', 70000, TO_DATE('092523', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '2 Years', 'In Person')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(17, 9, 'Uber', 'Driver', 40000, TO_DATE('092523', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '1 Year', 'Hybrid')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(18, 9, 'Uber', 'Operations Manager', 75000, TO_DATE('092523', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '2 Years', 'In Person')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(19, 10, 'Twitter', 'Marketing Specialist', 65000, TO_DATE('092523', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '1 Year', 'Hybrid')",
					"INSERT INTO \"JOB\"(\"jobID\", \"employerID\", \"company\", \"profession\", \"salary\", \"date_posted\", \"deadline_date\", \"duration\", \"arrangement\") VALUES(20, 10, 'Twitter', 'Data Analyst', 70000, TO_DATE('092523', 'MMDDYY'), TO_DATE('123123', 'MMDDYY'), '2 Years', 'In Person')",

					//Insert locations into LOCATION table
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(1, 1, '123', 'Main St', '12345')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(2, 2, '456', 'Oak St', '56789')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(3, 3, '789', 'Elm St', '98765')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(4, 4, '101', 'Pine St', '54321')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(5, 5, '234', 'Cedar St', '45678')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(6, 6, '567', 'Birch St', '98765')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(7, 7, '890', 'Maple St', '65432')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(8, 8, '123', 'Oak Ave', '23456')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(9, 9, '456', 'Elm Ave', '78901')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(10, 10, '789', 'Pine Ave', '12345')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(11, 11, '101', 'Cedar Ave', '23456')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(12, 12, '234', 'Birch Ave', '34567')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(13, 13, '567', 'Maple Ave', '45678')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(14, 14, '890', 'Oak Blvd', '56789')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(15, 15, '123', 'Elm Blvd', '67890')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(16, 16, '456', 'Pine Blvd', '78901')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(17, 17, '789', 'Cedar Blvd', '89012')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(18, 18, '101', 'Birch Blvd', '90123')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(19, 19, '234', 'Maple Blvd', '01234')",
					"INSERT INTO \"LOCATION\"(\"locationID\", \"jobID\", \"number\", \"street\", \"zip_code\") VALUES(20, 20, '567', 'Oak Ct', '12345')",
					
					//Insert job qualifications into JOB_QUALIFICATIONS table
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(1, 1, 'Entry Level', 'High School Diploma')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(2, 2, 'Entry Level', 'High School Diploma')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(3, 3, 'Mid Level', 'Bachelor''s Degree')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(4, 4, 'Entry Level', 'High School Diploma')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(5, 5, 'Mid Level', 'Bachelor''s Degree')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(6, 6, 'Mid Level', 'Master''s Degree')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(7, 7, 'Mid Level', 'Bachelor''s Degree')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(8, 8, 'Senior Level', 'Master''s Degree')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(9, 9, 'Mid Level', 'Bachelor''s Degree')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(10, 10, 'Entry Level', 'High School Diploma')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(11, 11, 'Mid Level', 'Bachelor''s Degree')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(12, 12, 'Senior Level', 'Ph.D.')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(13, 13, 'Mid Level', 'Master''s Degree')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(14, 14, 'Mid Level', 'Bachelor''s Degree')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(15, 15, 'Entry Level', 'High School Diploma')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(16, 16, 'Mid Level', 'Bachelor''s Degree')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(17, 17, 'Entry Level', 'High School Diploma')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(18, 18, 'Senior Level', 'Master''s Degree')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(19, 19, 'Mid Level', 'Bachelor''s Degree')",
					"INSERT INTO \"JOB_QUALIFICATIONS\"(\"job_qualificationsID\", \"jobID\", \"experienceLevel\", \"educationLevel\") VALUES(20, 20, 'Entry Level', 'High School Diploma')",
					
					//Insert resumes into RESUME table
					"INSERT INTO \"RESUME\"(\"resumeID\", \"applicantID\") VALUES(1, 1)",
					"INSERT INTO \"RESUME\"(\"resumeID\", \"applicantID\") VALUES(2, 2)",
					"INSERT INTO \"RESUME\"(\"resumeID\", \"applicantID\") VALUES(3, 3)",
					"INSERT INTO \"RESUME\"(\"resumeID\", \"applicantID\") VALUES(4, 4)",
					"INSERT INTO \"RESUME\"(\"resumeID\", \"applicantID\") VALUES(5, 5)",
					"INSERT INTO \"RESUME\"(\"resumeID\", \"applicantID\") VALUES(6, 6)",
					"INSERT INTO \"RESUME\"(\"resumeID\", \"applicantID\") VALUES(7, 7)",
					"INSERT INTO \"RESUME\"(\"resumeID\", \"applicantID\") VALUES(8, 8)",
					"INSERT INTO \"RESUME\"(\"resumeID\", \"applicantID\") VALUES(9, 9)",
					"INSERT INTO \"RESUME\"(\"resumeID\", \"applicantID\") VALUES(10, 10)",
					"INSERT INTO \"RESUME\"(\"resumeID\", \"applicantID\") VALUES(11, 11)",
					"INSERT INTO \"RESUME\"(\"resumeID\", \"applicantID\") VALUES(12, 12)",
					"INSERT INTO \"RESUME\"(\"resumeID\", \"applicantID\") VALUES(13, 13)",
					"INSERT INTO \"RESUME\"(\"resumeID\", \"applicantID\") VALUES(14, 14)",
					
					//Insert applicant qualifications into APPLICANT_QUALIFICATIONS table
					"INSERT INTO \"APPLICANT_QUALIFICATIONS\"(\"applicant_qualificationsID\", \"resumeID\", \"experienceLevel\", \"educationLevel\") VALUES(1, 1, 'Entry Level', 'High School Diploma')",
					"INSERT INTO \"APPLICANT_QUALIFICATIONS\"(\"applicant_qualificationsID\", \"resumeID\", \"experienceLevel\", \"educationLevel\") VALUES(2, 2, 'Entry Level', 'High School Diploma')",
					"INSERT INTO \"APPLICANT_QUALIFICATIONS\"(\"applicant_qualificationsID\", \"resumeID\", \"experienceLevel\", \"educationLevel\") VALUES(3, 3, 'Mid Level', 'Bachelor''s Degree')",
					"INSERT INTO \"APPLICANT_QUALIFICATIONS\"(\"applicant_qualificationsID\", \"resumeID\", \"experienceLevel\", \"educationLevel\") VALUES(4, 4, 'Entry Level', 'High School Diploma')",
					"INSERT INTO \"APPLICANT_QUALIFICATIONS\"(\"applicant_qualificationsID\", \"resumeID\", \"experienceLevel\", \"educationLevel\") VALUES(5, 5, 'Mid Level', 'Bachelor''s Degree')",
					"INSERT INTO \"APPLICANT_QUALIFICATIONS\"(\"applicant_qualificationsID\", \"resumeID\", \"experienceLevel\", \"educationLevel\") VALUES(6, 6, 'Mid Level', 'Master''s Degree')",
					"INSERT INTO \"APPLICANT_QUALIFICATIONS\"(\"applicant_qualificationsID\", \"resumeID\", \"experienceLevel\", \"educationLevel\") VALUES(7, 7, 'Mid Level', 'Bachelor''s Degree')",
					"INSERT INTO \"APPLICANT_QUALIFICATIONS\"(\"applicant_qualificationsID\", \"resumeID\", \"experienceLevel\", \"educationLevel\") VALUES(8, 8, 'Senior Level', 'Master''s Degree')",
					"INSERT INTO \"APPLICANT_QUALIFICATIONS\"(\"applicant_qualificationsID\", \"resumeID\", \"experienceLevel\", \"educationLevel\") VALUES(9, 9, 'Mid Level', 'Bachelor''s Degree')",
					"INSERT INTO \"APPLICANT_QUALIFICATIONS\"(\"applicant_qualificationsID\", \"resumeID\", \"experienceLevel\", \"educationLevel\") VALUES(10, 10, 'Entry Level', 'High School Diploma')",
					"INSERT INTO \"APPLICANT_QUALIFICATIONS\"(\"applicant_qualificationsID\", \"resumeID\", \"experienceLevel\", \"educationLevel\") VALUES(11, 11, 'Mid Level', 'Bachelor''s Degree')",
					"INSERT INTO \"APPLICANT_QUALIFICATIONS\"(\"applicant_qualificationsID\", \"resumeID\", \"experienceLevel\", \"educationLevel\") VALUES(12, 12, 'Senior Level', 'Ph.D.')",
					"INSERT INTO \"APPLICANT_QUALIFICATIONS\"(\"applicant_qualificationsID\", \"resumeID\", \"experienceLevel\", \"educationLevel\") VALUES(13, 13, 'Mid Level', 'Master''s Degree')",
					"INSERT INTO \"APPLICANT_QUALIFICATIONS\"(\"applicant_qualificationsID\", \"resumeID\", \"experienceLevel\", \"educationLevel\") VALUES(14, 14, 'Mid Level', 'Bachelor''s Degree')",

					//Insert applications into APPLICATION table
					"INSERT INTO \"APPLICATION\"(\"applicationID\", \"applicantID\", \"resumeID\", \"status\", \"submissionDate\") VALUES(1, 1, 1, 'Submitted', TO_DATE('092323', 'MMDDYY'))",
					"INSERT INTO \"APPLICATION\"(\"applicationID\", \"applicantID\", \"resumeID\", \"status\", \"submissionDate\") VALUES(2, 2, 2, 'Submitted', TO_DATE('092323', 'MMDDYY'))",
					"INSERT INTO \"APPLICATION\"(\"applicationID\", \"applicantID\", \"resumeID\", \"status\", \"submissionDate\") VALUES(3, 3, 3, 'Submitted', TO_DATE('092323', 'MMDDYY'))",
					"INSERT INTO \"APPLICATION\"(\"applicationID\", \"applicantID\", \"resumeID\", \"status\", \"submissionDate\") VALUES(4, 4, 4, 'Submitted', TO_DATE('092323', 'MMDDYY'))",
					"INSERT INTO \"APPLICATION\"(\"applicationID\", \"applicantID\", \"resumeID\", \"status\", \"submissionDate\") VALUES(5, 5, 5, 'Submitted', TO_DATE('092323', 'MMDDYY'))",
					"INSERT INTO \"APPLICATION\"(\"applicationID\", \"applicantID\", \"resumeID\", \"status\", \"submissionDate\") VALUES(6, 6, 6, 'Submitted', TO_DATE('092323', 'MMDDYY'))",
					"INSERT INTO \"APPLICATION\"(\"applicationID\", \"applicantID\", \"resumeID\", \"status\", \"submissionDate\") VALUES(7, 7, 7, 'Submitted', TO_DATE('092323', 'MMDDYY'))",
					"INSERT INTO \"APPLICATION\"(\"applicationID\", \"applicantID\", \"resumeID\", \"status\", \"submissionDate\") VALUES(8, 8, 8, 'Submitted', TO_DATE('092323', 'MMDDYY'))",
					"INSERT INTO \"APPLICATION\"(\"applicationID\", \"applicantID\", \"resumeID\", \"status\", \"submissionDate\") VALUES(9, 9, 9, 'Submitted', TO_DATE('092323', 'MMDDYY'))",
					"INSERT INTO \"APPLICATION\"(\"applicationID\", \"applicantID\", \"resumeID\", \"status\", \"submissionDate\") VALUES(10, 10, 10, 'Submitted', TO_DATE('092323', 'MMDDYY'))",
					"INSERT INTO \"APPLICATION\"(\"applicationID\", \"applicantID\", \"resumeID\", \"status\", \"submissionDate\") VALUES(11, 11, 11, 'Submitted', TO_DATE('092323', 'MMDDYY'))",
					"INSERT INTO \"APPLICATION\"(\"applicationID\", \"applicantID\", \"resumeID\", \"status\", \"submissionDate\") VALUES(12, 12, 12, 'Submitted', TO_DATE('092323', 'MMDDYY'))",
					"INSERT INTO \"APPLICATION\"(\"applicationID\", \"applicantID\", \"resumeID\", \"status\", \"submissionDate\") VALUES(13, 13, 13, 'Submitted', TO_DATE('092323', 'MMDDYY'))",
					"INSERT INTO \"APPLICATION\"(\"applicationID\", \"applicantID\", \"resumeID\", \"status\", \"submissionDate\") VALUES(14, 14, 14, 'Submitted', TO_DATE('092323', 'MMDDYY'))",

					//Insert HR employees into HR_EMPLOYEE table
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(1, 'John', 'Smith', '555-1234')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(2, 'Jane', 'Doe', '555-5678')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(3, 'Michael', 'Johnson', '555-9876')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(4, 'Emily', 'Brown', '555-4321')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(5, 'David', 'Wilson', '555-8765')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(6, 'Sarah', 'Anderson', '555-3456')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(7, 'Christopher', 'Lee', '555-7890')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(8, 'Emma', 'Martinez', '555-2109')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(9, 'Daniel', 'Garcia', '555-6543')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(10, 'Olivia', 'Hernandez', '555-1098')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(11, 'William', 'Lopez', '555-8761')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(12, 'Ava', 'Scott', '555-5430')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(13, 'James', 'Green', '555-2104')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(14, 'Mia', 'Adams', '555-8760')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(15, 'Benjamin', 'Nelson', '555-1230')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(16, 'Evelyn', 'Roberts', '555-5671')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(17, 'Liam', 'Cook', '555-9011')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(18, 'Charlotte', 'Bailey', '555-2340')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(19, 'Henry', 'Harris', '555-5670')",
					"INSERT INTO \"HR_EMPLOYEE\"(\"employeeID\", \"first_name\", \"last_name\", \"phoneNumber\") VALUES(20, 'Amelia', 'White', '555-9010')"
				};

				for (String populateStatement: populateStatements)
				{
					String sql = populateStatement;
					PreparedStatement statement = conn.prepareStatement(sql);
					statement.executeUpdate();
					statement.close();
				}
			}
			catch(SQLException e) {e.printStackTrace();}
		});
		
		queryTablesButton.setOnAction(event -> showQueryMenuScreen(primaryStage));
		
		logoutButton.setOnAction(event ->
		{
			try
			{
				username = "";
				password = "";
				conn.close();
				start(primaryStage);
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		});
		
		// Set the scene
        Scene scene = new Scene(vbox, 500, 500); // Draw scene with window resolution of 500x500 using master vbox
        primaryStage.setTitle("Options Menu"); // Set window title
        primaryStage.setScene(scene); // Set the scene
        primaryStage.show(); // Show the scene
	}
	
	private void showQueryMenuScreen(Stage primaryStage)
	{
		// Query 1
		Button query1Button = new Button("View Results");
		Label query1Label = new Label("Query 1: Number of job postings per employer for Netflix, group by last name");
		HBox query1 = new HBox(query1Button, query1Label);
		
		// Query 2
		Button query2Button = new Button("View Results");
		Label query2Label = new Label("Query 2: Show applicants who submitted applications for Google & number of applications ordered by first then last name");
		HBox query2 = new HBox(query2Button, query2Label);
		
		// Query 3
		Button query3Button = new Button("View Results");
		Label query3Label = new Label("Query 3: Number of jobs by each employer");
		HBox query3 = new HBox(query3Button, query3Label);
		
		// Query 4
		Button query4Button = new Button("View Results");
		Label query4Label = new Label("Query 4: Average salary for each profession");
		HBox query4 = new HBox(query4Button, query4Label);
		
		// Query 5
		Button query5Button = new Button("View Results");
		Label query5Label = new Label("Query 5: Display all jobs");
		HBox query5 = new HBox(query5Button, query5Label);
		
		// Query 6
		Button query6Button = new Button("View Results");
		Label query6Label = new Label("Query 6: Display jobs with salaries between 40k and 60k, where employer is age 36");
		HBox query6 = new HBox(query6Button, query6Label);
		
		// Query 7
		Button query7Button = new Button("View Results");
		Label query7Label = new Label("Query 7: Shows job table with addition to associated employers first & last name");
		HBox query7 = new HBox(query7Button, query7Label);
		
		// Query 8
		Button query8Button = new Button("View Results");
		Label query8Label = new Label("Query 8: Takes Employers and sorts their average salary on the job bank in descending order");
		HBox query8 = new HBox(query8Button, query8Label);
		
		// Query 9
		Button query9Button = new Button("View Results");
		Label query9Label = new Label("Query 9: Display job salary variance sorted by employer name");
		HBox query9 = new HBox(query9Button, query9Label);
		
		// Query 10
		Button query10Button = new Button("View Results");
		Label query10Label = new Label("Query 10: Shows which roads are avenues");
		HBox query10 = new HBox(query10Button, query10Label);
		
		// Query 11
		Button query11Button = new Button("View Results");
		Label query11Label = new Label("Query 11: Show all jobs that are not in-person (remote)");
		HBox query11 = new HBox(query11Button, query11Label);
		
		// Create back button
		Button backButton = new Button("Back");
		backButton.setOnAction(event -> showMenuScreen(primaryStage));
		
		VBox vbox = new VBox(5, query1, query2, query3, query4, query5, query6, query7, query8, query9, query10, query11, backButton);
		
		query1Button.setOnAction(event -> showQuery1Screen(primaryStage));
		query2Button.setOnAction(event -> showQuery2Screen(primaryStage));
		query3Button.setOnAction(event -> showQuery3Screen(primaryStage));
		query4Button.setOnAction(event -> showQuery4Screen(primaryStage));
		query5Button.setOnAction(event -> showQuery5Screen(primaryStage));
		query6Button.setOnAction(event -> showQuery6Screen(primaryStage));
		query7Button.setOnAction(event -> showQuery7Screen(primaryStage));
		query8Button.setOnAction(event -> showQuery8Screen(primaryStage));
		query9Button.setOnAction(event -> showQuery9Screen(primaryStage));
		query10Button.setOnAction(event -> showQuery10Screen(primaryStage));
		query11Button.setOnAction(event -> showQuery11Screen(primaryStage));
		
		Scene scene = new Scene(vbox, 800, 500); // Draw scene with window resolution of 500x500 using master vbox
        primaryStage.setTitle("Queries Menu"); // Set window title
        primaryStage.setScene(scene); // Set the scene
        primaryStage.show(); // Show the scene
	}
	
	private void showQuery1Screen(Stage primaryStage)
	{
		// Create table view
		TableView<query1> query1Table = new TableView<>(); // Create table to display query results

		// Define table columns
		TableColumn<query1, String> employerLastNameColumn = new TableColumn<>("Employer Last Name"); // Create employer last name column
		TableColumn<query1, Integer> jobsPostedColumn = new TableColumn<>("Jobs Posted"); // Create username column
		query1Table.getColumns().addAll(employerLastNameColumn, jobsPostedColumn); // Add columns to table

		employerLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("employerLastName")); // Bind employer last name
		jobsPostedColumn.setCellValueFactory(new PropertyValueFactory<>("jobsPosted")); // Bind number of jobs posted

		// Fetch data from the database
		try
		{
			String query = "SELECT e.\"last_name\" AS \"Employer Last Name\", COUNT(j.\"jobID\") AS \"Jobs Posted\" FROM \"EMPLOYER\" e JOIN \"JOB\" j ON e.\"employerID\" = j.\"employerID\" WHERE j.\"company\" = 'Netflix' GROUP BY e.\"last_name\""; // Define SQL query
			PreparedStatement preparedStatement = conn.prepareStatement(query); // Prepare SQL query for execution
			ResultSet resultSet = preparedStatement.executeQuery(); // Execute SQL query

			while(resultSet.next()) // Loop through results (rows)
			{
				String employerLastName = resultSet.getString("Employer Last Name"); // Column name as shown in SQLDeveloper
				int jobsPosted = resultSet.getInt("Jobs Posted");

				query1Table.getItems().add(new query1(employerLastName, jobsPosted)); // Add rows to results table
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		// Create back button
		Button backButton = new Button("Back");
		backButton.setOnAction(event -> showQueryMenuScreen(primaryStage));
		
		VBox vbox = new VBox(5, query1Table, backButton);
		
		//Set the scene
		Scene scene = new Scene(vbox, 500, 500); //Draw scene with window resolution of 500x500 using master vbox
		primaryStage.setTitle("Query 1 Results"); //Set window title
		primaryStage.setScene(scene); //Set the scene
		primaryStage.show(); //Show the scene
	}
	
	private void showQuery2Screen(Stage primaryStage)
	{
		// Create table view
		TableView<query2> query2Table = new TableView<>(); // Create table to display query results

		// Define table columns
		TableColumn<query2, String> applicantNameColumn = new TableColumn<>("Applicant Name"); //Create applicant name column
		TableColumn<query2, Integer> numberOfApplicationsColumn = new TableColumn<>("Number of Applications"); // Create username column
		query2Table.getColumns().addAll(applicantNameColumn, numberOfApplicationsColumn); // Add columns to table

		applicantNameColumn.setCellValueFactory(new PropertyValueFactory<>("applicantName")); // Bind applicant name
		numberOfApplicationsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfApplications")); // Bind number of applications

		// Fetch data from the database
		try
		{
			String query = "SELECT A.\"first_name\" || ' ' || A.\"last_name\" AS \"Applicant Name\", COUNT(*) AS \"Number of Applications\" FROM \"APPLICANT\" A JOIN \"APPLICATION\" AP ON A.\"applicantID\" = AP.\"applicantID\" JOIN \"JOB\" J ON AP.\"resumeID\" = J.\"jobID\" WHERE EXISTS (SELECT 1 FROM \"JOB\" J2 WHERE J2.\"company\" = 'Google' AND J2.\"jobID\" = J.\"jobID\") GROUP BY A.\"first_name\", A.\"last_name\" HAVING COUNT(*) > 0 ORDER BY \"Number of Applications\" DESC"; // Define SQL query
			PreparedStatement preparedStatement = conn.prepareStatement(query); // Prepare SQL query for execution
			ResultSet resultSet = preparedStatement.executeQuery(); // Execute SQL query

			while(resultSet.next()) // Loop through results (rows)
			{
				String applicantNameName = resultSet.getString("Applicant Name"); // Column name as shown in SQLDeveloper
				int numberOfApplications = resultSet.getInt("Number of Applications");

				query2Table.getItems().add(new query2(applicantNameName, numberOfApplications)); // Add rows to results table
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		// Create back button
		Button backButton = new Button("Back");
		backButton.setOnAction(event -> showQueryMenuScreen(primaryStage));
		
		VBox vbox = new VBox(5, query2Table, backButton);
		
		//Set the scene
		Scene scene = new Scene(vbox, 500, 500); // Draw scene with window resolution of 500x500 using master vbox
		primaryStage.setTitle("Query 2 Results"); // Set window title
		primaryStage.setScene(scene); // Set the scene
		primaryStage.show(); // Show the scene
	}
	
	private void showQuery3Screen(Stage primaryStage)
	{
		// Create table view
		TableView<query3> query3Table = new TableView<>(); // Create table to display query results

		// Define table columns
		TableColumn<query3, Integer> employerIDColumn = new TableColumn<>("Employer ID"); //Create applicant name column
		TableColumn<query3, Integer> jobCountColumn = new TableColumn<>("Job Count"); // Create username column
		query3Table.getColumns().addAll(employerIDColumn, jobCountColumn); // Add columns to table

		employerIDColumn.setCellValueFactory(new PropertyValueFactory<>("employerID")); // Bind employer ID
		jobCountColumn.setCellValueFactory(new PropertyValueFactory<>("jobCount")); // Bind job count

		// Fetch data from the database
		try
		{
			String query = "SELECT E.\"employerID\" AS \"Employer ID\", COUNT(J.\"jobID\") AS \"Job Count\" FROM \"EMPLOYER\" E LEFT JOIN \"JOB\" J ON E.\"employerID\" = J.\"employerID\" GROUP BY E.\"employerID\" ORDER BY \"Job Count\" DESC"; // Define SQL query
			PreparedStatement preparedStatement = conn.prepareStatement(query); // Prepare SQL query for execution
			ResultSet resultSet = preparedStatement.executeQuery(); // Execute SQL query

			while(resultSet.next()) // Loop through results (rows)
			{
				int employerID = resultSet.getInt("Employer ID"); // Column name as shown in SQLDeveloper
				int jobCount = resultSet.getInt("Job Count");

				query3Table.getItems().add(new query3(employerID, jobCount)); // Add rows to results table
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		// Create back button
		Button backButton = new Button("Back");
		backButton.setOnAction(event -> showQueryMenuScreen(primaryStage));
		
		VBox vbox = new VBox(5, query3Table, backButton);
		
		//Set the scene
		Scene scene = new Scene(vbox, 500, 500); // Draw scene with window resolution of 500x500 using master vbox
		primaryStage.setTitle("Query 3 Results"); // Set window title
		primaryStage.setScene(scene); // Set the scene
		primaryStage.show(); // Show the scene
	}
	
	private void showQuery4Screen(Stage primaryStage)
	{
		// Create table view
		TableView<query4> query4Table = new TableView<>(); // Create table to display query results

		// Define table columns
		TableColumn<query4, String> jobProfessionColumn = new TableColumn<>("Job Profession"); //Create job profession column
		TableColumn<query4, Integer> averageSalaryColumn = new TableColumn<>("Average Salary"); // Create average salary column
		query4Table.getColumns().addAll(jobProfessionColumn, averageSalaryColumn); // Add columns to table

		jobProfessionColumn.setCellValueFactory(new PropertyValueFactory<>("jobProfession")); // Bind job profession
		averageSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("averageSalary")); // Bind average salary

		// Fetch data from the database
		try
		{
			String query = "SELECT \"profession\" AS \"Job Profession\", AVG(\"salary\") AS \"Average Salary\" FROM \"JOB\" GROUP BY \"profession\" HAVING AVG(\"salary\") > 50000 ORDER BY \"Average Salary\" DESC"; // Define SQL query
			PreparedStatement preparedStatement = conn.prepareStatement(query); // Prepare SQL query for execution
			ResultSet resultSet = preparedStatement.executeQuery(); // Execute SQL query

			while(resultSet.next()) // Loop through results (rows)
			{
				String jobProfession = resultSet.getString("Job Profession"); // Column name as shown in SQLDeveloper
				int averageSalary = resultSet.getInt("Average Salary");

				query4Table.getItems().add(new query4(jobProfession, averageSalary)); // Add rows to results table
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		// Create back button
		Button backButton = new Button("Back");
		backButton.setOnAction(event -> showQueryMenuScreen(primaryStage));
		
		VBox vbox = new VBox(5, query4Table, backButton);
		
		//Set the scene
		Scene scene = new Scene(vbox, 500, 500); // Draw scene with window resolution of 500x500 using master vbox
		primaryStage.setTitle("Query 4 Results"); // Set window title
		primaryStage.setScene(scene); // Set the scene
		primaryStage.show(); // Show the scene
	}
	
	private void showQuery5Screen(Stage primaryStage)
	{
		// Create table view
		TableView<query5> query5Table = new TableView<>(); // Create table to display query results

		// Define table columns
		TableColumn<query5, Integer> jobIDColumn = new TableColumn<>("jobID"); // Create job ID column
		TableColumn<query5, String> companyColumn = new TableColumn<>("company"); // Create company column
		TableColumn<query5, String> professionColumn = new TableColumn<>("profession"); // Create profession column
		TableColumn<query5, Integer> salaryColumn = new TableColumn<>("salary"); // Create salary column
		TableColumn<query5, String> date_postedColumn = new TableColumn<>("date_posted"); // Create date posted column
		TableColumn<query5, String> deadline_dateColumn = new TableColumn<>("deadline_date"); // Create deadline column
		TableColumn<query5, String> durationColumn = new TableColumn<>("duration"); // Create duration column
		TableColumn<query5, String> arrangementColumn = new TableColumn<>("arrangement"); // Create arrangement column
		TableColumn<query5, String> employer_first_nameColumn = new TableColumn<>("employer_first_name"); // Create employer first name column
		TableColumn<query5, String> employer_last_nameColumn = new TableColumn<>("employer_last_name"); // Create employer last name column
		TableColumn<query5, Integer> location_numberColumn = new TableColumn<>("location_number"); // Create location number column
		TableColumn<query5, String> location_streetColumn = new TableColumn<>("location_street"); // Create location street column
		TableColumn<query5, Integer> location_zip_codeColumn = new TableColumn<>("location_zip_code"); // Create location zip code column
		TableColumn<query5, String> job_experience_levelColumn = new TableColumn<>("job_experience_level"); // Create job experience level column
		TableColumn<query5, String> job_education_levelColumn = new TableColumn<>("job_education_level"); // Create job education level column
		query5Table.getColumns().addAll(jobIDColumn, companyColumn, professionColumn, salaryColumn, date_postedColumn, deadline_dateColumn, durationColumn, arrangementColumn, employer_first_nameColumn, employer_last_nameColumn, location_numberColumn, location_streetColumn, location_zip_codeColumn, job_experience_levelColumn, job_education_levelColumn); // Add columns to table

		jobIDColumn.setCellValueFactory(new PropertyValueFactory<>("jobID")); // Bind job ID
		companyColumn.setCellValueFactory(new PropertyValueFactory<>("company")); // Bind company
		professionColumn.setCellValueFactory(new PropertyValueFactory<>("profession")); // Bind profession
		salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary")); // Bind salary
		date_postedColumn.setCellValueFactory(new PropertyValueFactory<>("date_posted")); // Bind date posted
		deadline_dateColumn.setCellValueFactory(new PropertyValueFactory<>("deadline_date")); // Bind deadline date
		durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration")); // Bind duration
		arrangementColumn.setCellValueFactory(new PropertyValueFactory<>("arrangement")); // Bind arrangement
		employer_first_nameColumn.setCellValueFactory(new PropertyValueFactory<>("employer_first_name")); // Bind employer first name
		employer_last_nameColumn.setCellValueFactory(new PropertyValueFactory<>("employer_last_name")); // Bind employer last name
		location_numberColumn.setCellValueFactory(new PropertyValueFactory<>("location_number")); // Bind location number
		location_streetColumn.setCellValueFactory(new PropertyValueFactory<>("location_street")); // Bind location street
		location_zip_codeColumn.setCellValueFactory(new PropertyValueFactory<>("location_zip_code")); // Bind location zip code
		job_experience_levelColumn.setCellValueFactory(new PropertyValueFactory<>("job_experience_level")); // Bind job experience level
		job_education_levelColumn.setCellValueFactory(new PropertyValueFactory<>("job_education_level")); // Bind job education level

		// Fetch data from the database
		try
		{
			String query = "SELECT J.\"jobID\", J.\"company\", J.\"profession\", J.\"salary\", J.\"date_posted\", J.\"deadline_date\", J.\"duration\", J.\"arrangement\", E.\"first_name\" AS \"employer_first_name\", E.\"last_name\" AS \"employer_last_name\", L.\"number\" AS \"location_number\", L.\"street\" AS \"location_street\", L.\"zip_code\" AS \"location_zip_code\", JQ.\"experienceLevel\" AS \"job_experience_level\", JQ.\"educationLevel\" AS \"job_education_level\" FROM \"JOB\" J INNER JOIN \"EMPLOYER\" E ON J.\"employerID\" = E.\"employerID\" LEFT JOIN \"LOCATION\" L ON J.\"jobID\" = L.\"jobID\" LEFT JOIN \"JOB_QUALIFICATIONS\" JQ ON J.\"jobID\" = JQ.\"jobID\""; // Define SQL query
			PreparedStatement preparedStatement = conn.prepareStatement(query); // Prepare SQL query for execution
			ResultSet resultSet = preparedStatement.executeQuery(); // Execute SQL query

			while(resultSet.next()) // Loop through results (rows)
			{
				int jobID = resultSet.getInt("jobID"); // Column name as shown in SQLDeveloper
				String company = resultSet.getString("company");
				String profession = resultSet.getString("profession");
				int salary = resultSet.getInt("salary");
				String date_posted = resultSet.getString("date_posted");
				String deadline_date = resultSet.getString("deadline_date");
				String duration = resultSet.getString("duration");
				String arrangement = resultSet.getString("arrangement");
				String employer_first_name = resultSet.getString("employer_first_name");
				String employer_last_name = resultSet.getString("employer_last_name");
				int location_number = resultSet.getInt("location_number");
				String location_street = resultSet.getString("location_street");
				int location_zip_code = resultSet.getInt("location_zip_code");
				String job_experience_level = resultSet.getString("job_experience_level");
				String job_education_level = resultSet.getString("job_education_level");

				query5Table.getItems().add(new query5(jobID, company, profession, salary, date_posted, deadline_date, duration, arrangement, employer_first_name, employer_last_name, location_number, location_street, location_zip_code, job_experience_level, job_education_level)); // Add rows to results table
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		// Create back button
		Button backButton = new Button("Back");
		backButton.setOnAction(event -> showQueryMenuScreen(primaryStage));
		
		VBox vbox = new VBox(5, query5Table, backButton);
		
		//Set the scene
		Scene scene = new Scene(vbox, 500, 500); // Draw scene with window resolution of 500x500 using master vbox
		primaryStage.setTitle("Query 5 Results"); // Set window title
		primaryStage.setScene(scene); // Set the scene
		primaryStage.show(); // Show the scene
	}
	
	private void showQuery6Screen(Stage primaryStage)
	{
		// Create table view
		TableView<query6> query6Table = new TableView<>(); // Create table to display query results

		// Define table columns
		TableColumn<query6, Integer> jobIDColumn = new TableColumn<>("jobID"); //Create job ID column
		TableColumn<query6, String> companyColumn = new TableColumn<>("company"); // Create company column
		TableColumn<query6, String> professionColumn = new TableColumn<>("profession"); // Create profession column
		TableColumn<query6, Integer> salaryColumn = new TableColumn<>("salary"); //Create salary column
		query6Table.getColumns().addAll(jobIDColumn, companyColumn, professionColumn, salaryColumn); // Add columns to table

		jobIDColumn.setCellValueFactory(new PropertyValueFactory<>("jobID")); // Bind job ID
		companyColumn.setCellValueFactory(new PropertyValueFactory<>("company")); // Bind company
		professionColumn.setCellValueFactory(new PropertyValueFactory<>("profession")); // Bind profession
		salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary")); // Bind salary

		// Fetch data from the database
		try
		{
			String query = "SELECT J.\"jobID\", J.\"company\", J.\"profession\", J.\"salary\" FROM \"JOB\" J INNER JOIN \"EMPLOYER\" E ON J.\"employerID\" = E.\"employerID\" WHERE J.\"salary\" BETWEEN 40000 AND 60000 AND E.\"age\" = 36 UNION SELECT J.\"jobID\", J.\"company\", J.\"profession\", J.\"salary\" FROM \"JOB\" J INNER JOIN \"EMPLOYER\" E ON J.\"employerID\" = E.\"employerID\" WHERE J.\"salary\" BETWEEN 40000 AND 60000 AND E.\"age\" = 36"; // Define SQL query
			PreparedStatement preparedStatement = conn.prepareStatement(query); // Prepare SQL query for execution
			ResultSet resultSet = preparedStatement.executeQuery(); // Execute SQL query

			while(resultSet.next()) // Loop through results (rows)
			{
				int jobID = resultSet.getInt("jobID"); // Column name as shown in SQLDeveloper
				String company = resultSet.getString("company");
				String profession = resultSet.getString("profession");
				int salary = resultSet.getInt("salary");

				query6Table.getItems().add(new query6(jobID, company, profession, salary)); // Add rows to results table
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		// Create back button
		Button backButton = new Button("Back");
		backButton.setOnAction(event -> showQueryMenuScreen(primaryStage));
		
		VBox vbox = new VBox(5, query6Table, backButton);
		
		//Set the scene
		Scene scene = new Scene(vbox, 500, 500); // Draw scene with window resolution of 500x500 using master vbox
		primaryStage.setTitle("Query 6 Results"); // Set window title
		primaryStage.setScene(scene); // Set the scene
		primaryStage.show(); // Show the scene
	}
	
	private void showQuery7Screen(Stage primaryStage)
	{
		// Create table view
		TableView<query7> query7Table = new TableView<>(); // Create table to display query results

		// Define table columns
		TableColumn<query7, Integer> jobIDColumn = new TableColumn<>("jobID"); //Create job ID column
		TableColumn<query7, String> companyColumn = new TableColumn<>("Company"); // Create company column
		TableColumn<query7, String> professionColumn = new TableColumn<>("Profession"); // Create profession column
		TableColumn<query7, Integer> salaryColumn = new TableColumn<>("Salary"); //Create salary column
		TableColumn<query7, String> datePostedColumn = new TableColumn<>("Date Posted"); //Create date posted column
		TableColumn<query7, String> deadlineDateColumn = new TableColumn<>("Deadline Date"); //Create deadline date column
		TableColumn<query7, String> durationColumn = new TableColumn<>("Duration"); //Create duration column
		TableColumn<query7, String> arrangementColumn = new TableColumn<>("Arrangement"); //Create arrangement column
		TableColumn<query7, String> employerFirstNameColumn = new TableColumn<>("Employer First Name"); //Create employer first name column
		TableColumn<query7, String> employerLastNameColumn = new TableColumn<>("Employer Last Name"); //Create employer last name column
		query7Table.getColumns().addAll(jobIDColumn, companyColumn, professionColumn, salaryColumn, datePostedColumn, deadlineDateColumn, durationColumn, arrangementColumn, employerFirstNameColumn, employerLastNameColumn); // Add columns to table

		jobIDColumn.setCellValueFactory(new PropertyValueFactory<>("jobID")); // Bind job ID
		companyColumn.setCellValueFactory(new PropertyValueFactory<>("company")); // Bind company
		professionColumn.setCellValueFactory(new PropertyValueFactory<>("profession")); // Bind profession
		salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary")); // Bind salary
		datePostedColumn.setCellValueFactory(new PropertyValueFactory<>("datePosted")); // Bind date posted
		deadlineDateColumn.setCellValueFactory(new PropertyValueFactory<>("deadlineDate")); // Bind deadline date
		durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration")); // Bind duration
		arrangementColumn.setCellValueFactory(new PropertyValueFactory<>("arrangement")); // Bind arrangement
		employerFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("employerFirstName")); // Bind employer first name column
		employerLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("employerLastName")); // Bind employer last name column

		// Fetch data from the database
		try
		{
			String query = "SELECT J.\"jobID\", J.\"company\" AS \"Company\", J.\"profession\" AS \"Profession\", J.\"salary\" AS \"Salary\", J.\"date_posted\" AS \"Date Posted\", J.\"deadline_date\" AS \"Deadline Date\", J.\"duration\" AS \"Duration\", J.\"arrangement\" AS \"Arrangement\", E.\"first_name\" AS \"Employer First Name\", E.\"last_name\" AS \"Employer Last Name\" FROM \"JOB\" J INNER JOIN \"EMPLOYER\" E ON J.\"employerID\" = E.\"employerID\""; // Define SQL query
			PreparedStatement preparedStatement = conn.prepareStatement(query); // Prepare SQL query for execution
			ResultSet resultSet = preparedStatement.executeQuery(); // Execute SQL query

			while(resultSet.next()) // Loop through results (rows)
			{
				int jobID = resultSet.getInt("jobID"); // Column name as shown in SQLDeveloper
				String company = resultSet.getString("Company");
				String profession = resultSet.getString("Profession");
				int salary = resultSet.getInt("Salary");
				String datePosted = resultSet.getString("Date Posted");
				String deadlineDate = resultSet.getString("Deadline Date");
				String duration = resultSet.getString("Duration");
				String arrangement = resultSet.getString("Arrangement");
				String employerFirstName = resultSet.getString("Employer First Name");
				String employerLastName = resultSet.getString("Employer Last Name");

				query7Table.getItems().add(new query7(jobID, company, profession, salary, datePosted, deadlineDate, duration, arrangement, employerFirstName, employerLastName)); // Add rows to results table
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		// Create back button
		Button backButton = new Button("Back");
		backButton.setOnAction(event -> showQueryMenuScreen(primaryStage));
		
		VBox vbox = new VBox(5, query7Table, backButton);
		
		//Set the scene
		Scene scene = new Scene(vbox, 500, 500); // Draw scene with window resolution of 500x500 using master vbox
		primaryStage.setTitle("Query 7 Results"); // Set window title
		primaryStage.setScene(scene); // Set the scene
		primaryStage.show(); // Show the scene
	}
	
	private void showQuery8Screen(Stage primaryStage)
	{
		// Create table view
		TableView<query8> query8Table = new TableView<>(); // Create table to display query results

		// Define table columns
		TableColumn<query8, Integer> employerIDColumn = new TableColumn<>("EmployerID"); //Create employer ID column
		TableColumn<query8, String> firstNameColumn = new TableColumn<>("First Name"); // Create first name column
		TableColumn<query8, String> lastNameColumn = new TableColumn<>("Last Name"); // Create last name column
		TableColumn<query8, Integer> averageSalaryColumn = new TableColumn<>("Average Salary"); //Create average salary column
		query8Table.getColumns().addAll(employerIDColumn, firstNameColumn, lastNameColumn, averageSalaryColumn); // Add columns to table

		employerIDColumn.setCellValueFactory(new PropertyValueFactory<>("employerID")); // Bind employer ID
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName")); // Bind first name
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName")); // Bind last name
		averageSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("averageSalary")); // Bind average salary

		// Fetch data from the database
		try
		{
			String query = "SELECT E.\"employerID\" AS \"EmployerID\", E.\"first_name\" AS \"First Name\", E.\"last_name\" AS \"Last Name\", AVG(J.\"salary\") AS \"Average Salary\" FROM \"EMPLOYER\" E INNER JOIN \"JOB\" J ON E.\"employerID\" = J.\"employerID\" GROUP BY E.\"employerID\", E.\"first_name\", E.\"last_name\" HAVING COUNT(J.\"jobID\") > 0 ORDER BY \"Average Salary\" DESC"; // Define SQL query
			PreparedStatement preparedStatement = conn.prepareStatement(query); // Prepare SQL query for execution
			ResultSet resultSet = preparedStatement.executeQuery(); // Execute SQL query

			while(resultSet.next()) // Loop through results (rows)
			{
				int employerID = resultSet.getInt("EmployerID"); // Column name as shown in SQLDeveloper
				String firstName = resultSet.getString("First Name");
				String lastName = resultSet.getString("Last Name");
				int averageSalary = resultSet.getInt("Average Salary");

				query8Table.getItems().add(new query8(employerID, firstName, lastName, averageSalary)); // Add rows to results table
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		// Create back button
		Button backButton = new Button("Back");
		backButton.setOnAction(event -> showQueryMenuScreen(primaryStage));
		
		VBox vbox = new VBox(5, query8Table, backButton);
		
		//Set the scene
		Scene scene = new Scene(vbox, 500, 500); // Draw scene with window resolution of 500x500 using master vbox
		primaryStage.setTitle("Query 8 Results"); // Set window title
		primaryStage.setScene(scene); // Set the scene
		primaryStage.show(); // Show the scene
	}
	
	private void showQuery9Screen(Stage primaryStage)
	{
		// Create table view
		TableView<query9> query9Table = new TableView<>(); // Create table to display query results

		// Define table columns
		TableColumn<query9, Integer> employerIDColumn = new TableColumn<>("employerID"); //Create employer ID column
		TableColumn<query9, String> employerFirstNameColumn = new TableColumn<>("employer_first_name"); // Create employer first name column
		TableColumn<query9, String> employerLastNameColumn = new TableColumn<>("employer_last_name"); // Create employer last name column
		TableColumn<query9, Integer> averageSalaryColumn = new TableColumn<>("average_salary"); //Create average salary column
		TableColumn<query9, Integer> salaryVarianceColumn = new TableColumn<>("salary_variance"); //Create salary variance column
		query9Table.getColumns().addAll(employerIDColumn, employerFirstNameColumn, employerLastNameColumn, averageSalaryColumn, salaryVarianceColumn); // Add columns to table

		employerIDColumn.setCellValueFactory(new PropertyValueFactory<>("employerID")); // Bind employer ID
		employerFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("employerFirstName")); // Bind employer first name
		employerLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("employerLastName")); // Bind employer last name
		averageSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("averageSalary")); // Bind average salary
		salaryVarianceColumn.setCellValueFactory(new PropertyValueFactory<>("salaryVariance")); // Bind salary variance

		// Fetch data from the database
		try
		{
			String query = "SELECT E.\"employerID\", E.\"first_name\" AS \"employer_first_name\", E.\"last_name\" AS \"employer_last_name\", AVG(J.\"salary\") AS \"average_salary\", VARIANCE(J.\"salary\") AS \"salary_variance\" FROM \"EMPLOYER\" E INNER JOIN \"JOB\" J ON E.\"employerID\" = J.\"employerID\" GROUP BY E.\"employerID\", E.\"first_name\", E.\"last_name\" ORDER BY \"employer_first_name\", \"employer_last_name\""; // Define SQL query
			PreparedStatement preparedStatement = conn.prepareStatement(query); // Prepare SQL query for execution
			ResultSet resultSet = preparedStatement.executeQuery(); // Execute SQL query

			while(resultSet.next()) // Loop through results (rows)
			{
				int employerID = resultSet.getInt("employerID"); // Column name as shown in SQLDeveloper
				String employerFirstName = resultSet.getString("employer_first_name");
				String employerLastName = resultSet.getString("employer_last_name");
				int averageSalary = resultSet.getInt("average_salary");
				int salaryVariance = resultSet.getInt("salary_variance");

				query9Table.getItems().add(new query9(employerID, employerFirstName, employerLastName, averageSalary, salaryVariance)); // Add rows to results table
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		// Create back button
		Button backButton = new Button("Back");
		backButton.setOnAction(event -> showQueryMenuScreen(primaryStage));
		
		VBox vbox = new VBox(5, query9Table, backButton);
		
		//Set the scene
		Scene scene = new Scene(vbox, 500, 500); // Draw scene with window resolution of 500x500 using master vbox
		primaryStage.setTitle("Query 9 Results"); // Set window title
		primaryStage.setScene(scene); // Set the scene
		primaryStage.show(); // Show the scene
	}
	
	private void showQuery10Screen(Stage primaryStage)
	{
		// Create table view
		TableView<query10> query10Table = new TableView<>(); // Create table to display query results

		// Define table columns
		TableColumn<query10, Integer> locationIDColumn = new TableColumn<>("locationID"); //Create location ID column
		TableColumn<query10, Integer> jobIDColumn = new TableColumn<>("jobID"); //Create job ID column
		TableColumn<query10, Integer> numberColumn = new TableColumn<>("number"); //Create number column
		TableColumn<query10, String> streetColumn = new TableColumn<>("street"); //Create street column
		TableColumn<query10, Integer> zipCodeColumn = new TableColumn<>("zip_code"); //Create zip code column
		query10Table.getColumns().addAll(locationIDColumn, jobIDColumn, numberColumn, streetColumn, zipCodeColumn); // Add columns to table

		locationIDColumn.setCellValueFactory(new PropertyValueFactory<>("locationID")); // Bind location ID
		jobIDColumn.setCellValueFactory(new PropertyValueFactory<>("jobID")); // Bind job ID
		numberColumn.setCellValueFactory(new PropertyValueFactory<>("number")); // Bind number
		streetColumn.setCellValueFactory(new PropertyValueFactory<>("street")); // Bind street
		zipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("zipCode")); // Bind zip code

		// Fetch data from the database
		try
		{
			String query = "SELECT * FROM \"LOCATION\" WHERE \"street\" LIKE '%Ave%' ORDER BY \"street\" ASC"; // Define SQL query
			PreparedStatement preparedStatement = conn.prepareStatement(query); // Prepare SQL query for execution
			ResultSet resultSet = preparedStatement.executeQuery(); // Execute SQL query

			while(resultSet.next()) // Loop through results (rows)
			{
				int locationID = resultSet.getInt("locationID"); // Column name as shown in SQLDeveloper
				int jobID = resultSet.getInt("jobID");
				int number = resultSet.getInt("number");
				String street = resultSet.getString("street");
				int zipCode = resultSet.getInt("zip_code");

				query10Table.getItems().add(new query10(locationID, jobID, number, street, zipCode)); // Add rows to results table
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		// Create back button
		Button backButton = new Button("Back");
		backButton.setOnAction(event -> showQueryMenuScreen(primaryStage));
		
		VBox vbox = new VBox(5, query10Table, backButton);
		
		//Set the scene
		Scene scene = new Scene(vbox, 500, 500); // Draw scene with window resolution of 500x500 using master vbox
		primaryStage.setTitle("Query 10 Results"); // Set window title
		primaryStage.setScene(scene); // Set the scene
		primaryStage.show(); // Show the scene
	}
	
	private void showQuery11Screen(Stage primaryStage)
	{
		// Create table view
		TableView<query11> query11Table = new TableView<>(); // Create table to display query results

		// Define table columns
		TableColumn<query11, Integer> jobIDColumn = new TableColumn<>("jobID"); //Create job ID column
		TableColumn<query11, Integer> employerIDColumn = new TableColumn<>("employerID"); //Create employer ID column
		TableColumn<query11, String> companyColumn = new TableColumn<>("company"); //Create company column
		TableColumn<query11, String> professionColumn = new TableColumn<>("profession"); //Create profession column
		TableColumn<query11, Integer> salaryColumn = new TableColumn<>("salary"); //Create salary column
		TableColumn<query11, String> datePostedColumn = new TableColumn<>("date_posted"); //Create date posted column
		TableColumn<query11, String> deadlineDateColumn = new TableColumn<>("deadline_date"); //Create deadline_date column
		TableColumn<query11, String> durationColumn = new TableColumn<>("duration"); //Create duration column
		TableColumn<query11, String> arrangementColumn = new TableColumn<>("arrangement"); //Create arrangement column
		query11Table.getColumns().addAll(jobIDColumn, employerIDColumn, companyColumn, professionColumn, salaryColumn, datePostedColumn, deadlineDateColumn, durationColumn, arrangementColumn); // Add columns to table

		jobIDColumn.setCellValueFactory(new PropertyValueFactory<>("jobID")); // Bind job ID
		employerIDColumn.setCellValueFactory(new PropertyValueFactory<>("employerID")); // Bind employer ID
		companyColumn.setCellValueFactory(new PropertyValueFactory<>("company")); // Bind company
		professionColumn.setCellValueFactory(new PropertyValueFactory<>("profession")); // Bind profession
		salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary")); // Bind salary
		datePostedColumn.setCellValueFactory(new PropertyValueFactory<>("datePosted")); // Bind date posted
		deadlineDateColumn.setCellValueFactory(new PropertyValueFactory<>("deadlineDate")); // Bind deadline date
		durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration")); // Bind duration
		arrangementColumn.setCellValueFactory(new PropertyValueFactory<>("arrangement")); // Bind arrangement

		// Fetch data from the database
		try
		{
			String query = "(SELECT * FROM \"JOB\") MINUS (SELECT j.* FROM \"JOB\" j WHERE \"arrangement\" = 'In Person')"; // Define SQL query
			PreparedStatement preparedStatement = conn.prepareStatement(query); // Prepare SQL query for execution
			ResultSet resultSet = preparedStatement.executeQuery(); // Execute SQL query

			while(resultSet.next()) // Loop through results (rows)
			{
				int jobID = resultSet.getInt("jobID"); // Column name as shown in SQLDeveloper
				int employerID = resultSet.getInt("employerID");
				String company = resultSet.getString("company");
				String profession = resultSet.getString("profession");
				int salary = resultSet.getInt("salary");
				String datePosted = resultSet.getString("date_posted");
				String deadlineDate = resultSet.getString("deadline_date");
				String duration = resultSet.getString("duration");
				String arrangement = resultSet.getString("arrangement");

				query11Table.getItems().add(new query11(jobID, employerID, company, profession, salary, datePosted, deadlineDate, duration, arrangement)); // Add rows to results table
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		// Create back button
		Button backButton = new Button("Back");
		backButton.setOnAction(event -> showQueryMenuScreen(primaryStage));
		
		VBox vbox = new VBox(5, query11Table, backButton);
		
		//Set the scene
		Scene scene = new Scene(vbox, 500, 500); // Draw scene with window resolution of 500x500 using master vbox
		primaryStage.setTitle("Query 11 Results"); // Set window title
		primaryStage.setScene(scene); // Set the scene
		primaryStage.show(); // Show the scene
	}
	
	public static void main(String[] args)
	{
		launch(args); // Launch GUI
	}
}