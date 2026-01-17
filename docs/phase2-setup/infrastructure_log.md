# 第2フェーズ：詳細設計・環境構築ログ

## 1. 概要
Dockerを用いたデータベース実行環境の構築、およびSpring Bootアプリの初期設定を完了。

## 2. 関連ファイル
- `docker-compose.yml`: PostgreSQL 16 および pgAdmin 4 のコンテナ定義
- `src/main/resources/application.yml`: アプリケーションの動作設定、DB接続プロパティ

## 3. インフラ構成 (Docker)
* **データベース**: `task-app-db` (PostgreSQL 16)
    * ホスト接続ポート: `5432`
* **管理ツール**: `task-app-pgadmin` (pgAdmin 4)
    * ホスト接続ポート: `8080`
    * 接続設定: ホスト名に `host.docker.internal` を使用

## 4. アプリケーション設定
* **ポート番号**: `8085`
    * pgAdmin(8080)との衝突を避けるため、デフォルトから変更。
* **JPA/Hibernate**: `ddl-auto: update`
    * エンティティの変更を自動でDBスキーマに反映する設定を有効化。

## 5. 実装のポイントとトラブルシューティング
* **Dockerネットワークの理解**:
    - pgAdminコンテナからホスト側のDBに接続する際、`localhost` ではなく `host.docker.internal` を使う必要があることを学び、接続を確立した。