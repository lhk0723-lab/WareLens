package com.example.demo;

import org.springframework.web.multipart.MultipartFile;

/**
 * [역할: 요청/응답 DTO 설계]
 * - 프론트엔드(React)에서 전송한 파일 및 텍스트 데이터를 하나로 묶어서 수신하는 객체
 * - 향후 데이터 확장 시 변수(항목) 추가만으로 쉽게 대응 가능
 */
public class UploadRequestDto {
    private MultipartFile file;  // [역할: 이미지 업로드 처리] 리액트 전송 이미지 파일
    private String gender;       // [역할: 사용자 입력 처리] 리액트 전송 성별 데이터
    private String style;        // [역할: 사용자 입력 처리] 리액트 전송 스타일 데이터

    // 데이터 매핑을 위한 Getter / Setter
    public MultipartFile getFile() { return file; }
    public void setFile(MultipartFile file) { this.file = file; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }
}