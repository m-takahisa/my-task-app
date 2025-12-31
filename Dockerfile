# --- ステージ1: ビルド環境 ---
FROM amazoncorretto:21-alpine AS build
WORKDIR /app

# Gradle の設定ファイルをコピー
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# 依存関係を先にダウンロード（キャッシュを利用するため）
RUN ./gradlew dependencies

# ソースコードをコピーしてビルド
COPY src src
RUN ./gradlew build -x test

# --- ステージ2: 実行環境 ---
FROM amazoncorretto:21-alpine
WORKDIR /app

# ステージ1からビルド済みの JAR ファイルだけをコピー
COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar

# 実行
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080