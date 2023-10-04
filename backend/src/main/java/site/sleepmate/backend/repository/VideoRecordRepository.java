package site.sleepmate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.sleepmate.backend.domain.VideoRecord;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VideoRecordRepository extends JpaRepository<VideoRecord, Long> {
    List<VideoRecord> findAllByMember_MemberSeqOrderBySleepDateAsc(Long memberSeq);
    //취침 시작 시간의 로그 가져오는 쿼리
    Optional<VideoRecord> findTop1BySleepDateAndMember_MemberSeqOrderByVideoSeqAsc(LocalDate date, Long memberSeq);
    //취침 종료 시간의 로그 가져오는 쿼리
    Optional<VideoRecord> findTop1BySleepDateAndMember_MemberSeqOrderByVideoSeqDesc(LocalDate date, Long memberSeq);
    Optional<VideoRecord> findTop1ByOrderByVideoSeqDesc();



    List<VideoRecord> findAllByMember_MemberSeqAndSleepDateOrderByTimeDesc(Long memberSeq, LocalDate localDate);

    List<VideoRecord> findAllByMember_MemberSeqAndSleepDateOrderByTimeAsc(Long memberSeq, LocalDate localDate);

    List<VideoRecord> findAllByMember_MemberSeqAndSleepDate(Long memberSeq, LocalDate sleepDate);

    Optional<VideoRecord> findAllByMember_MemberSeqAndTime(Long memberSeq, LocalDateTime time);

    int countBySleepDateAndMember_MemberSeq(LocalDate sleepDate, Long memberSeq);
    int countBySleepDateAndPostureAndMember_MemberSeq(LocalDate sleepDate, int posture, Long memberSeq);
}
