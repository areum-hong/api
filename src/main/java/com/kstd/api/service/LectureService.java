package com.kstd.api.service;

import com.kstd.api.entity.Applicant;
import com.kstd.api.entity.Lecture;
import com.kstd.api.exception.InvalidDataException;
import com.kstd.api.repository.ApplicantRepository;
import com.kstd.api.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LectureService {
    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    public Page<Lecture> getAllLectures(Pageable pageable) {
        return lectureRepository.findAll(pageable);
    }

    public List<Lecture> getAvailableLectures(Pageable pageable) {
        return lectureRepository.findAvailableLectures(pageable);
    }

    public Lecture addLecture(Lecture lecture) {
        validateLecture(lecture);

        return lectureRepository.save(lecture);
    }

    public List<Applicant> getApplicantsByLectureId(Long lectureId) {
        return applicantRepository.findByLectureId(lectureId);
    }

    public List<Lecture> getPopularLectures(Pageable pageable) {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        return lectureRepository.findPopularLectures(threeDaysAgo, pageable);
    }

    private void validateLecture(Lecture lecture) {
        if (lecture.getLecturer() == null || lecture.getLecturer().isEmpty()) {
            throw new InvalidDataException("강연자는 필수 입력 항목입니다.");
        }
        if (lecture.getLocation() == null || lecture.getLocation().isEmpty()) {
            throw new InvalidDataException("강연 위치는 필수 입력 항목입니다.");
        }
        if (lecture.getContent() == null || lecture.getContent().isEmpty()) {
            throw new InvalidDataException("강연 내용은 필수 입력 항목입니다.");
        }
        if (lecture.getStartTime() == null) {
            throw new InvalidDataException("강연 시작 시간은 필수 입력 항목입니다.");
        }
        if (lecture.getMaxApplicants() <= 0) {
            throw new InvalidDataException("최대 신청 인원은 0보다 커야 합니다.");
        }
    }
}
