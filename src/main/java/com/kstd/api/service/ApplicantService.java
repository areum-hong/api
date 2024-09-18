package com.kstd.api.service;

import com.kstd.api.entity.Applicant;
import com.kstd.api.entity.Lecture;
import com.kstd.api.exception.InvalidDataException;
import com.kstd.api.repository.ApplicantRepository;
import com.kstd.api.repository.LectureRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicantService {

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private LectureRepository lectureRepository;


    // 해당 강의 전체 인원 조회
    public List<Applicant> getApplicantsByLectureId(Long lectureId) {
        return applicantRepository.findByLectureId(lectureId);
    }

    // 강의 신청
    @Transactional
    public void applyForLecture(Long lectureId, String employeeNumber) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강연이 존재하지 않습니다."));

        validApplicant(lecture, employeeNumber);

        // 신청
        Applicant applicant = Applicant.builder()
                .lecture(lecture)
                .employeeNumber(employeeNumber)
                .build();
        applicantRepository.save(applicant);
    }

    // 신청 내역 조회 (사번으로 검색)
    public List<Applicant> getApplicantsByEmployeeNumber(String employeeNumber) {
        return applicantRepository.findByEmployeeNumber(employeeNumber);
    }

    // 신청 취소
    @Transactional
    public void cancelApplication(Long applicantId) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new IllegalArgumentException("신청 내역이 존재하지 않습니다."));
        applicantRepository.delete(applicant);
    }

    private void validApplicant(Lecture lecture, String employeeNumber){


        if (employeeNumber == null || employeeNumber.isEmpty()) {
            throw new InvalidDataException("사번은 필수 입력 항목입니다.");
        }

        if (employeeNumber.length() != 5) {
            throw new InvalidDataException("사번은 5자리로 입력해야합니다.");
        }

        // Max 인원을 넘기면 안됨
        if (lecture.getApplicants() != null && lecture.getApplicants().size() >= lecture.getMaxApplicants()) {
            throw new IllegalArgumentException("신청 인원이 초과되었습니다.");
        }

        // 중복 신청 확인
        if (applicantRepository.existsByLectureIdAndEmployeeNumber(lecture.getId(), employeeNumber)) {
            throw new IllegalArgumentException("이미 신청한 강연입니다.");
        }

        if (lectureRepository.findValidLectureById(lecture.getId()).isEmpty()) {
            throw new InvalidDataException("신청 가능한 강의가 아닙니다.");
        }
    }
}
