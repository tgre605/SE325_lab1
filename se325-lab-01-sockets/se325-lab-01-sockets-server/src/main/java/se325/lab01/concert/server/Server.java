package se325.lab01.concert.server;

import java.io.Console;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se325.lab01.concert.common.Concert;
import se325.lab01.concert.common.Config;
import se325.lab01.concert.common.RequestMessage;
import se325.lab01.concert.common.ResponseMessage;

/**
 * Simple server that maintains a collection of Concerts, and which implements a protocol allowing clients to make
 * CRUD requests of the server.
 * <p>
 * Clients are expected to follow a protocol whereby a Hello message is the first message that they should send. Once
 * acknowledged, clients can then send further messages, as defined by class RequestMessage. At the end of a
 * communication session, clients should send a Goodbye message, informing the server that no further messages
 * will be sent.
 */
public class Server {
    // List of Concerts.
    private Map<Long, Concert> concerts;

    // Unique id of the next concert to create.
    private long nextId;

    // Network connection objects.
    private ServerSocket serverSocket;
    private Socket clientConnection;

    public Server() {
        concerts = new HashMap<>();
        nextId = 1;
    }

    /**
     * Starts the server, causing it to block while waiting for a connection request. Once a connection has been
     * accepted, the server processes incoming messages coming over the connection.
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(Config.SERVER_PORT);

        InetAddress serverHost = InetAddress.getLocalHost();
        System.out.println("Server destination: " + serverHost.getHostAddress() + ", " + serverSocket.getLocalPort());

        // Repeatedly handle requests for processing.
        boolean quit = false;

        while (!quit) {
            try (Socket clientConnection = serverSocket.accept()) {
                this.clientConnection = clientConnection;

                ObjectOutputStream out = new ObjectOutputStream(clientConnection.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(clientConnection.getInputStream());

                RequestMessage request = (RequestMessage) in.readObject();
                ResponseMessage response = null;

                if (request.getType() != RequestMessage.MessageType.Hello) {
                    // Accepted a new connection, but the first message isn't a Hello message - so terminate the connection.
                    response = ResponseMessage.makeProtocolErrorResponse();
                    out.writeObject(response);
                } else {
                    // Accepted a new connection and received the initial Hello message.
                    response = ResponseMessage.makeSuccessfulResponse();
                    out.writeObject(response);
                    manageSession(clientConnection, out, in);
                }
            } catch (IOException e) {
                quit = true;
            } catch (ClassNotFoundException e) {
                // Server attempted to deserialise an object without having access to the corresponding class.
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the server.
     */
    public void shutdown() {
        try {
            // Closing the ServerSocket will cause any accept() call on it to abort and throw an IOException.
            serverSocket.close();

            // Closing the Socket connection to the client will cause any IO methods to abort with an IOException.
            if (clientConnection != null) {
                clientConnection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes incoming messages, generating and sending a response back to
     * the client.
     */
    private void manageSession(Socket clientConnection, ObjectOutputStream out, ObjectInputStream in) {
        boolean sessionEnded = false;
        ResponseMessage response = null;

        try {
            while (!sessionEnded) {
                // Read next request.
                RequestMessage request = (RequestMessage) in.readObject();
                System.out.println("Received a new message: " + request.getType());

                switch (request.getType()) {
                    case Hello: {
                        response = ResponseMessage.makeProtocolErrorResponse();
                        break;
                    }
                    case Goodbye: {
                        // Acknowledge the client's intention to close the session.
                        response = ResponseMessage.makeSuccessfulResponse();
                        sessionEnded = true;
                        break;
                    }
                    case Create: {
                        response = handleCreate(request);
                        break;
                    }
                    case Retrieve: {
                        response = handleRetrieve(request);
                        break;
                    }
                    case Update: {
                        response = handleUpdate(request);
                        break;
                    }
                    case Delete: {
                        response = handleDelete(request);
                        break;
                    }
                    case List: {
                        List<Concert> concertList = new ArrayList<>(
                                concerts.values());
                        response = ResponseMessage.makeListResponse(concertList);
                        break;
                    }
                    case Clear: {
                        concerts.clear();
                        response = ResponseMessage.makeSuccessfulResponse();
                    }
                }

                // Send response back to the client.
                out.writeObject(response);
            }

        } catch (IOException e) {
            sessionEnded = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ResponseMessage handleDelete(RequestMessage request) {
        ResponseMessage response;
        Long uid = request.getId();

        Concert concert = concerts.get(uid);
        if (concert == null) {
            response = ResponseMessage.makeUnsuccessfulDeleteResponse();
        } else {
            concerts.remove(uid);
            response = ResponseMessage.makeSuccessfulResponse();
        }
        return response;
    }

    private ResponseMessage handleUpdate(RequestMessage request) {
        ResponseMessage response;
        Concert concert = request.getConcert();
        if (!concerts.containsKey(concert.getId())) {
            response = ResponseMessage.makeUnsuccessfulUpdateResponse();
        } else {
            concerts.put(concert.getId(), concert);
            response = ResponseMessage.makeSuccessfulResponse();
        }
        return response;
    }

    private ResponseMessage handleRetrieve(RequestMessage request) {
        ResponseMessage response;
        Long uid = request.getId();
        Concert concert = concerts.get(uid);
        if (concert == null) {
            response = ResponseMessage.makeUnsuccessfulRetrieveResponse();
        } else {
            response = ResponseMessage.makeSuccessfulRetrieveResponse(concert);
        }
        return response;
    }

    private ResponseMessage handleCreate(RequestMessage request) {
        ResponseMessage response;
        Concert concert = request.getConcert();

        // Store the new Concert.
        Long uid = nextId++;
        Concert newConcert = new Concert(uid, concert.getTitle(), concert.getDate());
        concerts.put(uid, newConcert);

        response = ResponseMessage.makeSuccessfulCreateResponse(newConcert);
        return response;
    }

    public static void main(String[] args) throws InterruptedException {
        final Server server = new Server();

        Thread serviceThread = new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                System.out.println("Error starting server");
            }
        });
        serviceThread.start();

        Thread.sleep(1000);
        Keyboard.prompt("Press enter to stop the server ");

        server.shutdown();
        System.out.println("Server shutting down");

        serviceThread.join();
    }
}
