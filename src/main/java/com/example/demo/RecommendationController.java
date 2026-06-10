package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * [역할: REST API 설계]
 * - 프론트엔드(React)와 소통하는 백엔드의 공식적인 주소 창구
 * [역할: React ↔ Spring 연동]
 * - @CrossOrigin 설정을 통해 리액트와 스프링 서버 간의 CORS(접속 차단) 문제 사전 해결
 */
@RestController
@RequestMapping("/api/recommendations") // API 공통 기본 주소
@CrossOrigin(origins = "*") 
public class RecommendationController {

    private final RecommendationService recommendationService;

    // 생성자 주입을 통한 핵심 로직 레이어 연결
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * [역할: 사용자 입력 및 이미지 업로드 처리]
     * [역할: Swagger 문서화 반영 영역]
     * - 향후 라이브러리 추가 시 웹 화면에 API 설명서가 자동으로 생성되도록 정석대로 설계
     */
    @PostMapping("/upload") // 데이터 수신용 세부 엔드포인트
    public Map<String, Object> uploadFile(@ModelAttribute UploadRequestDto requestDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // [역할: 응답 가공 및 핵심 로직 위임]
            // 컨트롤러는 요청 수신만 담당하며, 실제 데이터 처리는 서비스(Service) 클래스로 위임
            Map<String, Object> result = recommendationService.processRecommendation(requestDto);
            
            // 처리 결과를 포맷팅하여 최종 JSON 데이터 형태로 반환
            response.put("status", "success");
            response.putAll(result);
            
        } catch (Exception e) {
            // [역할: 예외 처리 검증]
            // 프로세스 중 예외 발생 시 서버 다운을 방지하고 에러 원인을 클라이언트에 안전하게 반환
            response.put("status", "fail");
            response.put("error", e.getMessage());
        }
        
        return response;
    }

    /**
     * [역할: API 테스트 및 통합 테스트 지원용 임시 페이지]
     * ⚠️ [추후 기능 구현 및 연동 완료 후 삭제 예정]
     * - 프론트엔드 화면 연동 전, 백엔드 단독 데이터 송수신 테스트용 내부 웹 페이지
     * - 화면 전환 없이 연속적인 데이터 전송 및 결과 확인이 가능하도록 구현
     */
    @GetMapping("/upload-page")
    public String getUploadPage() {
        return """
               <html>
               <head>
                 <meta charset="UTF-8">
                 <title>WareLens 백엔드 연속 테스트</title>
               </head>
               <body style="font-family: sans-serif; max-width: 600px; margin: 40px auto; padding: 20px; border: 1px solid #ccc; border-radius: 8px;">
                 <h2>📸 WareLens 통합 데이터 연속 테스트 (백엔드 전용)</h2>
                 <p style="color: #666; font-size: 14px;">화면 전환 없이 여러 번 연속으로 사진을 올려서 결과를 확인할 수 있습니다.</p>
                 <hr/>
                 
                 <form id="uploadForm" enctype="multipart/form-data">
                   <p><label><b>1. 성별:</b> </label><input type="text" name="gender" value="남성" style="padding:5px;"/></p>
                   <p><label><b>2. 스타일:</b> </label><input type="text" name="style" value="스트릿" style="padding:5px;"/></p>
                   <p><label><b>3. 옷 사진:</b> </label><input type="file" name="file" required /></p>
                   <button type="submit" style="padding: 10px 20px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;">백엔드로 전송</button>
                 </form>
                 
                 <hr style="margin-top:30px;"/>
                 <h3>📊 백엔드 반환 결과 (실시간)</h3>
                 <div id="loading" style="display:none; color: blue; font-weight: bold;">⏳ AI 파이프라인 작동 중... 잠시만 기다려주세요...</div>
                 <pre id="resultBox" style="background: #f4f4f4; padding: 15px; border-radius: 4px; overflow-x: auto; min-height: 50px; font-weight: bold; color: #333;">결과가 여기에 표시됩니다.</pre>
                 
                 <script>
                   document.getElementById('uploadForm').addEventListener('submit', function(e) {
                     e.preventDefault();
                     
                     var form = document.getElementById('uploadForm');
                     var formData = new FormData(form);
                     
                     document.getElementById('loading').style.display = 'block';
                     document.getElementById('resultBox').innerText = '분석 중...';
                     
                     fetch('/api/recommendations/upload', {
                       method: 'POST',
                       body: formData
                     })
                     .then(function(res) { return res.json(); })
                     .then(function(data) {
                       document.getElementById('loading').style.display = 'none';
                       document.getElementById('resultBox').innerText = JSON.stringify(data, null, 2);
                     })
                     .catch(function(err) {
                       document.getElementById('loading').style.display = 'none';
                       document.getElementById('resultBox').innerText = '에러 발생: ' + err;
                     });
                   });
                 </script>
               </body>
               </html>
               """;
    }
}