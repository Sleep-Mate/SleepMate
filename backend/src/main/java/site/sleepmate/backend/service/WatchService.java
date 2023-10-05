package site.sleepmate.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.sleepmate.backend.domain.AccelerometerRecord;
import site.sleepmate.backend.domain.LuxRecord;
import site.sleepmate.backend.domain.Member;
import site.sleepmate.backend.domain.VideoRecord;
import site.sleepmate.backend.dto.AccelerometerRequestDto;
import site.sleepmate.backend.dto.LuxRequestDto;
import site.sleepmate.backend.dto.WakeUpResponseDto;
import site.sleepmate.backend.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WatchService {
    private final LuxRecordRepository luxRecordRepository;
    private final VideoOrderRepository videoOrderRepository;
    private final VideoRecordRepository videoRecordRepository;
    private final MemberRepository memberRepository;
    private final AccelerometerRecordRepository accelerometerRecordRepository;

    //30분 전의 lux값과 자러간 시간, 일어난 시간 반환.
    public WakeUpResponseDto getLuxAndSleepTime(LocalDateTime time, long memberSeq){
        //lux값 조회
        LuxRecord lux = getLuxBefore30Min(time, memberSeq);
        //자러간 시간, 일어난 시간 조회
        LocalDate date = getLastRecord().getSleepDate(); //가장 최근 취침 날짜 가져옴
        VideoRecord startSleep = videoRecordRepository.findTop1BySleepDateAndMember_MemberSeqOrderByVideoSeqAsc(date, memberSeq).orElseThrow(NoSuchElementException::new);
        VideoRecord endSleep = videoRecordRepository.findTop1BySleepDateAndMember_MemberSeqOrderByVideoSeqDesc(date, memberSeq).orElseThrow(NoSuchElementException::new);

        return WakeUpResponseDto.fromEntities(lux, startSleep, endSleep);
    }

    public VideoRecord getLastRecord(){
        return videoRecordRepository.findTop1ByOrderByVideoSeqDesc().orElseThrow(NoSuchElementException::new);
    }

    private LuxRecord getLuxBefore30Min(LocalDateTime time, long memberSeq){
        //lux값 조회
        LocalDateTime before30MinTime = time.minusMinutes(30); //30분 전 시각
        LocalDateTime startTime = LocalDateTime.of(before30MinTime.getYear(), before30MinTime.getMonthValue(),
                before30MinTime.getDayOfMonth(), before30MinTime.getHour(), before30MinTime.getMinute(), 0);
        LocalDateTime endTime = startTime.plusMinutes(1);
        List<LuxRecord> luxs = luxRecordRepository.findByTime(memberSeq, startTime, endTime);
        return luxs.get(0);
    }

    //일주기 리듬
    public Map<String, Integer> getCircadianRhythm(long memberSeq){
        LocalDate date = getLastRecord().getSleepDate();
        VideoRecord startSleep = videoRecordRepository.findTop1BySleepDateAndMember_MemberSeqOrderByVideoSeqAsc(date, memberSeq).orElseThrow(NoSuchElementException::new);
        VideoRecord endSleep = videoRecordRepository.findTop1BySleepDateAndMember_MemberSeqOrderByVideoSeqDesc(date, memberSeq).orElseThrow(NoSuchElementException::new);
        //당일 새벽 3시(이후에 잠들기 시작하면 일주기 리듬 안좋음)
        LocalDateTime startMarginalTime = LocalDateTime.of(date.plusDays(1), LocalTime.of(3, 0));
        //당일 아침 10시(이후에 기상하면 일주기 리듬 안좋음)
        LocalDateTime endMarginalTime = LocalDateTime.of(date.plusDays(1), LocalTime.of(10, 0));

        boolean isRhythmFine = true;
        //새벽 3시 이후 취침했거나, 오전 10시 이후 기상했다면 주의.
        if(startSleep.getTime().isAfter(startMarginalTime) || endSleep.getTime().isAfter(endMarginalTime)){
            isRhythmFine = false;
        }
        LuxRecord lux = getLuxBefore30Min(endSleep.getTime(), memberSeq);

        int result = -1;
        if(!isRhythmFine){ //일주기 리듬 안좋으면
            //조도가 20lux보다 작으면
            result = (lux.getLux() < 20) ? 0 : 1;
        } else{
            result = (lux.getLux() < 20) ? 2 : 3;
        }

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("rhythm", result);
        return resultMap;
    }

    @Transactional
    public void saveLuxData(LuxRequestDto luxRequestDto){
        Member member = memberRepository.findByMemberSeq(luxRequestDto.getMemberSeq()).orElseThrow(() ->
                new NoSuchElementException());
        LuxRecord luxRecord = luxRequestDto.toEntity(luxRequestDto, member);
        luxRecordRepository.save(luxRecord);
    }

    @Transactional
    public void saveAccelerometerData(AccelerometerRequestDto accelerometerRequestDto){
        Member member = memberRepository.findByMemberSeq(accelerometerRequestDto.getMemberSeq()).orElseThrow(() ->
                new NoSuchElementException());
        System.out.println(accelerometerRequestDto.getMvalue());
        AccelerometerRecord accelerometerRecord = accelerometerRequestDto.toEntity(accelerometerRequestDto, member);
        accelerometerRecordRepository.save(accelerometerRecord);
    }
}
