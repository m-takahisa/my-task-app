# 第3フェーズ：実装ログ (Vol.2 タスクCRUD基礎)

## 1. 概要
ユーザーおよびタスクの基本エンティティの実装と、DB連携（保存・取得）の疎通確認を完了。

## 2. プロジェクト構造と役割 (com.m_takahisa.taskapp)
実装した主要ファイルと役割を整理。

| レイヤー | ファイル名 | 主要な役割・ポイント |
| :--- | :--- | :--- |
| **Entity** | `User.java`, `Task.java` | DBテーブル定義。`@ManyToOne` による1対多のリレーションを構築。 |
| **Repository** | `UserRepository.java`, `TaskRepository.java` | `JpaRepository` 継承によるDB操作。`findByEmail` 等のカスタムメソッドを定義。 |
| **Service** | `UserService.java`, `TaskService.java` | ビジネスロジック。`@Transactional` による整合性確保。 |
| **Controller** | `UserController.java`, `TaskController.java` | REST API窓口。JSON形式でのレスポンス返却。 |

## 3. 実装のポイント
* **DDL自動更新**: `application.yml` の `ddl-auto: update` 設定により、Entityの変更をDBへ自動反映。
* **ポート設定**: `server.port: 8085` にて、管理ツールとの競合を回避。
* **DBカラムへの論理名（コメント）付与**:
  - JPAの `@Column(columnDefinition = "...")` を活用し、DBツール上でカラムの役割（論理名）を確認できるように改善。
  - A5M2やpgAdminでのメンテナンス性を向上させ、設計書との不一致を防止。

## 4. 疎通確認の結果
ブラウザからのリクエストにより、正常にDBへデータが永続化されることを確認。
- `/users/test`: `m_takahisa` ユーザーの作成。
- `/tasks/test`: 上記ユーザーへのタスク紐付け保存。

## 5. トラブルシューティングの記録
* **DataIntegrityViolationException**:
    - 原因: `User` 作成時に `password` (nullable=false) をセットし忘れた。
    - 学び: Entityの制約がDB保存時に厳密にチェックされることを確認。
* **シンボルを見つけられません (コンパイルエラー)**:
    - 原因: `Task` に `completed` フィールドが不足。
    - 解決: フィールド追加とLombokの `@Data` によりGetter/Setterを有効化。
* **PostgreSQLでのCOMMENT構文エラー**:
    - 現象: `@Column(columnDefinition = "... COMMENT '...'")` でエラー発生。
    - 原因: PostgreSQLは`CREATE TABLE` 内での `COMMENT` 記述をサポートしていないため。
    - 解決: `columnDefinition` から `COMMENT` を削除。論理名は設計書（`db-design.md`）で管理することとした。
* **想定したカラム順序と異なる**:
  - 現象: `User.java` や `Task.java` のフィールドを理想の順に並べ替えて、アプリを再起動したが、その順序でテーブルが作成されない。
  - 原因: Hibernateは内部的にJavaクラスをリフレクションという機能で読み取るが、フィールドが定義された順番（上から順）ではなく、
  **アルファベット順** や **内部的に保持された順序** で `CREATE TABLE` の SQL を組み立ててしまうから。
  - 解決①: **A5M2などのツール側で見る順番を変える**<br>
    物理的な順序（ストレージ上の順序）は気にせず、SQLを発行する際やツール上の表示設定で並び替える。
  - 解決②: **初期化SQL（schema.sql）を用意する**<br>
    `ddl-auto: update` に頼らず、自分で `CREATE TABLE` 文を記述した `src/main/resources/schema.sql` を作成する。
  - 解決③: **FlywayやLiquibaseなどのマイグレーションツールを使う**<br>
    SQLファイルをバージョン管理し、定義した順番通りにテーブルを作成・変更する。
  <br>→ 開発スピードを優先し、DB上の物理順序はとりあえずは許容する。