package com.example.demo;

import org.springframework.web.multipart.MultipartFile;

/**
 * [역할: 데이터 전송 객체 (DTO)]
 * - 클라이언트(React)와 서버 간의 데이터 규격 정의
 */

//프론트엔드 데이터이름, FastAPI 가 요구하는 이름 변경시 수정
public class UploadRequestDto {
    private MultipartFile file;  // 업로드 이미지,  "옷 사진"을 전송하는 Key 이름이 file 에서 변경 시 수정
    private String gender;       // 성별 정보
    private String style;        // 스타일 태그

    // [데이터 바인딩] Getter/Setter
    public MultipartFile getFile() { return file; }
    public void setFile(MultipartFile file) { this.file = file; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }
}