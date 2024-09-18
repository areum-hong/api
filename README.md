개발언어 : JAVA(17)
프레임워크 : Spring Boot, Spring JPA
RDBMS : H2 Database

테이블 설계
Lecture (
    id : 고유 ID
    lecturer : 강연자
    location : 장소
    content : 강연내용
    startTime: 강연 시작 시간
    endTime: 강연 종료 시간
    maxApplicants: 최대 신청 가능 인원
)

Applicant (
    id : 고유 ID
    employeeNumber : 사번
    lecture_id : 강연 id
)

고민, 설명 : 특성상 많은 건수가 들어갈 상황은 아니지만, 건수가 많아질 경우를 대비해서 paging 처리하였습니다.

