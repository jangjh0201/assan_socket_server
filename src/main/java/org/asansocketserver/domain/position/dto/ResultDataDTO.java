package org.asansocketserver.domain.position.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDataDTO implements Comparable<ResultDataDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String android_id;
    private String sectorName;
    private int count;
    private double avg;
    private double ratio;

    public ResultDataDTO(int id, String sectorName, int count, double avg, double ratio) {
        this.id = id;
        this.sectorName = sectorName;
        this.count = count;
        this.avg = avg;
        this.ratio = ratio;
    }

    @Override
    public int compareTo(ResultDataDTO other) {
        // count가 높은 것을 우선순위로 설정
        return Integer.compare(other.count, this.count);
    }

    @Override
    public String toString() {
        return "ResultDataDTO{" + "android_id" + android_id +
                "id=" + id +
                ", sectorName='" + sectorName + '\'' +
                ", count=" + count +
                ", avg=" + avg +
                ", ratio=" + ratio +
                '}';
    }
}
