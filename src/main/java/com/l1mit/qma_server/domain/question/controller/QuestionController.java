package com.l1mit.qma_server.domain.question.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.l1mit.qma_server.domain.question.domain.Question;
import com.l1mit.qma_server.domain.question.dto.param.QuestionSearchParam;
import com.l1mit.qma_server.domain.question.dto.request.QuestionRequest;
import com.l1mit.qma_server.domain.question.dto.response.QuestionDetailResponse;
import com.l1mit.qma_server.domain.question.dto.response.QuestionResponse;
import com.l1mit.qma_server.domain.question.service.QuestionService;
import com.l1mit.qma_server.global.common.MemberId;
import com.l1mit.qma_server.global.common.response.ApiResponse;
import com.l1mit.qma_server.global.common.response.PageResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
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
            @RequestBody @Valid QuestionRequest questionRequest) {
        final Question saved = questionService.save(memberId, questionRequest);
        return ResponseEntity.status(NO_CONTENT)
                .body(ApiResponse.createSuccess());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<List<QuestionResponse>>>> search(
            @PageableDefault Pageable pageable,
            QuestionSearchParam questionSearchParam) {
        final Page<QuestionResponse> questionResponses = questionService.searchWithCondition(
                pageable, questionSearchParam);
        return ResponseEntity.status(OK)
                .body(ApiResponse.createSuccessWithData(
                        PageResponse.create(
                                questionResponses.getContent(),
                                questionResponses.getNumberOfElements(),
                                questionResponses.getTotalPages())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionDetailResponse>> searchDetail(
            @PathVariable("id") Long questionId) {
        final QuestionDetailResponse questionDetailResponse = questionService.getDetail(questionId);

        return ResponseEntity.status(OK)
                .body(ApiResponse.createSuccessWithData(questionDetailResponse));
    }
}
