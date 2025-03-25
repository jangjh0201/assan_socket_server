package org.assansocketserver.socket.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MessageType {
    POSITION("POSITION"),
    WATCH_LIST("WATCH_LIST"),
    NEW_WATCH("NEW_WATCH"),
    DEL_WATCH("DEL_WATCH"),
    HEART_RATE("HEART_RATE"),
    HIGH_HEART_RATE("HIGH_HEART_RATE"),
    LOW_HEART_RATE("LOW_HEART_RATE"),
    LOCATION("LOCATION"),
    LIGHT("LIGHT"),
    BAROMETER("BAROMETER"),
    GYROSCOPE("GYROSCOPE"),
    ACCELEROMETER("ACCELEROMETER"),
    PPG_GREEN("PPG_GREEN"),
    DIS_WATCH("DIS_WATCH"),
    CONTACTION("CONTACTION"),
    PROHIBITION("PROHIBITION"),
    GENDER("GENDER"),
    TOTAL_PROHIBITION("TOTAL_PROHIBITION");

    private final String desc;
}
