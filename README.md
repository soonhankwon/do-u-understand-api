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
