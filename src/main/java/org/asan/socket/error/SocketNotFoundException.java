package org.asan.socket.error;

public class SocketNotFoundException extends SocketException {
    public SocketNotFoundException(SocketErrorCode socketErrorCode) {
        super(socketErrorCode);
    }
}
