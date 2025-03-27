package org.assansocketserver.domain.sensor.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SensorDataJobLauncher {

    private final JobLauncher jobLauncher;
    @Qualifier("sensorDataJob")
    private final Job sensorDataJob;
    @Qualifier("deleteExpiredSensorDataJob")
    private final Job deleteExpiredSensorDataJob;

    @Autowired
    public SensorDataJobLauncher(JobLauncher jobLauncher,
            @Qualifier("sensorDataJob") Job sensorDataJob,
            @Qualifier("deleteExpiredSensorDataJob") Job deleteExpiredSensorDataJob) {
        this.jobLauncher = jobLauncher;
        this.sensorDataJob = sensorDataJob;
        this.deleteExpiredSensorDataJob = deleteExpiredSensorDataJob;
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void runJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(sensorDataJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 21 7 * * *")
    public void runDeleteExpiredSensorDataJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(deleteExpiredSensorDataJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
