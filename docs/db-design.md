# DB設計書：高機能タスク管理システム

## 1. ER図 (Entity Relationship Diagram)

```mermaid
erDiagram
    users ||--o{ tasks : "1対多 (1人のユーザーは複数のタスクを持つ)"
    
    users {
        bigint id PK "サロゲートキー / 自動採番"
        string email UK "ログインID / ユニーク制約"
        string password "BCryptハッシュ化"
        string name "ユーザー名"
        datetime created_at
        datetime updated_at
    }

    tasks {
        bigint id PK "自動採番"
        bigint user_id FK "users.idへの参照"
        string title "タスク名 / 必須"
        text description "詳細説明"
        string status "TODO, DOING, DONE"
        date due_date "期限日"
        datetime created_at
        datetime updated_at
    }