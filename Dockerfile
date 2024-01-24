# 이미지를 빌드하는 단계에서 사용할 베이스 이미지
FROM amazoncorretto:21 as builder

# Gradle을 사용하여 의존성 설치 및 빌드 진행
WORKDIR /app
COPY . /app
RUN ./gradlew build

# 최종 이미지 정의
FROM amazoncorretto:21

# Gradle 빌드에서 생성된 JAR 파일을 복사
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# Asciidoctor를 실행하기 위해 필요한 의존성 설치
RUN yum -y install ruby asciidoctor

# Asciidoctor 명령어 실행
WORKDIR /app
RUN asciidoctor -o build/generated-snippets/index.html src/main/asciidoc/index.adoc

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]