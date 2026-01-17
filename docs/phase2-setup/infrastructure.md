# インフラ構成設計書：高機能タスク管理システム

## 1. 構成概要
本システムは、開発効率と本番環境の信頼性を両立させるため、Dockerによるコンテナ技術を基盤とする。開発環境はローカルPC上のDocker Compose、本番環境はAWSのマネージドサービスを利用したサーバーレス構成を採用する。

## 2. 開発環境構成（Local Docker）
開発者のローカルPC上で、アプリケーションとデータベースを迅速に立ち上げるための構成。

| サービス | コンポーネント | 役割 | 接続情報 |
| :--- | :--- | :--- | :--- |
| **App** | Spring Boot 3.x | APIサーバー (Port: 8085) | localhost:8085 |
| **DB** | PostgreSQL 16 | 永続化データ管理 | localhost:5432 |
| **Admin** | pgAdmin 4 | GUIによるDB操作 | localhost:8080 |

### ネットワーク図（論理）
- `App` -> `DB` (コンテナ間通信)
- `Admin` -> `DB` (host.docker.internal経由)

## 3. 本番環境構成案（AWS Cloud）
実務レベルの運用を想定し、運用負荷の低いサーバーレス・マネージドサービスを中心とした構成。

### 構成要素
1. **Compute**: **AWS App Runner**
    - 理由: Dockerイメージをプッシュするだけで、オートスケーリングやSSL証明書管理を自動化できるため。
2. **Database**: **Amazon RDS for PostgreSQL**
    - 理由: バックアップ、パッチ適用、マルチAZによる高可用性をマネージドで実現するため。
3. **Network**: **VPC / Private Subnet**
    - 理由: データベースをパブリックインターネットから隔離し、App Runnerからのみ接続可能にすることでセキュリティを担保する。
4. **CI/CD**: **GitHub Actions**
    - 理由: コードプッシュをトリガーに、ビルド・テスト・App Runnerへのデプロイを自動化するため。

## 4. セキュリティ設計
* **通信の暗号化**: 本番環境では全て HTTPS (TLS) 通信を強制する。
* **認証**: APIへのアクセスは JWT (JSON Web Token) を必須とし、Statelessな認証を実現する。
* **DB保護**: データベースはプライベートサブネットに配置し、踏み台サーバーまたは特定のアプリケーション層からの接続のみを許可する。

## 5. インフラ構成図 (将来像)
```mermaid
graph TD
    User((ユーザー)) -->|HTTPS| AppRunner[AWS App Runner]
    subgraph VPC
        AppRunner -->|VPC Connector| RDS[(Amazon RDS)]
    end
    GitHub[GitHub Repo] -->|Push| GHA[GitHub Actions]
    GHA -->|Deploy| AppRunner