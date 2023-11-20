package com.l1mit.qma_server.domain.answer.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.l1mit.qma_server.domain.answer.dto.request.AnswerRequest;
import com.l1mit.qma_server.domain.answer.dto.response.AnswerResponse;
import com.l1mit.qma_server.domain.answer.service.AnswerService;
import com.l1mit.qma_server.global.common.MemberId;
import com.l1mit.qma_server.global.common.response.ApiResponse;
import com.l1mit.qma_server.global.common.response.PageResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/answer")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(final AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@RequestBody @Valid AnswerRequest answerRequest, @MemberId Long memberId) {

        answerService.create(answerRequest, memberId);

        return ResponseEntity.status(NO_CONTENT)
                .build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<List<AnswerResponse>>>> getPagedAnswers(@PageableDefault Pageable pageable, @RequestParam("id") Long questionId) {

        Page<AnswerResponse> responses = answerService.findPagedAnswerByQuestionId(pageable, questionId);

        return ResponseEntity.status(OK)
                .body(ApiResponse.createSuccessWithData(
                        PageResponse.create(
                                responses.getContent(),
                                responses.getNumberOfElements(),
                                responses.getTotalPages())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> questionRemove(@PathVariable("id") Long answerId, @MemberId Long memberId) {

        answerService.deleteById(answerId, memberId);

        return ResponseEntity.status(NO_CONTENT)
                .body(ApiResponse.createSuccess());
    }
}
