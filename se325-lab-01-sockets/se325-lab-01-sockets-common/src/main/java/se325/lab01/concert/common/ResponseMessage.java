package se325.lab01.concert.common;

import java.io.Serializable;
import java.util.List;

/**
 * Class to represent a response message, sent from the Server to a Client.
 * <p>
 * Similarly to RequestMessage, this class has a set of factory methods to create ResponseMessages.
 */
public class ResponseMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    // Message type.
    public enum Status {
        Success, Failure
    }

    // Field values.
    private Status status;
    private String failureMessage;
    private Long id;
    private Concert concert;
    private List<Concert> concerts;

    private ResponseMessage() {
    }

    public static ResponseMessage makeSuccessfulResponse() {
        ResponseMessage replyMessage = new ResponseMessage();
        replyMessage.status = Status.Success;

        return replyMessage;
    }

    public static ResponseMessage makeSuccessfulCreateResponse(Concert concert) {
        ResponseMessage replyMessage = new ResponseMessage();
        replyMessage.status = Status.Success;
        replyMessage.id = concert.getId();
        replyMessage.concert = concert;

        return replyMessage;
    }

    public static ResponseMessage makeSuccessfulRetrieveResponse(Concert concert) {
        ResponseMessage replyMessage = new ResponseMessage();
        replyMessage.status = Status.Success;
        replyMessage.concert = concert;

        return replyMessage;
    }

    public static ResponseMessage makeUnsuccessfulRetrieveResponse() {
        ResponseMessage replyMessage = new ResponseMessage();
        replyMessage.status = Status.Failure;
        replyMessage.failureMessage = "Concert with specified id not found";

        return replyMessage;
    }

    public static ResponseMessage makeUnsuccessfulUpdateResponse() {
        ResponseMessage replyMessage = new ResponseMessage();
        replyMessage.status = Status.Failure;
        replyMessage.failureMessage = "Concert with specified id not found";

        return replyMessage;
    }

    public static ResponseMessage makeUnsuccessfulDeleteResponse() {
        ResponseMessage replyMessage = new ResponseMessage();
        replyMessage.status = Status.Failure;
        replyMessage.failureMessage = "Concert with specified id not found";

        return replyMessage;
    }

    public static ResponseMessage makeListResponse(List<Concert> concerts) {
        ResponseMessage replyMessage = new ResponseMessage();
        replyMessage.status = Status.Success;
        replyMessage.concerts = concerts;

        return replyMessage;
    }

    public static ResponseMessage makeProtocolErrorResponse() {
        ResponseMessage replyMessage = new ResponseMessage();
        replyMessage.status = Status.Failure;
        replyMessage.failureMessage = "Unexpected message received";

        return replyMessage;
    }

    public Status getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public Concert getConcert() {
        return concert;
    }

    public List<Concert> getConcerts() {
        return concerts;
    }

    public String getFailureMessage() {
        return failureMessage;
    }
}
