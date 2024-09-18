package com.kstd.api.repository;

import com.kstd.api.entity.Lecture;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @Query(value = "SELECT * FROM Lecture l WHERE (CURRENT_TIMESTAMP BETWEEN DATEADD('DAY', -7,  l.start_time ) AND DATEADD('DAY', 1,  l.start_time ))", nativeQuery = true)
    List<Lecture> findAvailableLectures(Pageable pageable);

    @Query(value = "SELECT * FROM Lecture l WHERE l.id = :lectureId AND (CURRENT_TIMESTAMP BETWEEN DATEADD('DAY', -7,  l.start_time ) AND DATEADD('DAY', 1,  l.start_time ))", nativeQuery = true)
    Optional<Lecture> findValidLectureById(@Param("lectureId") Long lectureId);

    @Query("SELECT l FROM Lecture l JOIN l.applicants a WHERE a.createdDate > :threeDaysAgo GROUP BY l ORDER BY COUNT(a) DESC")
    List<Lecture> findPopularLectures(@Param("threeDaysAgo") LocalDateTime threeDaysAgo, Pageable pageable);
}
