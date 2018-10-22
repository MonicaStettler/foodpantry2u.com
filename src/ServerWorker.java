import java.io.*;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

// handle requests
class ServerWorker extends Thread{
    private Socket socket;
    SQLiteDB db;

    ServerWorker(Socket socket, SQLiteDB _db) {
        this.socket = socket;
        this.db = _db;
    }

    // create response header
    private void createResponseHeader(String httpVersion,
                                      int statusCode,
                                      String statusInfo,
                                      String contentType,
                                      int contentLength,
                                      PrintStream out) {
        if(statusCode == 200) {
            out.print(httpVersion + " " + statusCode + " " + statusInfo + "\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "Content-Length: " + contentLength + "\r\n\r\n");
        }else if(statusCode == 404) {
            out.println(httpVersion + " " + statusCode + " " + statusInfo + "\r\n");
        }
    }

    // send file contents to client
    private void sendFileToClient(InputStream file, OutputStream out) {

        byte[] buffer = new byte[1000];
        try {
            while (file.available() > 0) {
                out.write(buffer, 0, file.read(buffer));
            }
        } catch (IOException e) {
            System.out.println("Error when reading send file");
        }
    }

    @Override
    public void run() {

        // to client
        PrintStream out = null;

        // from client
        BufferedReader in;

        QueryParser qp = new QueryParser(this.db);

        String request;
        String requestType = "default pathName";
        String pathName = "default pathName";
        String wholePathName = "default wholePathName";
        String HTTPVersion = "default pathName";
        String query = "default query";
        String contentType = "default Content-Type";
        int contentLength = 0;
        int successStatusCode = 200;
        int failedStatusCode = 404;
        String successStatusInfo = "OK";
        String failedStatusInfo = "Not Found";

        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintStream(this.socket.getOutputStream());

            // get the request info
            request = in.readLine();

            // parse the request
            if(request != null && request.split(" ").length >= 3) {
                System.out.println("Request: " + request);
                requestType = request.split(" ")[0];
                wholePathName = request.split(" ")[1];
                pathName = request.split(" ")[1];
                HTTPVersion = request.split(" ")[2];

                if(pathName.split("\\?").length >= 2 ) {
                    query = pathName.split("\\?")[1];
                    pathName = pathName.split("\\?")[0];
                }
            }
            if(pathName.endsWith(".cgi")) {
                File fileInfo;
                if (pathName.endsWith("Volunteer.cgi")){
                    qp.parseVolunteerInfo(query);
                    fileInfo = new File("." + "/cgi/OrderVolunteer.htm");
                }else if (pathName.endsWith("Order.cgi")){
                     qp.parseOrderInfo(query);
                    fileInfo = new File("." + "/cgi/OrderVolunteer.htm");
                } else {
                    qp.parseNeighborInfo(query);
                    fileInfo = new File("." + "/cgi/OrderVolunteer.htm");
                }
                contentType = "text/html";
                contentLength = (int) fileInfo.length();
                createResponseHeader(HTTPVersion,
                        successStatusCode,
                        successStatusInfo,
                        contentType,
                        contentLength,
                        out);
                InputStream file = new FileInputStream(fileInfo);
                sendFileToClient(file, out);
            }else{
                File fileInfo = new File("." + "/cgi/FrontPage.htm");
                contentType = "text/html";
                contentLength = (int) fileInfo.length();
                createResponseHeader(HTTPVersion,
                        successStatusCode,
                        successStatusInfo,
                        contentType,
                        contentLength,
                        out);
                InputStream file = new FileInputStream(fileInfo);
                sendFileToClient(file, out);
            }
        } catch (IOException e) {
            createResponseHeader(HTTPVersion, failedStatusCode, failedStatusInfo, contentType, contentLength, out);
            e.printStackTrace();
        }
        try {
            out.close();

            // close the socket
            this.socket.close();
            System.out.println("Response: " + HTTPVersion + " " + requestType + " " + wholePathName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
