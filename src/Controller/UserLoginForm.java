package Controller;

import DAO.DBAppointments;
import DAO.DBUserInfo;
import Model.Appointments;
import Model.UserInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;

/** This is the UserLoginForm class that works with the UserLoginForm FXML to get the user's login information. */
public class UserLoginForm implements Initializable {
    Stage stage;
    Parent scene;

    /** When the Exit button is pressed, the application will close. */
    @FXML
    void onActionExit(ActionEvent event) {
        System.exit(0);
    }
    @FXML
    private Button loginBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private TextField passwordTxt;
    @FXML
    private TextField userIdTxt;
    @FXML
    private Label passwordLbl;
    @FXML
    private Label welcomeLbl;
    @FXML
    private Label userIdLbl;
    @FXML
    private Label userLocationLbl;
    @FXML
    private Label locationLbl;
    ResourceBundle rb;

    /** When the user inputs the correct credentials and presses Login, they will be able to view Customer Appointments and Records.
     The screen will also display the user's current location and will also be in the desktop's default language.
     When the user presses the Login button (whether successful or failed), the user activity will be documented in the login_activity.txt file.
     It will include the Username, the attempt time and date in Eastern time, and whether or not it was a successful login.
     I created alerts if there are no upcoming appointments within 15 minutes of the user's login time or if there is an appointment
     coming up within 15 minutes. If an appointment is coming up within 15 minutes of login, a warning will display how many minutes are until the appointment
     and the appointment ID and the appointment date and time.
     @param event logs user into the application */
    @FXML
    void onActionLogin(ActionEvent event) throws IOException {

        String filename = "src/Files/login_activity.txt";
        FileWriter fwriter = new FileWriter(filename, true);
        PrintWriter output = new PrintWriter(fwriter);
        ZonedDateTime currentZDT = ZonedDateTime.now();
        ZoneId easternTime = ZoneId.of("US/Eastern");
        ZonedDateTime easternLoginActivity = currentZDT.withZoneSameInstant(easternTime);

        String userName = userIdTxt.getText();
        String password = passwordTxt.getText();
        boolean upcoming = false;

        for (UserInfo users : DBUserInfo.getAllUserInfo()) {
            String validUserName = users.getUserName();
            String validPassword = users.getUserPassword();
            if (userName.equals(validUserName) && password.equals(validPassword)) {
                output.println("Login Successful! - " + "Username: " + userName + " |  Login Attempt: " + easternLoginActivity);
                output.close();
                for (Appointments appts : DBAppointments.getAllAppts()) {
                    String apptStartTime = appts.getApptStart();
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    int apptId = appts.getApptId();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime apptDateTime = LocalDateTime.parse(apptStartTime, format);
                    long timeDifference = ChronoUnit.MINUTES.between(apptDateTime, currentDateTime);
                    long differenceResults = (timeDifference + -1) * -1;

                    if (differenceResults >= 0 && differenceResults <= 15) {
                        Alert upcomingAppt = new Alert(Alert.AlertType.WARNING);
                        upcomingAppt.setContentText("Appointment coming up in " + differenceResults + " minutes!" + "\n\nAppointment ID: " + apptId +
                                "\nAppointment Date & Time: " + apptStartTime);
                        upcomingAppt.showAndWait();
                        upcoming = true;
                        }
                    }
                if (!upcoming){
                    Alert noUpcomingAppt = new Alert(Alert.AlertType.INFORMATION);
                    noUpcomingAppt.setContentText("There are no upcoming appointments within 15 minutes of logging in.");
                    noUpcomingAppt.showAndWait();
                }
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/View/SelectOptionForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
                return;
                    }
            else{
                output.println("Login Failed! - " + "Username: " + userName + " |  Login Attempt: " + easternLoginActivity);
                output.close();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(rb.getString("ErrorMsg"));
                alert.showAndWait();
                return;
            }
        }

    }

    /** This will check the computer's default language and display the login page accordingly. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        System.out.println(Locale.getDefault());

        welcomeLbl.setText(rb.getString("Title"));
        userIdLbl.setText(rb.getString("UserID"));
        passwordLbl.setText(rb.getString("Password"));
        userLocationLbl.setText(rb.getString("Location"));
        loginBtn.setText(rb.getString("Login"));
        exitBtn.setText(rb.getString("Exit"));
        locationLbl.setText(String.valueOf(ZoneId.systemDefault()));
    }
}
