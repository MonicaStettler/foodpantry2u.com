import java.sql.*;

public class SQLiteDB {

    public static Connection getConnection() {
        try {
        	//String driver = "com.mysql.jdbc.Driver";
        	String url = "jdbc:mysql://localhost:3306/foodbankcustomer";
        	String username = "root";
        	String password = "foodbank";
        	//Class.forName(driver);
        	
        	Connection conn = DriverManager.getConnection(url, username, password);
        	System.out.println("connected");
            return conn;
        } catch (SQLException e) {
        	System.out.println("getConnection() error");
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void createTestTable()
    {
    	 String sql = "CREATE TABLE IF NOT EXISTS TestHack" +
    			 "(TNeighborID VARCHAR(255) NOT NULL, " +
                 "TUserName VARCHAR(255), " + 
                 "TFoodOrder VARCHAR(255), " + 
                 "TAddress VARCHAR(255), " + 
                 "PRIMARY KEY ( TNeighborID ))"; 

        try  {
        	Connection con = getConnection();
        	PreparedStatement create = con.prepareStatement(sql);
        	create.executeUpdate();
        } catch (SQLException e) {
        	System.out.println("create table error");
            System.out.println(e.getMessage());
        }
    }

    public void insertTestTable(String TNeighborID
            ,String TUserName
            ,String TFoodOrder
            ,String TAddress)
    {
        String sql = "INSERT INTO TestHack(TNeighborID, TUserName, TFoodOrder, TAddress) " +
                "VALUES(?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, TNeighborID);
            pstmt.setString(2, TUserName);
            pstmt.setString(3, TFoodOrder);
            pstmt.setString(4, TAddress);

            pstmt.executeUpdate();
            System.out.println("table inserted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createNeighborTable() 
    {  
        String sql = "CREATE TABLE IF NOT EXISTS Neighbor"
                + "(NeighborID VARCHAR(64) NOT NULL, "
                + "LastName VARCHAR(32), "
                + "FirstName VARCHAR(32), "
                + "AddressID VARCHAR(64), "
                + "OrderID VARCHAR(64), "
                + "VolunteerID VARCHAR(64), "
                + "PRIMARY KEY (NeighborID))";

        try{
        	Connection con = getConnection();
        	PreparedStatement create = con.prepareStatement(sql);
        	create.executeUpdate();
        	System.out.println("Neighbor table created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createOrderTable() 
    {
        String sql = "CREATE TABLE IF NOT EXISTS FoodOrder"
                + "(OrderID VARCHAR(64) NOT NULL, "
                + "NeighborID VARCHAR(64), "
                + "PickUpLocationID VARCHAR(64), "
                + "PickUpDate VARCHAR(16), "
                + "Produce VARCHAR(8), "
                + "Meat VARCHAR(8), "
                + "DryGoods VARCHAR(8), "
                + "PRIMARY KEY (OrderID))";

        try{
        	Connection con = getConnection();
        	PreparedStatement create = con.prepareStatement(sql);
        	create.executeUpdate();
        	System.out.println("Order table created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createVolunteerTable() 
    {
        String sql = "CREATE TABLE IF NOT EXISTS Volunteer"
                + "(VolunteerID VARCHAR(64) NOT NULL, "
                + "PickUpLocationID VARCHAR(64), "
                + "VolunteerType VARCHAR(16), "
                + "VolunteerDate VARCHAR(16), "
                + "PRIMARY KEY (VolunteerID))";
        try{
        	Connection con = getConnection();
        	PreparedStatement create = con.prepareStatement(sql);
        	create.executeUpdate();
        	System.out.println("Volunteer table created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createAddressTable() 
    {
        String sql = "CREATE TABLE IF NOT EXISTS Address"
                + "(AddressID VARCHAR(64) NOT NULL, "
                + "StreetName VARCHAR(32), "
                + "City VARCHAR(16), "
                + "State VARCHAR(16), "
                + "ZIP VARCHAR(8), "
                + "PRIMARY KEY (AddressID))";

        try{
        	Connection con = getConnection();
        	PreparedStatement create = con.prepareStatement(sql);
        	create.executeUpdate();
        	System.out.println("Address table created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createPickUpLocationTable() 
    {
        String sql = "CREATE TABLE IF NOT EXISTS PickUpLocation"
                + "(PickUpLocationID VARCHAR(64) NOT NULL, "
                + "PickUpType VARCHAR(16), "
                + "StreetName VARCHAR(32), "
                + "City VARCHAR(16), "
                + "State VARCHAR(16), "
                + "ZIP VARCHAR(8), "
                + "PRIMARY KEY (PickUpLocationID))";

        try{
        	Connection con = getConnection();
        	PreparedStatement create = con.prepareStatement(sql);
        	create.executeUpdate();
        	System.out.println("pickup location table created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertNeighbor(String NeighborID
            ,String LastName
            ,String FirstName
            ,String AddressID
            ,String OrderID
            ,String VolunteerID)
    {
        String sql = "INSERT INTO Neighbor(NeighborID,LastName, FirstName, AddressID, OrderID, VolunteerID) " +
                "VALUES(?,?,?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, NeighborID);
            pstmt.setString(2, LastName);
            pstmt.setString(3, FirstName);
            pstmt.setString(4, AddressID);
            pstmt.setString(5, OrderID);
            pstmt.setString(6, VolunteerID);
            pstmt.executeUpdate();
            System.out.println("neighbor table inserted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertOrderTable(String OrderID
            ,String NeighborID
            ,String PickUpLocationID
            ,String PickUpDate
            ,String Produce
            ,String Meat
            ,String DryGoods)
    {
        String sql = "INSERT INTO FoodOrder(OrderID, NeighborID, PickUpLocationID, PickUpDate, Produce, Meat, DryGoods) " +
                "VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, OrderID);
            pstmt.setString(2, NeighborID);
            pstmt.setString(3, PickUpLocationID);
            pstmt.setString(4, PickUpDate);
            pstmt.setString(5, Produce);
            pstmt.setString(6, Meat);
            pstmt.setString(7, DryGoods);

            pstmt.executeUpdate();
            System.out.println("Order table inserted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertVolunteerTable(String VolunteerID
            ,String PickUpLocationID
            ,String VolunteerType
            ,String VolunteerDate)
    {
        String sql = "INSERT INTO Volunteer(VolunteerID, PickUpLocationID, VolunteerType, VolunteerDate) " +
                "VALUES(?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, VolunteerID);
            pstmt.setString(2, PickUpLocationID);
            pstmt.setString(3, VolunteerType);
            pstmt.setString(4, VolunteerDate);

            pstmt.executeUpdate();
            System.out.println("Volunteer Table inserted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertAddressTable(String AddressID
            ,String StreetName
            ,String City
            ,String State
            ,String ZIP)
    {
        String sql = "INSERT INTO Address(AddressID, StreetName, City, State, ZIP) " +
                "VALUES(?,?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, AddressID);
            pstmt.setString(2, StreetName);
            pstmt.setString(3, City);
            pstmt.setString(4, State);
            pstmt.setString(5, ZIP);

            pstmt.executeUpdate();
            System.out.println("address table inserted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertPickUpLocationTable(String PickUpLocationID
            ,String PickUpType
            ,String StreetName
            ,String City
            ,String State
            ,String ZIP)
    {
        String sql = "INSERT INTO PickUpLocation(PickUpLocationID, PickUpType, StreetName, City, State, ZIP) " +
                "VALUES(?,?,?,?,?,?)";

        /*
        String sql = "CREATE TABLE IF NOT EXISTS PickUpLocation (\n"
                + "	PickUpLocationID VARCHAR PRIMARY KEY,\n"
                + "	PickUpType VARCHAR NOT NULL,\n"
                + "	StreetName VARCHAR NOT NULL,\n"
                + "	City VARCHAR NOT NULL, \n"
                + "	State VARCHAR NOT NULL,\n"
                + "	ZIP integer NOT NULL,\n"
                + ");";
        */
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, PickUpLocationID);
            pstmt.setString(2, PickUpType);
            pstmt.setString(3, StreetName);
            pstmt.setString(4, City);
            pstmt.setString(5, State);
            pstmt.setString(6, ZIP);

            pstmt.executeUpdate();
            System.out.println("PickUp Location table inserted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * select all rows in the warehouses table
     */
    public void selectAll(){
        String sql = "SELECT id, name, capacity FROM warehouses";

        try (Connection conn = getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getDouble("capacity"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

