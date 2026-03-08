# --- ステージ1: ビルド環境 ---
FROM amazoncorretto:21-alpine AS build
WORKDIR /app

# Gradle の設定ファイルをコピー
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# 依存関係を先にダウンロード（キャッシュを利用するため）
# srcを変えてもここは再実行されない（ライブラリのダウンロードをスキップ）
RUN ./gradlew dependencies --no-daemon

# ソースコードをコピーしてビルド
COPY src src
RUN ./gradlew build -x test --no-daemon

# --- ステージ2: 実行環境 ---
FROM amazoncorretto:21-alpine
WORKDIR /app

# ステージ1からビルド済みの JAR ファイルだけをコピー
# Gradleでビルドするとプログラム（JARファイル）はbuild/libs/に出力される
COPY --from=build /app/build/libs/*.jar app.jar

# 実行
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8085