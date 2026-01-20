# 学習ログ Vol.2：タスクCRUD機能の実装

## 1. 概要
タスクの「作成・読み取り・更新・削除（CRUD）」および、それらに付随するバリデーションと画面表示（Thymeleaf）の実装を完了。

## 2. 実装のポイントと学び
* **DDL自動更新**: `application.yml` の `ddl-auto: update` 設定により、Entityの変更をDBへ自動反映。
* **View (Thymeleaf) の導入**
    - `@Controller` と `Model` を使い、サーバーサイドからHTMLへデータを渡す仕組みを構築。
* **Optional による安全なユーザー取得**
    - `Optional<User>` を採用し、`orElseThrow` でユーザー不在時の例外処理を簡潔に記述。
* **Bean Validation による入力チェック**
    - `@NotBlank` や `@Size` を `Task` エンティティに付与し、`BindingResult` でエラーを判定。
    - HTML側では `th:errors` と `is-invalid` クラスを用いて、ユーザーにエラー箇所を明示。
* **CRUD操作の実装**
    - **Create**: 新規登録画面のフォームからタスクを登録し、一覧へリダイレクトする機能を実装。
    - **Update**: 既存データを一度取得してからフォーム値で上書きする安全な更新フローを採用。
    - **Delete**: フォーム送信とJavaScriptの `confirm` による削除確認を実装。

## 3. トラブルシューティング
* **カラム順序の不一致**: Hibernateの仕様（リフレクションによる読み込み）により、Javaの定義順がDBに反映されないことを確認。開発中は許容し、将来的に `schema.sql` やマイグレーションツールでの解決を検討する。
* **HTMLのid参照エラー**: `th:field` による自動生成idがIDEで認識されない問題。`input` タグに明示的に `id` を記述することで解決。