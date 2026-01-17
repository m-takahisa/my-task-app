# API設計書：高機能タスク管理システム

## 1. 共通仕様
- **ベースURL**: `/api/v1`
- **レスポンス形式**: JSON (`application/json`)
- **認証方式**: Bearer認証 (JWT) - `Authorization: Bearer <token>`
- **エラーレスポンス**:
    - `400 Bad Request`: バリデーションエラー
    - `401 Unauthorized`: 未認証・トークン無効
    - `403 Forbidden`: アクセス権限なし（他人のデータ操作等）
    - `404 Not Found`: リソースが存在しない

## 2. エンドポイント一覧

### 2.1 認証系 (Auth)
| メソッド | パス | 説明 | 認証 |
| :--- | :--- | :--- | :--- |
| POST | /auth/signup | 新規ユーザー登録 | 不要 |
| POST | /auth/login | ログイン（JWT発行） | 不要 |

### 2.2 タスク系 (Tasks)
| メソッド | パス | 説明 | 認証 |
| :--- | :--- | :--- | :--- |
| GET | /tasks | ログインユーザーのタスク一覧取得 | 必要 |
| POST | /tasks | 新規タスク作成 | 必要 |
| GET | /tasks/{id} | タスクの詳細取得 | 必要 |
| PUT | /tasks/{id} | タスクの更新 | 必要 |
| DELETE | /tasks/{id} | タスクの削除 | 必要 |

## 3. リクエスト・レスポンス詳細

### 3.1 タスク一覧取得 (GET /tasks)
**クエリパラメータ:**
- `status`: 絞り込み (`TODO`, `DOING`, `DONE`) ※任意
- `search`: タイトル/内容のキーワード検索 ※任意

**レスポンス例 (200 OK):**
```json
[
  {
    "id": 1,
    "title": "設計書を書く",
    "status": "DOING",
    "due_date": "2024-05-30",
    "created_at": "2024-05-25T10:00:00Z"
  }
]