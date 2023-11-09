package com.l1mit.qma_server.domain.question.repository;

import com.l1mit.qma_server.setting.jpa.QueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class QuestionCustomRepositoryImplTest {

    @Autowired
    QuestionRepository questionRepository;

    @Nested
    @DisplayName("searchWithCondition 메소드는")
    class searchWithCondition {

        @Test
        @DisplayName("성공한다.")
        void success() throws Exception {
            //given

            //when

            //then
        }
    }
}