package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Diary;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    List<Diary> findAllByDate(LocalDate date);

    List<Diary> findByDateBetween(LocalDate startDate, LocalDate endDate);

    Diary getFirstByDate(LocalDate data); // getFirst = Limit(1)

    @Transactional // 붙여야 삭제됨
    void deleteAllByDate(LocalDate date);
}