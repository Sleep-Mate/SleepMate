package site.sleepmate.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.sleepmate.backend.domain.HeartRateRecord;
import site.sleepmate.backend.domain.VideoOrder;
import site.sleepmate.backend.repository.HeartRateRecordRepository;
import site.sleepmate.backend.repository.VideoOrderRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AbnormalHeartRateMeasurementService {
    private final HeartRateRecordRepository heartRateRecordRepository;
    private final VideoOrderRepository videoOrderRepository;

    // 감지된 시간 & 이상 심박수 & 해당 자세 반환 메서드
    public Map<Map<String, Integer>, Integer> getAbnormalSituation(LocalDate sleepDate) {
        // 수면 날짜의 데이터 가져오기(수정해야함)
        List<HeartRateRecord> heartRateRecords = heartRateRecordRepository.findAllBySleepDateOrderByTime(sleepDate);
        List<VideoOrder> videoOrders = videoOrderRepository.findAllBySleepDateOrderByStartTime(sleepDate);

        // 시간, 이상 심박수, 자세를 담고 있는 Map
        Map<Map<String, Integer>, Integer> abnormalElement = new HashMap<>();
        // 시간, 이상 심박수를 담고 있는 Map
        Map<LocalDateTime, Double> detectedTimeAndHeartRate = new HashMap<>();
        // 시간, 이상 심박수를 담고 있는 Map, 실제 저장용
        Map<String, Integer> strDetectedTimeAndHeartRate = new HashMap<>();
        // 이상 시간을 담은 Set
        Set<LocalDateTime> detectedTime = new HashSet<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // 이상 심박수일때, detectedTimeAndHeartRate에 시간, 이상 심박수 담기
        for (HeartRateRecord heartRateRecord : heartRateRecords) {
            if (heartRateRecord.getHeartRate() > 80 || heartRateRecord.getHeartRate() < 45) {
                // 정확하게 표현할 시간 정해지는대로 포맷팅
                detectedTimeAndHeartRate.put(heartRateRecord.getTime(), heartRateRecord.getHeartRate());
                strDetectedTimeAndHeartRate.put(formatter.format(heartRateRecord.getTime()), heartRateRecord.getHeartRate().intValue());
            }
        }

        // detectedTimeAndHeartRate에서 키인 시간만 detectedTime에 담기
        detectedTime = detectedTimeAndHeartRate.keySet();
        // 시간을 하나씩 가져오기위해 Iterator 클래스 사용
        Iterator<LocalDateTime> iterator = detectedTime.iterator();
        // 이상 심박수가 감지된 시간이 videoOrder 테입르의 시작 시간과 끝 시간안에 들어간 필드를 구해서 해당 범위안에 있는 자세를 abnormalElement에 대입하는 로직

        for (VideoOrder videoOrder : videoOrders) {
            while (iterator.hasNext()) {
                if (videoOrder.getStartTime().isBefore(iterator.next()) && videoOrder.getEndTime().isAfter(iterator.next())) {
                    abnormalElement.put(strDetectedTimeAndHeartRate, videoOrder.getPosture());
                }
            }
        }

        return abnormalElement;
    }
}
