package DAO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Contact;
import model.Country;
import model.Customer;
import model.FemaleCustomer;
import model.FirstLevelDivision;
import model.MaleCustomer;
import model.User;

public class Helper {
    private static Locale currentUserLocale;
    private static ZoneId currentUserZoneId;
    private static User currentUser;

    public Helper() {
    }

    public static ObservableList<MaleCustomer> getAllMaleCustomers(String gender) throws SQLException {
        ObservableList<MaleCustomer> allCustomers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers WHERE Gender = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setString(1,gender);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int customerID = rs.getInt(1);
            String customerName = rs.getString(2);
            String address = rs.getString(3);
            String postalCode = rs.getString(4);
            String phone = rs.getString(5);
            int divisionId = rs.getInt(10);
            Date examDate = rs.getDate(11);
            String gender1 = rs.getString(12);
            allCustomers.add(new MaleCustomer(customerID, customerName, address, postalCode, phone, divisionId, examDate, gender1));
        }

        return allCustomers;
    }

    public static ObservableList<FemaleCustomer> getAllFemaleCustomers(String gender) throws SQLException {
        ObservableList<FemaleCustomer> allCustomers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers WHERE Gender = ?";

        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setString(1,gender);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int customerID = rs.getInt(1);
            String customerName = rs.getString(2);
            String address = rs.getString(3);
            String postalCode = rs.getString(4);
            String phone = rs.getString(5);
            int divisionId = rs.getInt(10);
            Date examDate = rs.getDate(11);
            String gender1 = rs.getString(12);
            allCustomers.add(new FemaleCustomer(customerID, customerName, address, postalCode, phone, divisionId, examDate, gender1));
        }

        return allCustomers;
    }

    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int customerID = rs.getInt(1);
            String customerName = rs.getString(2);
            String address = rs.getString(3);
            String postalCode = rs.getString(4);
            String phone = rs.getString(5);
            int divisionId = rs.getInt(10);
            String gender = rs.getString(12);
            allCustomers.add(new Customer(customerID, customerName, address, postalCode, phone, divisionId, gender));
        }

        return allCustomers;
    }

    public static boolean checkCredentials(String userName, String password) throws SQLException {
        String sql = "SELECT * from users WHERE User_Name =? AND Password=? ";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int userId1 = rs.getInt(1);
            String userName1 = rs.getString(2);
            String password1 = rs.getString(3);
            currentUser = new User(userId1, userName1, password1);
            currentUserLocale = Locale.getDefault();
            currentUserZoneId = ZoneId.systemDefault();
            return true;
        } else {
            return false;
        }
    }

    public static Locale getCurrentUserLocale() {
        return currentUserLocale;
    }

    public static void setCurrentUserLocale(Locale currentUserLocale) {
        Helper.currentUserLocale = currentUserLocale;
    }

    public static ZoneId getCurrentUserZoneId() {
        return currentUserZoneId;
    }

    public static void setCurrentUserZoneId(ZoneId currentUserZoneId) {
        Helper.currentUserZoneId = currentUserZoneId;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Helper.currentUser = currentUser;
    }

    public static int deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM Customers WHERE Customer_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int deleteAppt(int apptId) throws SQLException {
        String sql = "DELETE FROM Appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, apptId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int updateAppt(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId, int apptId) throws SQLException {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startToString = start.format(formatter);
        String endToString = end.format(formatter);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(startToString));
        ps.setTimestamp(6, Timestamp.valueOf(endToString));
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        ps.setInt(10, apptId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int update(String name, String address, String postalCode, String phoneNum, int divId, int customerId) throws SQLException {
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNum);
        ps.setString(5, (String) null);
        ps.setString(6, (String) null);
        ps.setString(7, (String) null);
        ps.setString(8, (String) null);
        ps.setInt(9, divId);
        ps.setInt(10, customerId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static ObservableList<String> getAllTypes() {
        ObservableList<String> types = FXCollections.observableArrayList();
        types.add("Annual Physical Exam");
        types.add("Sports Physical");
        types.add("Sick Visit");
        types.add("Recheck Appointment");
        return types;
    }

    public static ObservableList<Contact> getAllContacts() throws SQLException {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        String sql = "SELECT * FROM contacts";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int contactId = rs.getInt(1);
            String contactName = rs.getString(2);
            String email = rs.getString(3);
            Contact contact1 = new Contact(contactId, contactName, email);
            contacts.add(contact1);
        }

        return contacts;
    }

    public static int getUserId(String username) throws SQLException {
        int userID = 1;
        String sql = "SELECT * FROM users WHERE User_Name = ? ";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setString(1, username);

        int userId;
        for (ResultSet rs = ps.executeQuery(); rs.next(); userID = userId) {
            userId = rs.getInt(1);
        }

        return userID;
    }

    public static User getUserById(int userId) throws SQLException {
        User user1 = null;
        String sql = "SELECT * FROM users WHERE User_ID = ? ";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, userId);

        String userName;
        String password;
        for (ResultSet rs = ps.executeQuery(); rs.next(); user1 = new User(userId, userName, password)) {
            userName = rs.getString("User_Name");
            password = rs.getString("Password");
        }

        return user1;
    }

    public static ObservableList<Country> getAllCountries() throws SQLException {
        ObservableList<Country> countries = FXCollections.observableArrayList();
        String sql = "SELECT Country, Country_ID FROM countries";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String countryName = rs.getString(1);
            int countryId = rs.getInt(2);
            Country country1 = new Country(countryId, countryName);
            countries.add(country1);
        }

        return countries;
    }

    public static ObservableList<FirstLevelDivision> getDivision(int countryID) throws SQLException {
        ObservableList<FirstLevelDivision> divisions = FXCollections.observableArrayList();
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, countryID);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String division = rs.getString(2);
            int divisionId = rs.getInt(1);
            int countryId = rs.getInt(7);
            FirstLevelDivision division1 = new FirstLevelDivision(divisionId, division, countryId);
            divisions.add(division1);
        }

        return divisions;
    }

    public static FirstLevelDivision getDivisionById(int divisionId) throws SQLException {
        FirstLevelDivision division1 = null;
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, divisionId);

        String division;
        int countryId;
        for (ResultSet rs = ps.executeQuery(); rs.next(); division1 = new FirstLevelDivision(divisionId, division, countryId)) {
            division = rs.getString("Division");
            countryId = rs.getInt("Country_ID");
        }

        return division1;
    }

    public static Country getCountryById(int countryId) throws SQLException {
        Country country1 = null;
        String sql = "SELECT * FROM countries WHERE Country_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, countryId);

        String countryName;
        for (ResultSet rs = ps.executeQuery(); rs.next(); country1 = new Country(countryId, countryName)) {
            countryName = rs.getString("Country");
        }

        return country1;
    }

    public static int addCustomer(String name, String address, String postalCode, String phoneNum, int divId, LocalDate lastMammogramProstate, String gender) throws SQLException {
        String sql = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By,Division_ID, Last_Exam, Gender) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        String examToString = lastMammogramProstate.toString();
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNum);
        ps.setString(5, (String) null);
        ps.setString(6, (String) null);
        ps.setString(7, (String) null);
        ps.setString(8, (String) null);
        ps.setInt(9, divId);
        ps.setDate(10, Date.valueOf(examToString));
        ps.setString(11, gender);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int addAppointment(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) throws SQLException {
        String sql = "INSERT into appointments(Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startToString = start.format(formatter);
        String endToString = end.format(formatter);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(startToString));
        ps.setTimestamp(6, Timestamp.valueOf(endToString));
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int appointmentId = rs.getInt(1);
            String title = rs.getString(2);
            String description = rs.getString(3);
            String location = rs.getString(4);
            String type = rs.getString(5);
            Timestamp start = rs.getTimestamp(6);
            Timestamp end = rs.getTimestamp(7);
            int customerId = rs.getInt(12);
            int userId = rs.getInt(13);
            int contactId = rs.getInt(14);
            allAppointments.add(new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId));
        }

        return allAppointments;
    }

    public static ObservableList<User> getAllUsers() throws SQLException {
        ObservableList<User> users = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int userId = rs.getInt(1);
            String userName = rs.getString(2);
            String password = rs.getString(3);
            users.add(new User(userId, userName, password));
        }

        return users;
    }

    public static ObservableList<Appointment> getApptsIn15(int userId) throws SQLException {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime userTimeNow = now.atZone(getCurrentUserZoneId());
        ZonedDateTime userTimePlus15 = userTimeNow.plusMinutes(15L);
        String sql = "SELECT * from appointments WHERE Start BETWEEN ? AND ? AND User_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        String start = userTimeNow.format(formatter);
        String end = userTimePlus15.format(formatter);
        ps.setTimestamp(1, Timestamp.valueOf(start));
        ps.setTimestamp(2, Timestamp.valueOf(end));
        ps.setInt(3, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int contactId1 = rs.getInt(14);
            int apptId = rs.getInt(1);
            String title = rs.getString(2);
            String description = rs.getString(3);
            String location = rs.getString(4);
            String type = rs.getString(5);
            Timestamp start1 = rs.getTimestamp(6);
            Timestamp end1 = rs.getTimestamp(7);
            int customerId = rs.getInt(12);
            int userId1 = rs.getInt(13);
            System.out.println(start + end);
            Appointment appointment = new Appointment(apptId, title, description, location, type, start1, end1, customerId, userId1, contactId1);
            appts.add(appointment);
        }

        return appts;
    }

    public static ObservableList<Appointment> filterByContactId(int contactId) throws SQLException {
        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();
        ObservableList<Appointment> allAppts = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, contactId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int contactId1 = rs.getInt(14);
            int apptId = rs.getInt(1);
            String title = rs.getString(2);
            String description = rs.getString(3);
            String location = rs.getString(4);
            String type = rs.getString(5);
            Timestamp start = rs.getTimestamp(6);
            Timestamp end = rs.getTimestamp(7);
            int customerId = rs.getInt(12);
            int userId = rs.getInt(13);
            allAppts.add(new Appointment(apptId, title, description, location, type, start, end, customerId, userId, contactId1));
        }

        return allAppts;
    }

    public static ObservableList<Appointment> filterByUserId(int userId) throws SQLException {
        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();
        ObservableList<Appointment> allAppts = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE User_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int contactId1 = rs.getInt(14);
            int apptId = rs.getInt(1);
            String title = rs.getString(2);
            String description = rs.getString(3);
            String location = rs.getString(4);
            String type = rs.getString(5);
            Timestamp start = rs.getTimestamp(6);
            Timestamp end = rs.getTimestamp(7);
            int customerId = rs.getInt(12);
            int userId1 = rs.getInt(13);
            allAppts.add(new Appointment(apptId, title, description, location, type, start, end, customerId, userId1, contactId1));
        }

        return allAppts;
    }

    public static ObservableList<Appointment> filterByCustomerId(int customerId) throws SQLException {
        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int contactId1 = rs.getInt(14);
            int apptId = rs.getInt(1);
            String title = rs.getString(2);
            String description = rs.getString(3);
            String location = rs.getString(4);
            String type = rs.getString(5);
            Timestamp start = rs.getTimestamp(6);
            Timestamp end = rs.getTimestamp(7);
            int customerId1 = rs.getInt(12);
            int userId = rs.getInt(13);
            filteredAppts.add(new Appointment(apptId, title, description, location, type, start, end, customerId1, userId, contactId1));
        }

        return filteredAppts;
    }

    public static ObservableList<Appointment> getAllApptsWithType(String type) throws SQLException {
        ObservableList<Appointment> allApptsWithType = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Type = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setString(1, type);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int contactId1 = rs.getInt(14);
            int apptId = rs.getInt(1);
            String title = rs.getString(2);
            String description = rs.getString(3);
            String location = rs.getString(4);
            String type1 = rs.getString(5);
            Timestamp start = rs.getTimestamp(6);
            Timestamp end = rs.getTimestamp(7);
            int customerId1 = rs.getInt(12);
            int userId = rs.getInt(13);
            allApptsWithType.add(new Appointment(apptId, title, description, location, type1, start, end, customerId1, userId, contactId1));
        }

        return allApptsWithType;
    }

    public static ObservableList<Appointment> getApptsFilteredMonthWeek(LocalDate start, LocalDate end) throws SQLException {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startString = start.format(formatter);
        String endString = end.format(formatter);
        String sql = "SELECT * FROM appointments WHERE Start between ? and ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setDate(1, Date.valueOf(startString));
        ps.setDate(2, Date.valueOf(endString));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int contactId1 = rs.getInt(14);
            int apptId = rs.getInt(1);
            String title = rs.getString(2);
            String description = rs.getString(3);
            String location = rs.getString(4);
            String type1 = rs.getString(5);
            Timestamp start1 = rs.getTimestamp(6);
            Timestamp end1 = rs.getTimestamp(7);
            int customerId1 = rs.getInt(12);
            int userId = rs.getInt(13);
            Appointment appointment1 = new Appointment(apptId, title, description, location, type1, start1, end1, customerId1, userId, contactId1);
            appts.add(appointment1);
        }

        return appts;
    }

    public static String createMonthTypeReport() throws SQLException {
        StringBuilder monthType = new StringBuilder();
        String sql = "SELECT MONTHNAME(start) AS MONTH, TYPE, COUNT(Appointment_ID) as TOTAL FROM appointments GROUP BY MONTH, TYPE";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String var10001 = rs.getString("MONTH");
            monthType.append("Month: " + var10001 + "\n Number of " + rs.getString("TYPE") + " appointments: " + rs.getString("TOTAL") + "\n\n");
        }

        return monthType.toString();
    }

    public static boolean validateBusinessHours(LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDate apptDate) {
        LocalDateTime ldtBusinessHrsStart = LocalDateTime.of(apptDate, LocalTime.of(8, 0));
        LocalDateTime ldtBusinessHrsEnd = LocalDateTime.of(apptDate, LocalTime.of(22, 0));
        ZonedDateTime zdtBusinessHrsStart = ZonedDateTime.of(ldtBusinessHrsStart, ZoneId.of("US/Eastern"));
        ZonedDateTime zdtBusinessHrsEnd = ZonedDateTime.of(ldtBusinessHrsEnd, ZoneId.of("US/Eastern"));
        ZonedDateTime zdtStart = ZonedDateTime.of(startDateTime, getCurrentUserZoneId());
        ZonedDateTime zdtEnd = ZonedDateTime.of(endDateTime, getCurrentUserZoneId());
        return !zdtStart.isAfter(zdtEnd) && !zdtStart.isBefore(zdtBusinessHrsStart) && !zdtStart.isAfter(zdtBusinessHrsEnd) && !zdtEnd.isBefore(zdtBusinessHrsStart) && !zdtEnd.isAfter(zdtBusinessHrsEnd);
    }

    public static Customer getCustomerByID(int customerId) throws SQLException {
        Customer customer1 = null;
        String sql = "SELECT * FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, customerId);

        String customerName;
        String address;
        String postalcode;
        String phone;
        int divisionId;
        String gender;
        String examDateToString;
        for (ResultSet rs = ps.executeQuery(); rs.next(); customer1 = new Customer(customerId, customerName, address, postalcode, phone, divisionId, gender)) {
            customerName = rs.getString("Customer_Name");
            address = rs.getString("Address");
            postalcode = rs.getString("Postal_Code");
            phone = rs.getString("Phone");
            divisionId = rs.getInt("Division_ID");
            gender = rs.getString("Gender");
           // Timestamp examDate = rs.getTimestamp("Last_Mammogram/Prostate_Exam");
           // LocalDate examDate1 = examDate.toLocalDateTime().toLocalDate();
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            //examDateToString = examDate1.toString();
        }

        return customer1;
    }

    public static Contact getContactById(int contactId) throws SQLException {
        Contact contact1 = null;
        String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, contactId);

        String conTactName;
        String email;
        for (ResultSet rs = ps.executeQuery(); rs.next(); contact1 = new Contact(contactId, conTactName, email)) {
            conTactName = rs.getString("Contact_Name");
            email = rs.getString("Email");
        }

        return contact1;
    }

    public static int addCustomer(FemaleCustomer femaleCustomer) throws SQLException {
        String sql = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By,Division_ID, Last_Exam, Gender) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        String name = femaleCustomer.getCustomerName();
        String address = femaleCustomer.getAddress();
        String postalCode = femaleCustomer.getPostalCode();
        String phoneNum = femaleCustomer.getPhoneNumber();
        int divId = femaleCustomer.getDivisionId();
        Date lastMammogramProstate = femaleCustomer.getLastMammogram();
        String gender = femaleCustomer.getGender();
        String examToString = lastMammogramProstate.toString();
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNum);
        ps.setString(5, (String) null);
        ps.setString(6, (String) null);
        ps.setString(7, (String) null);
        ps.setString(8, (String) null);
        ps.setInt(9, divId);
        ps.setDate(10, Date.valueOf(examToString));
        ps.setString(11, gender);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int addMaleCustomer(MaleCustomer maleCustomer) throws SQLException {
        String sql = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By,Division_ID, Last_Exam, Gender) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        String name = maleCustomer.getCustomerName();
        String address = maleCustomer.getAddress();
        String postalCode = maleCustomer.getPostalCode();
        String phoneNum = maleCustomer.getPhoneNumber();
        int divId = maleCustomer.getDivisionId();
        Date lastMammogramProstate = maleCustomer.getLastProstateExam();
        String gender = maleCustomer.getGender();
        //String examToString = lastMammogramProstate.toString();
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNum);
        ps.setString(5, (String) null);
        ps.setString(6, (String) null);
        ps.setString(7, (String) null);
        ps.setString(8, (String) null);
        ps.setInt(9, divId);
        ps.setDate(10, lastMammogramProstate);
        ps.setString(11, gender);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    public static ObservableList<Appointment> getApptById(int apptId) throws SQLException {
        ObservableList<Appointment> allApptsWithId = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, apptId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int contactId1 = rs.getInt(14);
            int apptId1 = rs.getInt(1);
            String title = rs.getString(2);
            String description = rs.getString(3);
            String location = rs.getString(4);
            String type1 = rs.getString(5);
            Timestamp start = rs.getTimestamp(6);
            Timestamp end = rs.getTimestamp(7);
            int customerId1 = rs.getInt(12);
            int userId = rs.getInt(13);
            allApptsWithId.add(new Appointment(apptId1, title, description, location, type1, start, end, customerId1, userId, contactId1));
        }

        return allApptsWithId;
    }
}

