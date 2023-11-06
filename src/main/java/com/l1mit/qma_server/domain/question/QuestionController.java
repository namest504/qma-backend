package com.l1mit.qma_server.domain.question;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.l1mit.qma_server.domain.question.dto.QuestionDetailResponse;
import com.l1mit.qma_server.domain.question.dto.QuestionRequest;
import com.l1mit.qma_server.domain.question.dto.QuestionResponse;
import com.l1mit.qma_server.domain.question.dto.QuestionSearchParam;
import com.l1mit.qma_server.global.common.MemberId;
import com.l1mit.qma_server.global.common.response.ApiResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> search(
            @PageableDefault Pageable pageable,
            QuestionSearchParam questionSearchParam) {
        final List<QuestionResponse> questionResponses = questionService.searchWithCondition(
                pageable, questionSearchParam);
        return ResponseEntity.status(OK)
                .body(ApiResponse.createSuccessWithData(questionResponses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionDetailResponse>> searchDetail(
            @PathVariable("id") Long questionId) {
        final QuestionDetailResponse questionDetailResponse = questionService.getDetail(questionId);

        return ResponseEntity.status(OK)
                .body(ApiResponse.createSuccessWithData(questionDetailResponse));
    }
}
