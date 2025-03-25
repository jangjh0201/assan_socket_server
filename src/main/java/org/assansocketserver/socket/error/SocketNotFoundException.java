package org.assansocketserver.socket.error;

public class SocketNotFoundException extends SocketException {
    public SocketNotFoundException(SocketErrorCode socketErrorCode) {
        super(socketErrorCode);
    }
}
