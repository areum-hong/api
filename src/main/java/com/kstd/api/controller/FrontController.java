package com.kstd.api.controller;

import com.kstd.api.entity.Applicant;
import com.kstd.api.entity.Lecture;
import com.kstd.api.service.ApplicantService;
import com.kstd.api.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/front")
public class FrontController {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private ApplicantService applicantService;

    // 강연 목록 (신청 가능한 시점부터 강연 시작 시간 1일 후까지 노출)
    @GetMapping("/lectures")
    public ResponseEntity<List<Lecture>> getAvailableLectures(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        List<Lecture> lectures = lectureService.getAvailableLectures(pageable);
        return ResponseEntity.ok(lectures);
    }

    // 강연 신청
    @PostMapping("/lectures/{lectureId}/apply")
    public ResponseEntity<String> applyForLecture(@PathVariable Long lectureId, @RequestParam String employeeNumber) {
        try {
            applicantService.applyForLecture(lectureId, employeeNumber);
            return ResponseEntity.ok("신청이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 신청 내역 조회 (사번 입력)
    @GetMapping("/applicants")
    public ResponseEntity<List<Applicant>> getApplicantsByEmployeeNumber(@RequestParam String employeeNumber) {
        List<Applicant> applicants = applicantService.getApplicantsByEmployeeNumber(employeeNumber);
        return ResponseEntity.ok(applicants);
    }

    // 신청한 강연 취소
    @DeleteMapping("/applicants/{applicantId}/cancel")
    public ResponseEntity<String> cancelApplication(@PathVariable Long applicantId) {
        applicantService.cancelApplication(applicantId);
        return ResponseEntity.ok("신청이 취소되었습니다.");
    }

    // 실시간 인기 강연
    @GetMapping("/lectures/popular")
    public ResponseEntity<List<Lecture>> getPopularLectures(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        List<Lecture> popularLectures = lectureService.getPopularLectures(pageable);
        return ResponseEntity.ok(popularLectures);
    }
}
