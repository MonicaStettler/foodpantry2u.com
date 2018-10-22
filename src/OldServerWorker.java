import java.io.*;
import java.net.Socket;

// handle requests
class OldServerWorker extends Thread{
    private Socket _socket;

    public OldServerWorker(Socket socket) {
        _socket = socket;
    }

    @Override
    public void run() {

        // to client
        PrintStream out = null;

        // from client
        BufferedReader in = null;

        String request = "default pathName";
        String requestType = "default pathName";
        String pathName = "default pathName";
        String wholePathName = "default wholePathName";
        String HTTPVersion = "default pathName";
        String parentDir = "default pathName";
        String query = "default query";
        String contentType = "default Content-Type";
        int contentLength = 0;
        int successStatusCode = 200;
        int failedStatusCode = 404;
        String successStatusInfo = "OK";
        String failedStatusInfo = "Not Found";
        int currentStatusCode = 0;
        String currentStatusInfo = "default currentStatusInfo";

        try {
            in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            out = new PrintStream(_socket.getOutputStream());

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
            /*
            if(pathName.endsWith(".ico") || pathName.endsWith(".gif")) {

                // when extension equals .ico or .gif
                currentStatusCode = failedStatusCode;
                currentStatusInfo = failedStatusInfo;
                createResponseHeader(HTTPVersion, failedStatusCode, failedStatusInfo, contentType, contentLength, out);


            }else*/
            if(pathName.endsWith(".html") || pathName.endsWith(".htm")) {

                // when file extension equals .html or .htm
                // read the file
                File fileInfo = new File("." + pathName);

                // set content-type equals text/html
                contentType = "text/html";

                // get size of file
                contentLength = (int)fileInfo.length();
                currentStatusCode = successStatusCode;
                currentStatusInfo = successStatusInfo;

                // send response header to client
                createResponseHeader(HTTPVersion, successStatusCode, successStatusInfo, contentType, contentLength, out);

                // read file contents
                InputStream file = new FileInputStream(fileInfo);

                // send file to clients
                sendFileToClient(file, out);
            /*
            }else if(pathName.endsWith(".txt") || pathName.endsWith(".java")) {

                // when file extension equals txt or java
                File fileInfo = new File("." + pathName);

                // set content-type equals text/plain
                contentType = "text/plain";
                contentLength = (int)fileInfo.length();
                currentStatusCode = successStatusCode;
                currentStatusInfo = successStatusInfo;
                createResponseHeader(HTTPVersion, successStatusCode, successStatusInfo, contentType, contentLength, out);

                InputStream file = new FileInputStream(fileInfo);
                sendFileToClient(file, out);
            */
            /*
            }else if(pathName.endsWith(".class")) {

                // when file extension equals class
                File fileInfo = new File("." + pathName);
                HTTPVersion = "HTTP/1.0";

                // set content-type equals java/*
                contentType = "java/*";
                contentLength = (int)fileInfo.length();
                currentStatusCode = successStatusCode;
                currentStatusInfo = successStatusInfo;
                createResponseHeader(HTTPVersion, successStatusCode, successStatusInfo, contentType, contentLength, out);

                InputStream file = new FileInputStream(fileInfo);
                sendFileToClient(file, out);
            */
            }else if(pathName.endsWith(".fake-cgi")) {

                // parse querys
                String querys[] = query.split("&");

                // get user name
                String userName = querys[0].split("=")[1];

                // get num1 and num2
                int numOne = Integer.parseInt(querys[1].split("=")[1]);
                int numTwo = Integer.parseInt(querys[2].split("=")[1]);
                int sum = numOne + numTwo;

                currentStatusCode = successStatusCode;
                currentStatusInfo = successStatusInfo;

                // send html to client
                out.append("HTTP/1.1 200 OK\r\n" +
                        "\r\n" +
                        "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" +
                        "<TITLE>SUM</TITLE>\r\n" +
                        "</HEAD><BODY>\r\n" +
                        "<H1>"+ userName + ", the sum of " + numOne + " and " + numTwo +
                        " is " + sum +
                        "</H1>\r\n</BODY></HTML>\r\n");
            }else {

                // read path
                String filePath = "." + pathName;
                File fileInfo = new File(filePath);
                File[] filesDirs = fileInfo.listFiles();

                // when the path not exists, response 404
                if(fileInfo == null || filesDirs == null) {

                    currentStatusCode = failedStatusCode;
                    currentStatusInfo = failedStatusInfo;
                    createResponseHeader(HTTPVersion, failedStatusCode, failedStatusInfo, contentType, contentLength, out);

                } else {

                    currentStatusCode = successStatusCode;
                    currentStatusInfo = successStatusInfo;
                    out.append("HTTP/1.1 200 OK\r\n" +
                            "\r\n" +
                            "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" +
                            "<TITLE>Directory</TITLE>\r\n" +
                            "</HEAD><BODY>\r\n" +
                            "<H1>Index of " + pathName + "</H1>\r\n");

                    // get parent directory
                    parentDir = fileInfo.getParent();
                    if(parentDir != null) {
                        if(parentDir.split("\\.").length == 0) {
                            out.append("<a href=/> Parent Directory </a><br>");
                        }else {
                            out.append("<a href=" + parentDir.split("\\.")[1] + "> Parent Directory </a><br>");
                        }
                    }
                    // create directory html
                    for(int i=0; i<filesDirs.length; i++) {
                        if(filesDirs[i].isDirectory()) {
                            out.append("<a href=" + filesDirs[i].getPath() + ">" + filesDirs[i].getName() + "/</a><br>");
                        }
                        else if (filesDirs[i].isFile()) {
                            out.append("<a href=" + filesDirs[i].getPath().substring(1) + ">" + filesDirs[i].getName() + "</a><br>");
                        }
                    }
                    out.append("</BODY></HTML>\r\n");
                }
            }

        } catch (IOException e) {

            currentStatusCode = failedStatusCode;
            currentStatusInfo = failedStatusInfo;
            createResponseHeader(HTTPVersion, failedStatusCode, failedStatusInfo, contentType, contentLength, out);
            e.printStackTrace();
        }
        try {
            out.close();

            // close the socket
            _socket.close();
            System.out.println("Response: " + HTTPVersion + " " + currentStatusCode + " "
                    + currentStatusInfo + " " + requestType + " " + wholePathName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
}
