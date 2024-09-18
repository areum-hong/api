package com.kstd.api.controller;

import com.kstd.api.entity.Applicant;
import com.kstd.api.entity.Lecture;
import com.kstd.api.service.ApplicantService;
import com.kstd.api.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/backoffice")
public class BackOfficeController {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private ApplicantService applicantService;

    // 전체 강연 목록 조회
    @GetMapping("/lectures")
    public ResponseEntity<Page<Lecture>> getAllLectures(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Lecture> lectures = lectureService.getAllLectures(pageable);
        return ResponseEntity.ok(lectures);
    }

    // 강연 등록
    @PostMapping("/lectures")
    public ResponseEntity<Lecture> addLecture(@RequestBody Lecture lecture) {
        Lecture savedLecture = lectureService.addLecture(lecture);
        return new ResponseEntity<>(savedLecture, HttpStatus.CREATED);
    }

    // 강연 신청자 목록 조회 (강연 별 신청자 목록)
    @GetMapping("/lectures/{lectureId}/applicants")
    public ResponseEntity<List<String>> getApplicantsByLecture(@PathVariable Long lectureId) {
        List<String> employeeNumbers = applicantService.getApplicantsByLectureId(lectureId)
                .stream()
                .map(Applicant::getEmployeeNumber) // 사번만 추출
                .collect(Collectors.toList());

        return ResponseEntity.ok(employeeNumbers);
    }
}