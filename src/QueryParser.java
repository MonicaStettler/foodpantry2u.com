
import java.util.UUID;

public class QueryParser {
    SQLiteDB db;
    UUID uuid;
    String neighborID;
    String addressID;
    String orderID;
    String volunteerID;
    String pickupLocationID;

    QueryParser(SQLiteDB _db){
        this.db = _db;
        uuid = UUID.randomUUID();
        neighborID = uuid.toString();
        uuid = UUID.randomUUID();
        addressID = uuid.toString();
        uuid = UUID.randomUUID();
        orderID = uuid.toString();
        uuid = UUID.randomUUID();
        volunteerID = uuid.toString();
        uuid = UUID.randomUUID();
        pickupLocationID = uuid.toString();
    }

    public void parseNeighborInfo(String query){
        //gets user information
        String[] queries = query.split("&");
        String nameFirst = queries[0].split("=")[1].replace('+', ' ');
        String nameLast = queries[1].split("=")[1].replace('+', ' ');
        String addrStreet1 = queries[2].split("=")[1].replace('+', ' ');
        String addrStreet2 = queries[3].split("=")[1].replace('+', ' ');
        String addrCity = queries[4].split("=")[1].replace('+', ' ');
        String addrState = queries[5].split("=")[1].replace('+', ' ');
        String addrZip = queries[6].split("=")[1].replace('+', ' ');
       // String[] userInfo = {nameFirst, nameLast, addrStreet1, addrStreet2, addrCity, addrState, addrZip};

        UUID uuid = UUID.randomUUID();
        String neighborID = uuid.toString();

        db.insertNeighbor(neighborID, nameLast, nameFirst, addressID, orderID, volunteerID);
        db.insertAddressTable(addressID, addrStreet1 + addrStreet2, addrCity, addrState, addrZip);
    }

    public void parseVolunteerInfo(String query){
        String[] queries = query.split("&");
        String volunteerType = queries[0].split("=")[1].replace('+', ' ');
        String volunteerDate= queries[1].split("=")[1].replace('+', ' ');

        db.insertVolunteerTable(volunteerID, pickupLocationID, volunteerType, volunteerDate);

    }

    public void parseOrderInfo(String query){
        String[] queries = query.split("&");
        String pickupLocation = queries[0].split("=")[1].replace('+', ' ');
        String orderDate = queries[1].split("=")[1].replace('+', ' ');
        String food0 = queries[2].split("=")[1].replace('+', ' ');
        String food1 = queries[3].split("=")[1].replace('+', ' ');
        String food2 = queries[4].split("=")[1].replace('+', ' ');
        //String[] orderInfo = {pickupLocation, orderDate, food0, food1,food2};

        db.insertOrderTable(orderID, neighborID, pickupLocationID, orderDate, food0, food1, food2);
    }
}
