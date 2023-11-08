package com.l1mit.qma_server.domain.answer;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.l1mit.qma_server.domain.answer.dto.AnswerRequest;
import com.l1mit.qma_server.global.common.MemberId;
import com.l1mit.qma_server.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/answer")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@RequestBody AnswerRequest answerRequest,
            @MemberId Long memberId) {
        answerService.create(answerRequest, memberId);
        return ResponseEntity.status(NO_CONTENT)
                .build();
    }
}
