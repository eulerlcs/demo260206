# Demo プロジェクト

このプロジェクトは Spring Boot を使った簡易デモです。
主に以下を実装しています。

- GET /add API: 2 つの整数パラメータ `a` と `b` を受け取り、その和を返す（バリデーションあり）
- Thymeleaf ベースの UI (`/add-ui`): ブラウザ上で a と b を入力して計算結果を表示
- OpenAPI/Swagger ドキュメント（springdoc を使用）

---

## 主要機能

- `HelloController` に `/add` エンドポイントを実装
  - クエリパラメータ `a` と `b` を受け取り、合計を返す
  - バリデーション: `a`, `b` は `null` でないこと、`0 <= value <= 1000`
  - OpenAPI アノテーションで API ドキュメントを記述
- `AddService` に計算ロジックを実装（UI と API 両方で利用）
- `AddUiController` と `src/main/resources/templates/add.html` により簡易 UI を提供
- 例外ハンドリング: バリデーションや必須パラメータ欠如は 400 エラーとして JSON で返す（`GlobalExceptionHandler`）

---

## 必要な環境

- JDK 21
- Maven（ラッパー `mvnw.cmd` を同梱しているので必須ではありません）

---

## ビルドとテスト

プロジェクトルート（この README と同じディレクトリ）で以下を実行します。

Windows (cmd.exe):

```cmd
mvnw.cmd -DskipTests=true compile
mvnw.cmd test
```

または Maven がインストールされている場合:

```bash
mvn -DskipTests=true compile
mvn test
```

---

## アプリの起動

開発時は以下で起動できます。

```cmd
mvnw.cmd spring-boot:run
```

起動後、デフォルトは `http://localhost:8080` です。

---

## 利用可能なエンドポイント

- GET /add
  - 説明: 2 つの整数の和を返す
  - クエリパラメータ:
    - `a` (int, 必須) — 0 以上 1000 以下
    - `b` (int, 必須) — 0 以上 1000 以下
  - 例:

```cmd
curl "http://localhost:8080/add?a=2&b=3"
# => 5
```

- UI: `/add-ui`
  - ブラウザでアクセスして、フォームに `a` と `b` を入力して計算できます。
  - 例: `http://localhost:8080/add-ui`

- OpenAPI / Swagger
  - JSON: `http://localhost:8080/v3/api-docs`
  - UI: `http://localhost:8080/swagger-ui/index.html` （または `/swagger-ui.html`）

---

## エラーハンドリング

- パラメータが欠如している場合: 400 Bad Request（例: `Required request parameter 'a'`）
- バリデーション違反 (範囲外など): 400 Bad Request、レスポンス JSON に `error` フィールドを含む

---

## 主要ファイルの場所

- コントローラ: `src/main/java/com/example/demo/controller/HelloController.java`
- UI コントローラ: `src/main/java/com/example/demo/controller/AddUiController.java`
- サービス: `src/main/java/com/example/demo/service/AddService.java`
- 例外ハンドラ: `src/main/java/com/example/demo/exception/GlobalExceptionHandler.java`
- テンプレート: `src/main/resources/templates/add.html`
- テスト: `src/test/java/com/example/demo/controller/HelloControllerTest.java`

---

## 開発メモ / 改善案

- 現状、UI はサーバ側で `AddService` を直接呼ぶ実装です。別アプリから利用する場合は API をそのまま呼べます。
- エラーレスポンスのスキーマをより厳密に管理したい場合は `ErrorResponse` の DTO を作成して OpenAPI に反映するとよいです。
- CSRF 対策やセキュリティ設定（Spring Security）を導入する場合はテンプレートのフォームに CSRF トークンを追加してください。

---

## Docker

ローカルでイメージを作成して実行する手順、GHCR へプッシュする例、およびクリーンアップ方法を示します。

### 前提

- Docker がインストールされ、動作していること
- プロジェクトルートに `Dockerfile` が存在すること（このリポジトリでは multi-stage の `Dockerfile` を用意しています）

### ローカルでビルド

Windows (cmd.exe):

```cmd
cd /d %CD%
:: プロジェクトルートで実行
docker build -t demo-app:latest .
```

macOS / Linux (bash):

```bash
docker build -t demo-app:latest .
```

ビルド後、`docker images` で `demo-app:latest` が作成されていることを確認してください。

### コンテナを起動

Windows (cmd.exe):

```cmd
docker run -d -p 8080:8080 --name demo-app demo-app:latest
```

ログ確認:

```cmd
docker logs -f demo-app
```

ホストからの HTTP 確認（curl がある場合）:

```cmd
curl http://localhost:8080/
```

PowerShell の場合:

```powershell
Invoke-WebRequest -Uri http://localhost:8080/ -UseBasicParsing
```

### GitHub Container Registry (GHCR) に Push する（例）

GitHub Actions を使う場合は、リポジトリに `GITHUB_TOKEN` があるため追加の設定なく Actions でプッシュできます（このリポジトリには `.github/workflows/ci.yml` を用意済み）。

ローカルから手動で push する場合の手順（例）:

1. Personal Access Token (PAT) を作成し、`write:packages` スコープを付与
2. ログインしてタグ付け、プッシュ

Windows (cmd.exe):

```cmd
set CR_PAT=ghp_xxx...   :: ご自身の PAT を環境変数にセット
echo %CR_PAT% | docker login ghcr.io -u <USERNAME> --password-stdin
docker tag demo-app:latest ghcr.io/<OWNER>/<REPO>:latest
docker push ghcr.io/<OWNER>/<REPO>:latest
```

bash:

```bash
export CR_PAT=ghp_xxx...
echo "$CR_PAT" | docker login ghcr.io -u <USERNAME> --password-stdin
docker tag demo-app:latest ghcr.io/<OWNER>/<REPO>:latest
docker push ghcr.io/<OWNER>/<REPO>:latest
```

注意: GitHub Actions 内では `GITHUB_TOKEN` を使うため PAT は不要。リポジトリ/組織の設定により追加の権限が必要な場合があります。

### クリーンアップ

```cmd
docker stop demo-app || echo not-running
docker rm demo-app || echo not-found
docker rmi demo-app:latest || echo not-found
```

---

## ライセンス

このリポジトリにライセンスファイルがない場合は利用制限に注意してください。

---

必要であれば README に追加で記載したい内容（スクリーンショット、CI 設定、Dockerfile など）を教えてください。