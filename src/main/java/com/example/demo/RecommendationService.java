package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.nio.file.*;
import java.util.*;

/**
 * [역할: 서비스 구현 - 핵심 비즈니스 로직 및 AI 서버 연동]
 * - 프론트엔드 데이터를 가공하고, FastAPI 서버와 통신하는 중계 레이어
 */
@Service
public class RecommendationService {

    // [설정] application.properties에서 주입받아 운영 환경 유연성 확보
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${ai.server.url}")
    private String aiServerUrl; // 예: http://localhost:8000/recommend

    private final RestTemplate restTemplate;

    public RecommendationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> processRecommendation(UploadRequestDto dto) throws Exception {
        
        // 1. [파일 처리] 로컬 스토리지에 이미지 임시 저장
        String fileName = dto.getFile().getOriginalFilename(); //"옷 사진"을 전송하는 Key 이름이 file 에서 변경 시 수정
        Path targetPath = Paths.get(uploadDir + fileName);
        Files.createDirectories(targetPath.getParent());
        Files.copy(dto.getFile().getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // 2. [데이터 가공] FastAPI 전송용 MultiValueMap 구성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(dto.getFile().getBytes()) { //"옷 사진"을 전송하는 Key 이름이 file 에서 변경 시 수정
            @Override
            public String getFilename() { return fileName; }
        });
      
      //프론트엔드 데이터이름, FastAPI 가 요구하는 이름 변경시 수정
      // 왼쪽이 FastAPI가 받을 이름, 오른쪽이 DTO에서 꺼내오는 값입니다.
        
        body.add("gender", dto.getGender());
        body.add("style", dto.getStyle());

        // 3. [통신] AI 서버(FastAPI) 호출 및 응답 수신
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(aiServerUrl, requestEntity, Map.class);

        // 4. [결과 반환] AI 서버로부터 받은 JSON 결과 전달
        return response.getBody();
    }
}