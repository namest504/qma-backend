package com.l1mit.qma_server.domain.member.domain;

import com.l1mit.qma_server.domain.member.domain.enums.Role;
import com.l1mit.qma_server.domain.member.dto.MemberInfoResponse;
import com.l1mit.qma_server.domain.question.Question;
import com.l1mit.qma_server.global.common.domain.AuditEntity;
import com.l1mit.qma_server.global.common.domain.MbtiEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_role", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "role")
    private List<Role> role;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Embedded
    private Oauth2Entity oauth2Entity;

    @Embedded
    private AuditEntity auditEntity;

    @Embedded
    private MbtiEntity mbtiEntity;

    @OneToMany(mappedBy = "member")
    private List<Question> questions;

    @Builder
    public Member(Oauth2Entity oauth2Entity) {
        this.oauth2Entity = oauth2Entity;
        this.nickname = oauth2Entity.getAccountId();
        this.role = new ArrayList<>(List.of(Role.ROLE_MEMBER));
        this.auditEntity = new AuditEntity();
    }

    public void updateMbtiEntity(MbtiEntity mbtiEntity) {
        this.mbtiEntity = mbtiEntity;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<SimpleGrantedAuthority> getRole() {
        return role.stream()
                .map(Role::name)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public MemberInfoResponse toMemberResponse() {
        return new MemberInfoResponse(oauth2Entity.getSocialProvider(), mbtiEntity);
    }
}
