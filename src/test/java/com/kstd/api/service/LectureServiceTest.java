package com.kstd.api.service;

import com.kstd.api.entity.Lecture;
import com.kstd.api.repository.LectureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LectureServiceTest {
    @Mock
    private LectureRepository lectureRepository;

    @InjectMocks
    private LectureService lectureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void addLecture_shouldSaveLecture() {
        Lecture lecture = Lecture.builder()
                .id(1L)
                .lecturer("Lecturer A")
                .location("Location A")
                .content("Content A")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .maxApplicants(100)
                .build();

        when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

        Lecture savedLecture = lectureService.addLecture(lecture);

        assertNotNull(savedLecture);
        assertEquals("Lecturer A", savedLecture.getLecturer());
        verify(lectureRepository, times(1)).save(lecture);
    }

    @Test
    void getAllLectures_shouldReturnAllLectures() {
        Lecture lecture1 = Lecture.builder()
                .id(1L)
                .lecturer("Lecturer A")
                .location("Location A")
                .content("Content A")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .maxApplicants(100)
                .build();

        Page<Lecture> mockPage = new PageImpl<>(Arrays.asList(lecture1));

        when(lectureRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        // 실제 테스트 실행
        Page<Lecture> lectures = lectureService.getAllLectures(PageRequest.of(0, 100));

        // 결과 검증
        assertEquals(1, lectures.getSize());
        verify(lectureRepository, times(1)).findAll(any(Pageable.class));
    }

}
