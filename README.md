# QmA 개인 프로젝트

## 목적

서로의 MBTI에 대한 이해를 넓히고, 사람들이 서로를 이해하고 소통하는 데 도움이 되는 플랫폼을 제공하는 것을 목표로 합니다.

## 주요 기능

- OIDC 인증
  - Kakao, Google, Apple 지원 가능
- 소켓 채팅
  - STOMP Intercepter, ErrorHandler 구현
  - EventListener를 통한 세션 관리
- 게시글, 댓글 CRUD
  - 조회 목록 페이징 쿼리 최적화

## CI/CD & 아키텍처 구조

![qma아키텍처](https://github.com/question-mbti-answer/qma-backend/assets/61047602/fa3fc55a-c6e7-4a91-865e-fc4c048fa366)

## ERD 다이어그램

![QmA (1)](https://github.com/question-mbti-answer/qma-backend/assets/61047602/577af59f-ecb6-4cb8-bea2-a8f2aacf598f)

## API 명세서

[API 명세서 링크](https://list-api.link/docs/swagger-ui/index.html)

## 기술 선택 과정

### Caffeine Cache

인증 과정에서 OIDC 키 조회 과정이 필수적으로 수행되어야 하며, 이 과정이 다른 캐시 기능보다 빠른 속도를 요구한다고 판단하여 선택하였다.

### Redis

채팅, 세션의 특성상 조회, 입력 과정이 더 많을 것으로 판단하여 Key/Value 타입 NOSQL인 Redis 사용.

### Rest Docs & Swagger UI

Rest Docs의 특성상 테스트가 필수적으로 요구되므로 신뢰성 있는 Rest API 작성이 가능하다.
다만, Rest Docs의 기본 adoc 의 가독성의 단점과 실제 API 테스트를 할 수 없다는 점에서 Swagger UI를 적용하여 사용하게 되었다.


## 트러블 슈팅 기록

### 테스트 환경에서 JpaAudit이 적용 안되는 경우

```java
@Configuration  
@EnableJpaAuditing  
public class JpaConfig {  
}
```

Configuration 빈을 등록해주고

테스트 환경에서

```java
@Import(JpaConfig.class)  
class test{
//...
}
```
Import를 통해 Audit이 필요한 테스트에서 활용하면 해결된다.

### 배포 환경에서 API 명세서 빌드가 안되는 경우

resources 경로에 static/docs 경로가 존재해야 open api 명세서가 생성되는 경우였다.
Repository 에 Push 할 때에는 docs 디렉토리에 명세서가 존재하지 않고 배포 환경에서 build를 통해 생성되도록 해뒀었다.
하지만 Repository 에 올라갈 때 해당 경로에 파일이 존재하지 않을 경우 디렉토리가 존재하지 않게 되어 원하는 대로 명세서가 생성되지 않는 경우가 발생했다.
해당 문제를 해결하기 위해 docs 경로에 .keep 파일을 생성하여 디렉토리가 유지되도록 설정하여 해결하였다.

### QueryParam String이 null로 안받아지는 경우

```text
http://server.com?cond1=&cond2=
```
해당 방식을 통해 query param을 입력받을 경우 cond1 과 cond2는 null로 처리되는줄 알았다.
하지만 실제 입력에서는 "" 과 같은 비어있는 문자열로 입력되어 해당 이슈가 발생한 검색 기능에서 동적 쿼리가 원하는 대로 동작하지 않는 경우가 발생했다.
원하는 대로 동작하려면

```text
http://server.com
```
위와 같이 query param 내용 자체가 비어있는 상태로 전달이 되어야하는데 이는 요청 자체를 신뢰해야 하는 상황이 발생된다.

```java
@Component
public class StringToNullConverter implements Converter<String, String> {
    @Override
    public String convert(String source) {
        return source.isEmpty() ? null : source;
    }
}
```

String이 비어있는 "" 값이면 null로 변환하는 Converter를 구현해주고

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    private final StringToNullConverter stringToNullConverter;

    public WebConfig(StringToNullConverter stringToNullConverter) {
        this.stringToNullConverter = stringToNullConverter;
    }

    // ...
    
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToNullConverter);
    }
}
```

WebMvcConfigurer 구현체에 등록해서 해결하였다.


### 재배포시 CI/CD 환경에서 발생하는 원인 해결

문제의 상황은 새로 배포를 진행 할 경우 이전 도커 컨테이너와 이름이 같은 프로젝트가 배포되어있어 새로 배포가 안되는 상황이였다.
-rm 옵션을 설정하여 stop 커맨드를 통해 새로 배포를 할 때 이전 컨테이너의 삭제 후 새로 배포된 컨테이너 등록을 예상하였지만
이전 컨테이너가 내려가는 도중에 새로운 컨테이너가 올라가는 문제가 발생하였던 것이였다.
이를 해결하기 위해 wait 커맨드를 통해 해당하는 Docker 컨테이너가 exit code를 반환 할 때 까지 대기하도록 하여 해결하였다.


## 테스트 환경 기록

### Mock Security Filter 구현

RestDocs을 사용하기 위해서 MockMvc를 통해 컨트롤러 계층을 테스트가 필요했다.
Spring Securty를 통해 Filter를 통해 인증을 처리해주는 과정으로 인해
테스트 환경에서 컨트롤러 테스트시 실패하는 상황이 발생했다.
RestDocsController 부모 클래스를 구현하기 전에 MockFilter를 다음과 같이 구현하였다.
```java
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
    SecurityContext context = SecurityContextHolder.getContext();

    PrincipalUser principalUser = new PrincipalUser(createMember());
    context.setAuthentication(
            new UsernamePasswordAuthenticationToken(principalUser, principalUser.getPassword(),
                    principalUser.getAuthorities()));

    chain.doFilter(request, response);
}
```
```java
private static Member createMember() {
    Oauth2Entity oauth2Entity = Oauth2Entity.builder()
            .accountId("1111")
            .socialProvider(SocialProvider.GOOGLE)
            .build();
    Member member = Member.builder()
            .oauth2Entity(oauth2Entity)
            .build();

    Class<Member> memberClass = Member.class;
    try {
        Field id = memberClass.getDeclaredField("id");
        id.setAccessible(true);
        id.set(member, 1L);
    } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new RuntimeException(e);
    }
    return member;
}
```
Member 클래스의 ID는 JPA를 통해 지정되므로 Reflection을 통해 지정해주었다.

### RestDocsController

각 Controller의 테스트는 RestDocs를 생성하기 위해 테스트를 진행할 것이기에 상속을 통해 구현해뒀다.
공통적으로 사용되는 Controller 계층 요청시 Securty Filter를 적용하고, 요청시 JSON으로 변환하기 위한 ObjectMapper 클래스를 미리 의존성을 부여해뒀다.
```java
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@WebMvcTest
public class RestDocsControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    protected String toJson(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }

    @BeforeEach
    public void setMockMvc(WebApplicationContext context,
            RestDocumentationContextProvider provider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(
                        documentationConfiguration(provider)
                                .uris()
                                .withScheme("http")
                                .withHost("127.0.0.1")
                                .withPort(8080))
                .apply(springSecurity(new MockSecurityFilter()))
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .alwaysDo(document("api/v1"))
                .build();
    }
}
```

### ValidationTest

Request로 들어오는 값을 검증하는 테스트를 위해 부모 클래스로 ValidationTest를 구현했다.
```java
public class ValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public Set<ConstraintViolation<Object>> getConstraintViolations(Object object) {
        return validator.validate(object);
    }
}
```

### MockWebServer

OIDC 인증을 과정을 테스트 하기 위해서는 외부 서비스에 API 요청을 해야하는 경우가 발생했다.
테스트는 외부 환경에 영향을 받으면 안되기에 MockWebServer를 통해 테스트를 진행하였다.

MockWebServer는 실제 외부에 요청을 한 것처럼 테스트를 수행할 수 있는데,
Queue에 원하는 응답값을 담아 요청 로직을 수행하면 실제 응답과 같이 받을 수 있게된다.45p
## 학습 내용

### 테스트 하기 좋은 코드를 작성하자

이전 프로젝트들의 공통적인 단점을 찾아 스스로 고민한 결과 첫 번째로 테스트 코드를 작성하지 않는 경우가 많았다.
왜 그랬는가?
**단순히 단위 테스트 코드를 작성하는게 어려웠다.**
하나의 메소드에 전부 동작하도록 구현을 해왔었기에 단위 테스트 코드를 작성하기에 Mock을 남발하게 되었고,
이는 기하급수적인 코드의 복잡함을 유발하여 한 주만 지나도 이게 뭐지 싶은 상황이 발생하게 되었다.

그래서 테스트 코드를 쉽게 짜는 방법을 찾아봤다.
정답은 하나의 메소드는 하나의 목적을 가지도록 구현하는 방법이였다.
컨트롤러를 지나 서비스 계층으로 들어오면 복잡한 얽인 비즈니스 로직의 메소드들을 하나의 목적으로 구현하여 작성을 해두니 실제로 단위 테스트를 작성하는게 매우 쉬워졌다.