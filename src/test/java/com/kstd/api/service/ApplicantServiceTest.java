package com.kstd.api.service;

import com.kstd.api.entity.Applicant;
import com.kstd.api.entity.Lecture;
import com.kstd.api.repository.ApplicantRepository;
import com.kstd.api.repository.LectureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class ApplicantServiceTest {

    @Mock
    private ApplicantRepository applicantRepository;

    @Mock
    private LectureRepository lectureRepository;

    @InjectMocks
    private ApplicantService applicantService;

    private Lecture mockLecture; // Mock Lecture 객체

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock 데이터 설정
        mockLecture = Lecture.builder()
                .id(1L)
                .lecturer("Lecturer A")
                .location("Location A")
                .content("Content A")
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(3))
                .maxApplicants(100)
                .build();

        when(lectureRepository.findById(1L)).thenReturn(Optional.of(mockLecture));
        when(lectureRepository.findValidLectureById(1L)).thenReturn(Optional.of(mockLecture));
    }

    @Test
    void applyForLecture_shouldApplySuccessfully() {
        Long lectureId = 1L;
        String employeeNumber = "12345";

        // 강의가 존재하며, 이미 신청하지 않은 상태로 Mock 설정
        when(applicantRepository.existsByLectureIdAndEmployeeNumber(lectureId, employeeNumber)).thenReturn(false);

        // 신청 실행
        applicantService.applyForLecture(lectureId, employeeNumber);

        // 검증: 신청이 성공적으로 저장되었는지 확인
        verify(applicantRepository, times(1)).save(any(Applicant.class));
    }

    @Test
    void applyForLecture_shouldThrowExceptionWhenAlreadyApplied() {
        Long lectureId = 1L;
        String employeeNumber = "12345";

        // 이미 신청된 상태로 Mock 설정
        when(applicantRepository.existsByLectureIdAndEmployeeNumber(lectureId, employeeNumber)).thenReturn(true);

        // 예외 검증
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                applicantService.applyForLecture(lectureId, employeeNumber));

        assertEquals("이미 신청한 강연입니다.", exception.getMessage());
        verify(applicantRepository, never()).save(any(Applicant.class));
    }

    @Test
    void cancelApplication_shouldCancelSuccessfully() {
        Long applicantId = 1L;

        // 신청 내역을 가진 Applicant 객체 생성
        Applicant applicant = Applicant.builder()
                .employeeNumber("12345")
                .lecture(mockLecture) // Mock Lecture 객체 참조
                .build();

        // Mock 설정: Applicant가 존재하는 상황 시뮬레이션
        when(applicantRepository.findById(applicantId)).thenReturn(Optional.of(applicant));

        // 신청 취소 실행
        applicantService.cancelApplication(applicantId);

        // 검증: 신청이 취소되었는지 확인
        verify(applicantRepository, times(1)).delete(applicant);
    }

    @Test
    void cancelApplication_shouldThrowExceptionWhenApplicantNotFound() {
        Long applicantId = 1L;

        // Mock 설정: Applicant가 존재하지 않는 경우
        when(applicantRepository.findById(applicantId)).thenReturn(Optional.empty());

        // 예외 검증
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                applicantService.cancelApplication(applicantId));

        assertEquals("신청 내역이 존재하지 않습니다.", exception.getMessage());
        verify(applicantRepository, never()).delete(any(Applicant.class));
    }
}
