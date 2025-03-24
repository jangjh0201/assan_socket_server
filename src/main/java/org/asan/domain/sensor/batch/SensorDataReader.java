package org.asan.domain.sensor.batch;
//package org.asansocketserver.domain.sensor.batch;
//
//import jakarta.annotation.PostConstruct;
//import org.asansocketserver.domain.sensor.entity.CombinedSensorData;
//import org.asansocketserver.domain.sensor.entity.SensorAccelerometer;
//import org.asansocketserver.domain.sensor.entity.SensorBarometer;
//import org.asansocketserver.domain.sensor.entity.SensorGyroscope;
//import org.asansocketserver.domain.sensor.entity.SensorHeartRate;
//import org.asansocketserver.domain.sensor.entity.SensorLight;
//import org.asansocketserver.domain.sensor.entity.sensorType.Barometer;
//import org.asansocketserver.domain.watch.entity.Watch;
//import org.asansocketserver.domain.watch.repository.WatchRepository;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Field;
//import java.util.List;
//
//@Component
//public class SensorDataReader implements ItemReader<CombinedSensorData> {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Autowired
//    private WatchRepository watchRepository;
//
//    private int currentIndex = 0;
//    private List<SensorAccelerometer> accelerometerData;
//    private List<Watch> watchList;
//
//    // 허용할 타임스탬프 차이 (밀리초 단위)
//    private final long TIMESTAMP_TOLERANCE_MS = 50;
//
//    private List<Watch> findAllByWatch() {
//        return watchRepository.findAll();
//    }
//
//    @PostConstruct
//    public void init() {
//        // 가속도계 데이터를 기준으로 타임스탬프를 가져옵니다.
//        List<Watch> watchList = findAllByWatch();
//    }
//
//    @Override
//    public CombinedSensorData read() {
//
//
//        if (currentIndex >= accelerometerData.size()) {
//            return null; // 더 이상 데이터가 없습니다.
//        }
//
//
//        SensorAccelerometer currentAcc = accelerometerData.get(currentIndex++);
//        long currentTimestamp = currentAcc.getTimestamp(); // 가속도계 타임스탬프 기준
//
//        CombinedSensorData combinedData = CombinedSensorData.builder()
//                .timestamp(currentTimestamp)
//                .accX(currentAcc.getAccX())
//                .accY(currentAcc.getAccY())
//                .accZ(currentAcc.getAccZ())
//                .barometerValue(getSensorValueWithinTolerance(Barometer.class, "value", currentTimestamp))
////                .gyroX(getSensorValueWithinTolerance(SensorGyroscope.class, "gyroX", currentTimestamp))
////                .gyroY(getSensorValueWithinTolerance(SensorGyroscope.class, "gyroY", currentTimestamp))
////                .gyroZ(getSensorValueWithinTolerance(SensorGyroscope.class, "gyroZ", currentTimestamp))
//                .heartRateValue(getSensorValueWithinTolerance(SensorHeartRate.class, "value", currentTimestamp))
//                .lightValue(getSensorValueWithinTolerance(SensorLight.class, "value", currentTimestamp))
//                .build();
//
//        return combinedData;
//    }
//
//    private <T> T getSensorValueWithinTolerance(Class<?> entityClass, String fieldName, long timestamp, Class<T> fieldType) {
//        long startTime = timestamp - TIMESTAMP_TOLERANCE_MS;
//        long endTime = timestamp + TIMESTAMP_TOLERANCE_MS;
//
//        Query query = new Query(
//                Criteria.where("timestamp").gte(startTime).lte(endTime)
//        );
//
//        Object entity = mongoTemplate.findOne(query, entityClass);
//        if (entity != null) {
//            try {
//                Field field = entityClass.getDeclaredField(fieldName);
//                field.setAccessible(true);  // 필드에 접근할 수 있도록 설정
//                return fieldType.cast(field.get(entity));
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//
//
//}
