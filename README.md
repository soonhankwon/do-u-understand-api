# SELFnews 백엔드 API

- 자신이 발행한 포스트를 정기적으로 이메일로 받아볼 수 있는 웹 애플리케이션 백엔드 API 입니다.
- SELFnews는 메모앱에서 수 많은 공부 메모를 작성하지만, 정작 리뷰는 귀찮아 공부한 내용을 까먹는 문제를 해결하고자 만든 시스템입니다.
- SELFnews의 핵심 기능은 2가지 입니다.
    - 포스트 작성, 발행, 구독 기능
    - 구독 포스트 정기적 이메일 발송 기능
- 결론적으로 자신이 등록한 포스트를 주기적인 이메일 알람을 통해 리마인드 시키는 것으로 지식을 까먹지 않도록 도움을 주는 웹 애플리케이션 백엔드 API 입니다.

## Table Of Contents

- [배포](#배포)
- [기술스택](#기술스택)
- [아키텍처](#아키텍처)
- [API 명세서](#api-명세서)
- [ERD](#erd)
- [핵심문제 해결과정 및 전략](#핵심문제-해결과정-및-전략)
  <br/>

## 배포

- Link: https://do-u-understand-web.vercel.app
- Frontend UI는 Svelte.js로 구현했습니다.
- Frontend Github Link: https://github.com/soonhankwon/self-news-web

## 기술스택

### 언어 및 라이브러리

- Java 21 Amazon Corretto
- SpringBoot 3.2.1
- Spring Data JPA 3.2.1
- Spring Validation 3.2.1
- Spring Data Redis 3.2.1
- Spring Mail 3.2.1
- Spring RestDocs 3.0.1
- Spring Security 6.2.1
- JJWT 0.12.3
- JUnit5, Mockito
- Jacoco 0.8.11
- Apache Jmeter

### 데이터베이스

- MySQL 8.0.33
- Redis 7.2.3

### DevOps

- AWS EC2
- AWS RDS
- AWS Route53
- AWS Certification Manager
- GitHub Actions
- Docker

## 아키텍처

![self-news-arch drawio](https://github.com/soonhankwon/self-news-api/assets/113872320/368836bd-80eb-4894-be0a-6d9f72714e26)

## api 명세서

- RestDocs: http://3.36.80.13/docs/api-document.html

## ERD

![self-news-erd](https://github.com/soonhankwon/self-news-api/assets/113872320/d4bfa599-8115-468d-9cf4-5e43fa8281c9)

## 핵심문제 해결과정 및 전략

### 테스트 데이터 셋

- 목표: 테스트를 통한 코드 및 쿼리 병목지점 파악 및 개선
- 가정: 다음과 같은 가정을 하고 테스트 데이터 셋을 생성했습니다.
- Mock 테스트 데이터 셋
    - 1차 목표 유저수: 1만명
    - 목표 DAU: 3천명(30%)
    - 1일 포스팅: 1인 1건
    - 서비스한 기간: 1달
    - 30 * 3000 = 90,000 포스트, 1인당 9포스트
    - 90,000 * 2 = 180,000 코멘트
    - 90,000 / 100 = 900 카테고리
    - 90,000 / 2 = 45,000 구독
- 결론: Thread 1, Loop 10,000 성능 테스트
    - DAU 3000 / 24 = 125 users per hour
    - 125 / 60 = 2.08 users per minute
    - 2.08 / 60 = 0.034 users per second
    - DAU가 3천명일 때 스레드를 1로 성능 테스트를 진행해도 무관하다고 예상
    - 이후 부하테스트를 진행하여 스레드 수를 늘려가며 별도의 테스트를 진행하도록 결정

### 포스트 목록 조회 API 성능 개선

- 기본적인 컬럼 인덱스가 모두 적용되어 있는 상태에서 단계적으로 성능을 향상시켰습니다.

#### 1차: 중복 쿼리 개선: 개선율 9.58%

- findByPostAndUser()에서 findByPost()로 변경
- 함수에서 findAllByUser() 로 페이지를 조회한 후 getPostDTO 함수에서 불필요하게 User를 사용하여 조회하고 있는 쿼리 개선

<details>
<summary><strong> 중복 쿼리 개선 CODE - Click! </strong></summary>
<div markdown="1">       

````java
public PostsGetResponse findPosts(String email, int pageNumber, String mode, String query) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

    Pageable pageable = PageRequest.of(pageNumber, 10);
    Page<Post> page;
    if (mode.

            equals(NOTICE_PARAM)) {
        page = postRepository.

                findAllByPostStatus(PostStatus.NOTICE, pageable);
    } else {
        // 이 조건 분기에서 유저의 포스트만 모두 찾아옴
        page = postRepository.

                findAllByUser(user, pageable);
    }

    int totalPages = page.getTotalPages();
    List<Post> postsByPage = page.getContent();
    List<PostDTO> postDTO;
    // logic .......
    // postDTO = getPostDTO(postsByPage, user);
    postDTO =

            getPostDTO(postsByPage);
    return PostGetResponse.

            of(totalPages, postDTO);
}

private List<PostDTO> getPostDTO(List<Post> posts) {
    return posts.stream()
            .map(post -> {
                Long commentCount = commentRepository.countAllByPost(post);
                // AndUser 불필요 -> 파라미터의 포스트는 모두 유저의 포스트
                // Optional<Subscribe> optionalSubscribe = subscribeRepository.findByPostAndUser(post, user);
                Optional<Subscribe> optionalSubscribe = subscribeRepository.findByPost(post);
                if (optionalSubscribe.isPresent()) {
                    return PostDTO.of(post, commentCount, true);
                }
                return PostDTO.of(post, commentCount, false);
            })
            .collect(Collectors.toList());
}
````

</div>
</details>

- 중복 쿼리 개선전
  <img width="882" alt="init-data" src="https://github.com/soonhankwon/self-news-api/assets/113872320/8c06220c-b68f-4910-8d7f-56b35912c23c">

- 중복 쿼리 개선 이후
  <img width="882" alt="query-refac-v1" src="https://github.com/soonhankwon/self-news-api/assets/113872320/f54bc5d3-438b-4b59-b36f-702cad110ffc">
    - TPS: 246.5/sec
    - Avg: 4
    - Max: 27
    - Error: 0.00%
    - 개선율: ((246.5 - 224.9) / 224.9) * 100 → 9.58%

#### 2차: Map을 활용한 getPostDTO 코드 리팩토링 + parallelStream : 개선율 52.8%

- getPostDTO 함수: DB 조회가 많이 일어나며, 프론트에 필수적인 값을 DTO로 변환해주는 로직

<details>
<summary><strong> getPostDTO Before CODE - Click! </strong></summary>
<div markdown="1">       

````java
private List<PostDTO> getPostDTO(List<Post> posts) {
    return posts.stream()
            .map(p -> {
                // 각 포스트의 코멘트 카운트를 조회한다.
                Long commentCount = commentRepository.countAllByPost(p);
                // 구독 레포지토리에서 포스트 리스트를 조회한다.
                Optional<Subscribe> optionalSubscribe = subscribeRepository.findByPost(p);
                // 포스트가 구독 테이블에 있다면 true, 없다면 false (isSubscribe)
                if (optionalSubscribe.isPresent()) {
                    return PostDTO.of(p, commentCount, true);
                } else {
                    return PostDTO.of(p, commentCount, false);
                }
            })
            .collect(Collectors.toList());
}
````

</div>
</details>

- 2-1 리팩토링: 코멘트 카운트와 구독 포스트 리스트 조회를 Stream을 활용하여 Map으로 만들어준다.
    - 일방향 해쉬 테이블 알고리즘에서 아이디어를 가져옴
    - map 루프에서 코멘트 카운트와 구독 쿼리를 보내는 것보다. 한번에 맵을 만들어 놓고 맵을 조회 O(1)해서 로직을 돌리는것이 효율적일것이라고 판단
    - 하지만, 해당 경우 TPS의 유의미한 변화는 없었다.
- 2-2 리팩토링: 맵을 만들어주는 로직을 parallelStream을 활용해 병렬처리
    - 병렬처리를 통해 최대한 빠르게 Map을 만들도록 설계
    - 포스트의 코멘트 수와 구독을 조회하여 맵을 만들어주는 로직이 독립적이기 때문에 에러가 검출되지 않을 것이라고 예상
    - 동시성 또는 스레드 풀 문제는 이후 부하 테스트에서 검증해야 할 것

<details>
<summary><strong> Map & parallelStream 활용 리팩토링 CODE - Click! </strong></summary>
<div markdown="1">       

````java
private List<PostDTO> getPostDTO(List<Post> posts) {
    Map<Post, Long> commentCounts = posts.parallelStream()
            .collect(Collectors.toMap(post -> post, commentRepository::countAllByPost));
    Map<Post, List<Subscribe>> subscribesByPost = posts.parallelStream()
            .collect(Collectors.toMap(post -> post, subscribeRepository::findAllByPost));

    return posts.stream()
            .map(post -> {
                Long commentCount = commentCounts.get(post);
                List<Subscribe> subscribes = subscribesByPost.get(post);
                boolean isSubscribed = subscribes.stream()
                        .anyMatch(subscribe -> Objects.equals(subscribe.getPost().getId(), post.getId()));
                return PostDTO.of(post, commentCount, isSubscribed);
            })
            .collect(Collectors.toList());
}
````

</div>
</details>

- Map & parallelStream 활용 리팩토링 성능 데이터
  <img width="882" alt="map-parallel-stream" src="https://github.com/soonhankwon/self-news-api/assets/113872320/29ca7cae-d961-4015-bae3-63f3896277eb">
    - TPS: 522.5/sec
    - Avg: 1
    - Max: 25
    - Error: 0.00%
    - 개선율: ((522.5 - 246.5) / 522.5) * 100 → 약 52.8%

### 카테고리 포스트 목록 조회 API 성능 개선 - 쿼리 파라미터

- 기존 모든 유저의 데이터를 가져와 stream filter를 활용하여 리턴하던 로직을 삭제하고 findAllByUserAndCategory_Name으로 직접 해당 데이터만 가져오도록 개선했습니다.

<details>
<summary><strong> 리팩토링 전 CODE - Click! </strong></summary>
<div markdown="1">       

````java
public PostsGetResponse findPosts(String email, int pageNumber, String mode, String query) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

    Pageable pageable = PageRequest.of(pageNumber, 10);
    Page<Post> page;
    if (mode.equals(NOTICE_PARAM)) {
        page = postRepository.findAllByPostStatus(PostStatus.NOTICE, pageable);
    } else {
        page = postRepository.findAllByUser(user, pageable);
    }

    int totalPages = page.getTotalPages();
    List<Post> postsByPaging = page.getContent();
    List<PostDTO> postDTO;
    // query param이 들어오는 경우 스트림 filter로 처리하고 있다.
    if (query != null && !query.isEmpty()) {
        List<Post> postsByFiltered = postsByPaging.stream()
                .filter(p -> p.getCategory().getName().equals(query))
                .collect(Collectors.toList());
        postDTO = getPostDTO(postsByFiltered, user);
        return PostGetResponse.of(totalPages, postDTO);
    }
    postDTO = getPostDTO(postsByPaging, user);
    return PostGetResponse.of(totalPages, postDTO);
}
````

</div>
</details>

#### 코드 리팩토링 & 카테고리 이름으로 알맞은 데이터만 조회해서 사용 : 개선율 7.78%

- 불필요하게 유저의 모든 카테고리 데이터를 가져와 filter함으로 효율성이 떨어지고 메모리 사용량이 많다는 것을 인식
- 코드 가독성 또한 떨어짐

<details>
<summary><strong> 리팩토링 후 CODE - Click! </strong></summary>
<div markdown="1">       

````java
public PostsGetResponse findPosts(String email, int pageNumber, String mode, String query) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_EXIST_USER_EMAIL));

    Pageable pageable = PageRequest.of(pageNumber, 10);
    Page<Post> page;
    // 분기에서 조건별로 page를 리턴한다.
    if (mode.equals(NOTICE_PARAM)) {
        page = postRepository.findAllByPostStatus(PostStatus.NOTICE, pageable);
    } else if (query != null && !query.isEmpty()) {
        // Query Param이 포함된 경우 Where - Category_Name으로 바로 조회한다.
        page = postRepository.findAllByUserAndCategory_Name(user, query, pageable);
    } else {
        page = postRepository.findAllByUser(user, pageable);
    }
    // filter 로직 삭제
    int totalPages = page.getTotalPages();
    List<Post> postsByPage = page.getContent();
    List<PostDTO> postDTO = getPostDTO(postsByPage);
    return PostGetResponse.of(totalPages, postDTO);
}
````

</div>
</details>

- 코드 리팩토링 및 쿼리 개선 전
  <img width="882" alt="query_param_init" src="https://github.com/soonhankwon/self-news-api/assets/113872320/19811fef-5c5a-44d6-83ce-13ec8f8ece65">

- 코드 리팩토링 및 쿼리 개선 후
  <img width="882" alt="query_param_v1" src="https://github.com/soonhankwon/self-news-api/assets/113872320/a61938a0-012d-4117-9139-81ea80ab35fb">
    - TPS: 502.7/sec
    - Avg: 1
    - Max: 18
    - Error: 0.00%
    - 개선율: ((502.7 - 463.9) / 502.7) * 100 → 약 7.78% 개선율
- DB에서 한번에 가져오는 데이터량이 줄었고, TPS 또한 개선되었습니다.

### 정기 구독 이메일 발송 성능 개선

- 기존 정기 구독 이메일 발송 로직: DB에서 유저의 모든 구독 포스트 데이터를 조회하여 자바 로직으로 데이터를 추출
- 해당 로직에서 DB에서 데이터를 비효율적으로 많이 가져와 속도가 나오지 않는 문제를 인식
- 1차 쿼리 개선을 통해 기존 11분 27초에서 6분 45초로 약5분 가량 속도를 개선시켰습니다.
- 이후 parallelStream을 활용한 병렬처리로 6분 45초에서 12초로 약6분 속도를 개선되었지만, 외부 이메일 전송 API가 병렬적으로 작업을 처리하지 못해 예외가 발생하여 hotfix
- 2차 쿼리 개선을 통해 6분 45초에서 6분 11초로 개선
- 1차, 2차 개선을 통해 11분 27초에서 6분 11초로 약 5분 성능 개선 (총 개선율 약 46.40%)

#### 1차 쿼리 개선 - 개선율 40.83%

<details>
<summary><strong> 기존 정기 구독 이메일 발송 로직 CODE - Click! </strong></summary>
<div markdown="1">       

````java
private void sendPriorityPostsByEmail(List<User> users) {

    users.forEach(u -> {
        List<Subscribe> subscribes = subscribeRepository.findAllByUser(u);
        // 알람 신청한 지식중 알람 카운터가 가장 적은것을 하나 전송한다(Round Robin)
        subscribes.stream()
                .map(Subscribe::getPost)
                .min(Comparator.comparing(Post::getNotificationCount))
                .ifPresent(post -> {
                    post.increaseNotificationCount();
                    sendMockEmail(u, post);
                });
    });
}

// 테스트용 Mock method
private void sendMockEmail(User user, Post post) {
    try {
        Thread.sleep(10L);
        // 재시도 로직 테스트시 사용
        // if (user.getId() < 100L) {
        // throw new RuntimeException() };
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }

}
````

</div>
</details>

<details>
<summary><strong> 쿼리(JPQL) CODE - Click! </strong></summary>
<div markdown="1">       

````java

@Query("SELECT s.post FROM Subscribe s WHERE s.user = :user ORDER BY s.post.notificationCount ASC ")
Page<Post> findPostWithMinNotificationCount(@Param("user") User user, Pageable pageable);
````

</div>
</details>

<details>
<summary><strong> 적용된 CODE - Click! </strong></summary>
<div markdown="1">       

````java
private void sendPriorityPostsByEmail(List<User> users) {
    users.forEach(u -> {
        // 알람 신청한 지식중 알람 카운터가 가장 적은것을 하나 전송한다(Round Robin)
        Page<Post> postPage = postRepository.findPostWithMinNotificationCount(user,
                PageRequest.of(0, 1));
        List<Post> posts = postPage.getContent();
    });
    if (posts.isEmpty()) {
        return;
    }
    Post minNotificationCountPost = posts.getFirst();
    minNotificationCountPost.increaseNotificationCount();
    sendMockEmail(user, minNotificationCountPost);
}
````

</div>
</details>

- 쿼리 개선 전
  <img width="882" alt="noti_init" src="https://github.com/soonhankwon/self-news-api/assets/113872320/89b3ee0c-5b11-4c1d-94e2-50086f4b2844">
- 1차 쿼리 개선 후
  <img width="882" alt="noti_query_v1" src="https://github.com/soonhankwon/self-news-api/assets/113872320/b6aaaa7c-7c74-4e3f-86ac-7b68d0543c35">
    - TPS: 8.9/h
    - Avg: 405154
    - Max: 405154
    - Error: 0.00%
    - 개선율: ((684956 - 405154) / 684956) * 100 → 약 40.84% 개선율

#### 2차 쿼리 개선 - 개선율 약 9.39%

- 알람수신 허용은 했지만, 구독포스트가 없는 유저들도 조회되는 문제가 발생했습니다.
- 기존 로직에서는 자바 코드로 구독포스트가 없는 유저를 판별하여 비효율적인 연산이 들어가 이 부분을 개선하면 속도가 개선될 것이라고 판단
- 유저와 구독 테이블을 조인해서 결과를 한번에 가져오도록 수정

<details>
<summary><strong> 기존 쿼리 CODE - Click! </strong></summary>
<div markdown="1">       

````java

@Scheduled(cron = "0 0 20 * * *")
public void sendUnderstandNotificationInEvening() {
    //수신허용을 했지만, 구독포스트가 없는 유저들도 조회되는 문제 발생
    List<User> users = findUserByAllowedNotification();
    sendPriorityPostsByEmail(users);
}

private List<User> findUserByAllowedNotification() {
    return userRepository.findAllByIsAllowedNotification(true);
}
````

</div>
</details>

<details>
<summary><strong> 개선 쿼리(JPQL) CODE - Click! </strong></summary>
<div markdown="1">       

````java
// 구독포스트가 없다면 결과에서 제외된다.
@Query(value = "SELECT u FROM User u JOIN Subscribe s ON u.id = s.user.id WHERE u.isAllowedNotification = true ")
List<User> findAllByIsAllowedNotificationExistsSubscribe();
````

</div>
</details>

- 2차 쿼리 개선 후
- 불필요한 데이터를 DB에서 조회하지 않아 메모리에서 처리할 데이터량이 감소(테스트에서 케이스 1,000건)
  <img width="882" alt="noti_query_v2" src="https://github.com/soonhankwon/self-news-api/assets/113872320/1a53fd39-917d-4937-a10d-35a4929eff57">
    - TPS: 9.8/h
    - Avg: 367076
    - Max: 367076
    - Error: 0.00%
    - 개선율: ((405154 - 367076)/ 405154) * 100 → 약 9.39% 개선율

### 이메일 알람 발송 예외 발생시 재시도 스케쥴링 및 결과 보고서 웹훅 로직 개발

- 이메일 알람 발송시 실패한 알람의 경우 처리를 꼭 해줘야하는 문제를 인식했습니다.
- 예를 들어 이메일 발송 루프에서 중간에 예외가 발생한다면 예외 발생 이후 이메일에는 모두 알람 발송이되지 않습니다.
- 따라서 Try-Catch로 예외처리를 해주어 이 문제를 해결했고, 실패 이벤트를 발행해 재시도 Set에 이벤트를 넣어주었습니다.
    - 이벤트 리스너에서 실패 이벤트를 재시도 Set에 넣어주는 메서드는 비동기처리(Async)하여 기존 로직과 흐름을 분리, 이메일 발송 로직에 영향을 최대한 미치지 않도록 합니다.
- 각 알람발송 시점 30분 후 재시도 Set에 재시도할 실패 이벤트가 있다면 이메일을 재발송하도록 스케줄링했습니다.
- 바로 실패한 요청을 retry하지 않는 이유 두 가지 입니다.
    - 실패한 요청은 이메일 자체가 문제가 있을 수 있기 때문에 또 실패할 가능성이 높다고 예상됨
    - 외부 API(이메일)의 문제일 경우 일정 시간 후 API의 장애가 회복되기를 기대할 수 있기 때문
- 부가적으로 정기 알람 발송 결과와 재시도 알람의 결과 보고서를 바로 파악하기 위해서 보고서 웹훅 전송 로직을 추가했습니다.
    - 1차 결과 보고서: 성공한 알람 갯수, 구독하지 않은 유저의수, 실패한 알람 갯수
    - 2차 재시도 결과 보고서: 실패한 알람 갯수, 실패한 이메일 리스트

<details>
<summary><strong> 이메일 알람 발송 재시도 처리 및 웹훅 전송 CODE - Click! </strong></summary>
<div markdown="1">       

````java
private void sendPriorityPostsByEmail(List<User> users) {
    // 실패한 email을 파악하기 위해 map 생성
    Map<String, Boolean> map = users.parallelStream()
            .collect(Collectors.toMap(User::getEmail, user -> false));

    AtomicInteger noneSubscribeUserCountRef = new AtomicInteger();
    AtomicInteger successNotificationCountRef = new AtomicInteger();
    users.parallelStream().forEach(user -> {
        // 알람 신청한 지식중 알람 카운터가 가장 적은것을 하나 전송한다(Round Robin)
        Page<Post> postPage = postRepository.findPostWithMinNotificationCount(user,
                PageRequest.of(0, 1));
        List<Post> posts = postPage.getContent();
        String email = user.getEmail();
        // posts가 비어있다면 구독한 포스트가 없다. retry 리스트에 포함되면 안되는 케이스
        if (posts.isEmpty()) {
            map.put(email, true);
            noneSubscribeUserCountRef.getAndIncrement();
            return;
        }
        try {
            Post minNotificationCountPost = posts.getFirst();
            minNotificationCountPost.increaseNotificationCount();
            sendEmail(user, minNotificationCountPost);
        } catch (Exception ex) {
            log.warn(
                    String.format(
                            "cause={%s} msg={%s}",
                            ex.getCause(), ex.getMessage()
                    )
            );
            return;
        }
        map.put(email, true);
        successNotificationCountRef.getAndIncrement();
    });

    AtomicInteger failNotificationCountRef = new AtomicInteger();
    // 맵에 false로 남아있다면 failEvent
    map.keySet().forEach(failUserEmail -> {
        Boolean isNotified = map.get(failUserEmail);
        if (!isNotified) {
            failNotificationCountRef.getAndIncrement();
            eventPublisher.publishEvent(new NotificationFailEvent(failUserEmail));
        }
    });
    map.clear();

    // 이메일 알람 결과 리포트 웹훅 전송 로직
    int successNotificationCount = successNotificationCountRef.get();
    int noneSubscribeUserCount = noneSubscribeUserCountRef.get();
    int failNotificationCount = failNotificationCountRef.get();
    String reportContent = getEmailNotificationResultReport(successNotificationCount,
            noneSubscribeUserCount, failNotificationCount);

    discordUtils.sendDiscordWebhook(DiscordWebhookRequest.of(reportContent, discordServerUrl));
}
````

</div>
</details>

<details>
<summary><strong> 이메일 알람 실패 이벤트 리스너 CODE - Click! </strong></summary>
<div markdown="1">       

````java

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationFailEventListener {

    @Value("${discord.webhook.server-url}")
    private String discordServerUrl;

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final EmailUtils emailUtils;
    private final DiscordUtils discordUtils;
    private final Set<NotificationFailEvent> retrySet = new HashSet<>();

    @Async
    @EventListener
    public void handleNotificationFailEvent(NotificationFailEvent notificationFailEvent) {
        retrySet.add(notificationFailEvent);
    }

    @Scheduled(cron = "0 30 08 * * *")
    public void retryNotificationInMorning() {
        retryNotification();
    }

    @Scheduled(cron = "0 30 13 * * *")
    public void retryNotificationInAfternoon() {
        retryNotification();
    }

    @Scheduled(cron = "0 30 20 * * *")
    public void retryNotificationInEvening() {
        retryNotification();
    }

    public void retryNotification() {
        if (this.retrySet.isEmpty()) {
            return;
        }
        try {
            retrySet.forEach(failEvent -> {
                String email = failEvent.email();
                User retryUser = userRepository.findByEmail(email).orElseThrow();
                Page<Post> postPage = postRepository.findPostWithMinNotificationCount(retryUser,
                        PageRequest.of(0, 1));
                List<Post> posts = postPage.getContent();
                if (posts.isEmpty()) {
                    return;
                }
                Post minCountPost = posts.getFirst();
                minCountPost.increaseNotificationCount();
                sendEmail(retryUser, minCountPost);
                retrySet.remove(failEvent);
            });
        } catch (Exception ex) {
            log.warn(
                    String.format(
                            "cause={%s} msg={%s}",
                            ex.getCause(), ex.getMessage()
                    )
            );
        }

        StringBuilder sb = new StringBuilder();
        retrySet.forEach(i -> sb.append(i.email()).append("\n"));
        String failEmails = sb.toString();
        String reportContent = getEmailRetryNotificationResultReport(retrySet.size(),
                failEmails);
        retrySet.clear();

        discordUtils.sendDiscordWebhook(DiscordWebhookRequest.of(reportContent, discordServerUrl));
    }

    private void sendEmail(User user, Post post) {
        emailUtils.sendPostNotificationMessage(user.getEmail(), NotificationEmailDTO.from(post));
    }

    private String getEmailRetryNotificationResultReport(int failCount, String failEmails) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");

        return "[" + LocalDateTime.now().format(formatter) + "]" +
                " 이메일 알람 실패: " + failCount + " 실패 이메일 목록: " + failEmails;
    }
}
````

</div>
</details>

- 결과 보고서 디스코드 스크린샷
  <img width="882" alt="discord_webhook" src="https://github.com/soonhankwon/self-news-api/assets/113872320/bc7e1016-1ff7-4238-9ed9-abfd59d4dca1">