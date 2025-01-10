package org.asansocketserver.batch.csv;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asansocketserver.batch.cdc.entity.SensorData;
import org.asansocketserver.batch.cdc.entity.SensorRow;
import org.asansocketserver.batch.cdc.repository.SensorDataRepository;
import org.asansocketserver.domain.notification.entity.Notification;
import org.asansocketserver.domain.notification.mongorepository.NotificationMongoRepository;
import org.asansocketserver.domain.watch.entity.Watch;
import org.asansocketserver.domain.watch.repository.WatchRepository;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@EnableAsync
@EnableScheduling
@Component
public class CsvWriterBatch {
    private final WatchRepository watchRepository;
    private final SensorDataRepository sensorDataRepository;
    private final NotificationMongoRepository  notificationMongoRepository;

    String notificationFolder = "/bin/home/noti/";
    String sensorFolder = "/bin/home/sensor/";

    @Transactional
    @Scheduled(cron = "0 25 19 * * *")
    public void createCsvTask() {
        LocalDate now = LocalDate.now();
        List<Watch> watchList = findAllByWatch();
        watchList.forEach(watch -> {
            String fileName = "ID_" + watch.getId() + "_data_" + now + ".csv";
            createAndWriteSensorDataAtCsv(watch, fileName, now);
        });

        createAndWriteNotificationAtCsv();
    }


    public void createAndWriteNotificationAtCsv() {
        LocalDateTime startOfDay;
        LocalDateTime endOfDay;

        LocalDate today = LocalDate.now();
        startOfDay = today.atStartOfDay();
        endOfDay = today.atTime(LocalTime.MAX);

        List<Notification> notificationList = notificationMongoRepository.findALLByTimestamp(startOfDay, endOfDay);

        int maxRowsPerFile = 10000;
        int fileIndex = 0;
        int currentRow = 0;

        FileWriter writer = null;

        try {
            for (Notification notification : notificationList) {
                // 새로운 파일이 필요하면 파일을 닫고 새 파일을 엽니다.
                if (currentRow % maxRowsPerFile == 0) {
                    if (writer != null) {
                        writer.close();
                    }
                    String fileName = notificationFolder + today + "_" + (fileIndex++) + ".csv";
                    writer = new FileWriter(fileName);
                    writer.write("WatchId,WatchName,WatchHost,Position,Type,Timestamp\n");
                }

                // 각 알림 데이터를 CSV 파일에 작성
                writer.write(String.format("%d,%s,%s,%s,%s,%s\n",
                        notification.getWatchId(),
                        notification.getWatchName(),
                        notification.getWatchHost(),
                        notification.getPosition() != null ? notification.getPosition() : "null",
                        notification.getAlarmType(),
                        notification.getTimestamp().toString()
                ));
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void createAndWriteSensorDataAtCsv(Watch watch, String baseFileName, LocalDate now) {
        List<SensorData> sensorDataList = sensorDataRepository.findAllByNameAndDate(watch.getName(),now);
        for(SensorData sensorData : sensorDataList) {
            int maxRowsPerFile = 100000;
            int fileIndex = 0;
            int currentRow = 0;

            FileWriter writer = null;

            try {
                if(sensorData != null) {
                    // 현재 날짜를 폴더명으로 생성
                    String dateFolderName = sensorFolder + now.toString();
                    File dateFolder = new File(dateFolderName);
                    if (!dateFolder.exists()) {
                        dateFolder.mkdirs();
                    }

                    // watch.name에 해당하는 폴더를 생성
                    String watchFolderName = dateFolderName + "/" + watch.getName();
                    File watchFolder = new File(watchFolderName);
                    if (!watchFolder.exists()) {
                        watchFolder.mkdirs();
                    }

                    for (SensorRow sensorRow : sensorData.getSensorRowList()) {
                        // 새로운 파일이 필요하면 파일을 닫고 새 파일을 엽니다.
                        if (currentRow % maxRowsPerFile == 0) {
                            if (writer != null) {
                                writer.close();
                            }
                            String fileName = watchFolderName + "/" + baseFileName + "_" + (fileIndex++) + ".csv";
                            writer = new FileWriter(fileName);
                            writer.write("timestamp,accX,accY,accZ,barometer,gyroX,gyroY,gyroZ,heartRate,light\n");
                        }
                        writeDataRow(writer, sensorRow);
                        currentRow++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }}
    }


    private void writeDataRow(FileWriter writer, SensorRow sensorRow) throws IOException {
        String rowDate = sensorRow.getTimestamp() + "," +
                sensorRow.getAccX() + "," +
                sensorRow.getAccY() + "," +
                sensorRow.getAccZ() + "," +
                sensorRow.getBarometerValue() + "," +
                sensorRow.getGyroX() + "," +
                sensorRow.getGyroY() + "," +
                sensorRow.getGyroZ() + "," +
                sensorRow.getHeartRateValue() + "," +
                sensorRow.getLightValue() ;
        writer.write(rowDate + "\n");
    }


//    private SensorData findSensorDataByWatchIdAndLocalDate(Watch watch, LocalDate localDate) {
//        return sensorDataRepository.findByWatchIdAndDate(watch.getId(), localDate).get();
//    }

    private List<Watch> findAllByWatch() {
        return watchRepository.findAll();
    }
}
