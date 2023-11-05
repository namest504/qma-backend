package com.l1mit.qma_server.domain.question;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.l1mit.qma_server.domain.question.dto.QuestionRequest;
import com.l1mit.qma_server.global.common.MemberId;
import com.l1mit.qma_server.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/question")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> insert(@MemberId Long memberId,
            @RequestBody QuestionRequest questionRequest) {
        final Question saved = questionService.save(memberId, questionRequest);
        return ResponseEntity.status(NO_CONTENT)
                .body(ApiResponse.createSuccess());
    }
}
