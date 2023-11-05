package com.l1mit.qma_server.domain.question;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.repository.MemberRepository;
import com.l1mit.qma_server.domain.question.dto.QuestionRequest;
import com.l1mit.qma_server.domain.question.dto.QuestionResponse;
import com.l1mit.qma_server.domain.question.dto.QuestionSearchParam;
import com.l1mit.qma_server.domain.question.repository.QuestionRepository;
import com.l1mit.qma_server.global.common.domain.MbtiEntity;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;

    public QuestionService(MemberRepository memberRepository,
            QuestionRepository questionRepository) {
        this.memberRepository = memberRepository;
        this.questionRepository = questionRepository;
    }

    @Transactional
    public Question save(Long memberId, QuestionRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new QmaApiException(ErrorCode.NOT_FOUND));
        return questionRepository.save(Question.builder()
                .member(member)
                .content(request.content())
                .receiveMbtiEntity(MbtiEntity.builder()
                        .attitude(request.attitude())
                        .perception(request.perception())
                        .decision(request.decision())
                        .lifestyle(request.lifestyle())
                        .build())
                .build());
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> searchWithCondition(Pageable pageable,
            QuestionSearchParam param) {
        return questionRepository.searchWithCondition(pageable, param);
    }
}
