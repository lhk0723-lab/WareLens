package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * [역할: REST API 게이트웨이]
 * - 요청 진입점으로서 유효성 검증 및 예외 처리 담당
 */
@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*") 
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/upload")
    public Map<String, Object> uploadFile(@ModelAttribute UploadRequestDto requestDto) {
        
        // [검증] 라벨링 가이드에 따른 스타일 유효성 체크
        List<String> validStyles = Arrays.asList("스트릿", "댄디", "고프코어", "미니멀");
        if (!validStyles.contains(requestDto.getStyle())) {
            return Map.of("status", "fail", "message", "지원하지 않는 스타일입니다.");
        }

        try {
            // [연동] 서비스 로직 위임
            Map<String, Object> result = recommendationService.processRecommendation(requestDto);
            
            // [응답 가공] 프론트엔드 표준 포맷 래핑
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.putAll(result);
            return response;
            
        } catch (Exception e) {
            // [예외 처리] 서버 장애 방지 및 에러 클라이언트 전달
            return Map.of("status", "fail", "error", e.getMessage());
        }
    }
    
    // [테스트] 통합 데이터 확인용 프론트엔드 페이지
    @GetMapping("/upload-page")
    public String getUploadPage() {
        return "<html>...</html>"; // 기존 HTML 내용 동일 유지
    }
}