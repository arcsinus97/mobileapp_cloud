# Lab 1 – Step-by-step (solo, StudyRoom)

Package used in the code: **`com.teamsolo.studyroom`**. If you choose another package, use it everywhere below and change the `package`/`import com.teamsolo...` lines in the `.kt` files (Android Studio: right-click the package → Refactor → Rename).

Tick the boxes as you go. Times are from the lab plan.

---

## Phase 1 – Roles (5 min)
- [ ] Team name: e.g. "Team Solo". You hold all four roles (PO, Tech Lead, Developer, QA/DevOps).
- [ ] Put your name + GitHub handle into the Team table in `README.md`.

## Phase 2 – Product (done for you)
- [ ] App: **StudyRoom**. Problem statement, MVP (Sprint 1 marked) and data model are in `README.md`. Read them so you can present them.

## Phase 3 – GitHub repo + board (20 min)

**Repository**
1. [ ] github.com → **New repository** → name `studyroom-app`, description = the problem statement.
2. [ ] **Public** (or Private only if you have GitHub Pro / Student Pack – needed for branch rules).
3. [ ] Do **not** add README, .gitignore or licence – leave it empty.
4. [ ] Settings → Collaborators → **Add people** → invite the instructor.

**Labels** (Issues → Labels → New label): `user-story`, `android`, `firebase`, `ui`, `bug`, `docs`. (`docs` may already exist as `documentation` – create `docs` anyway.)

**Board**
1. [ ] Repo → **Projects** tab → **New project** → template **Board** → name `StudyRoom – Sprint board`.
2. [ ] Click the column header ⋯ / Status field settings → set options: **Backlog, Sprint 1, In progress, In review, Done** (rename "Todo" → Backlog, add Sprint 1 and In review).
3. [ ] Project ⋯ → **Workflows** → check "Item closed" and "Pull request merged" are enabled and set Status = **Done**.

**Issues** – open `docs/sprint1-issues.md` and create issues 1–5 (Sprint 1) and 6–7 (Backlog). For each: labels, **Assignee = you**, **Projects = StudyRoom – Sprint board**, then set Status on the board. Note the number of the auth issue (probably **#1**).

## Phase 4 – Android + Firebase (30 min)

**Android project**
1. [ ] Android Studio → New Project → **Empty Activity**.
2. [ ] Name `StudyRoom`, package `com.teamsolo.studyroom`, Min SDK **API 26**, build language **Kotlin DSL**.
3. [ ] Wait for sync, run once – "Hello Android!" appears.

**First push** (Android Studio → Terminal, project root):
```bash
git init -b main
git add .
git commit -m "chore: initial Android project (Compose)"
git remote add origin https://github.com/<you>/studyroom-app.git
git push -u origin main
```
If asked for a password: sign in via Settings → Version Control → GitHub, or run `gh auth login`.

**Protect main** – repo Settings → Rules → Rulesets → **New branch ruleset**:
- [ ] Name `protect-main`, Enforcement **Active**, Target → Add target → **Include default branch**
- [ ] ✔ Restrict deletions ✔ Block force pushes ✔ Require a pull request before merging → **Required approvals: 0** (solo – you can't approve your own PR)
- [ ] Save.

**Firebase**
1. [ ] console.firebase.google.com → **Create a project** → name `StudyRoom` → Google Analytics **off**. Stay on **Spark** (never upgrade to Blaze).
2. [ ] Project settings (⚙) → **Users and permissions** → add the instructor (Editor).
3. [ ] Project overview → **Android icon** → package name = `applicationId` from `app/build.gradle.kts` (exactly `com.teamsolo.studyroom`), nickname StudyRoom, skip SHA-1.
4. [ ] Download **google-services.json** → put it in the **`app/`** folder (Project view, not Android view). Skip the console's Gradle instructions – you do that in Phase 5.
5. [ ] **Authentication → Get started → Sign-in method → Email/Password → Enable → Save.**

## PR #1 – docs (10 min, gives you the first of two merged PRs)
```bash
git checkout -b docs/readme-templates
```
- [ ] Copy `README.md` and the whole `.github/` folder from this bundle into the project root (replace the README if one exists). Fill in your name.
- [ ] `app/google-services.json` (from Firebase) goes into this commit too – it's not a secret.
```bash
git add README.md .github app/google-services.json
git commit -m "docs: add README, PR and issue templates, Firebase config"
git push -u origin docs/readme-templates
```
- [ ] On GitHub: **Compare & pull request** → fill the template, write `Closes #5` → Create → **Squash and merge**.
```bash
git checkout main && git pull
```

## Phase 5 – Auth feature (55 min)
```bash
git checkout -b feature/auth
```

**5.2 Gradle**
1. [ ] Merge `gradle-snippets/libs.versions.toml.additions` into `gradle/libs.versions.toml` (add lines into the existing `[versions]`, `[libraries]`, `[plugins]` sections – don't duplicate the headers).
2. [ ] Apply `gradle-snippets/build.gradle.kts.additions` to the **root** `build.gradle.kts` and `app/build.gradle.kts`.
3. [ ] **Sync Now**, then Build → Make Project. Fix nothing else yet.
```bash
git add . && git commit -m "build: add Firebase Auth and Navigation dependencies"
```

**5.3–5.6 Code** – copy everything from `app/src/main/java/com/teamsolo/studyroom/` into the same path in your project (replace `MainActivity.kt`). Commit in small steps:
```bash
git add app/src/main/java/com/teamsolo/studyroom/data
git commit -m "feat(auth): add AuthRepository with Firebase implementation"
git add app/src/main/java/com/teamsolo/studyroom/domain
git commit -m "feat(auth): add AuthValidator"
git add app/src/main/java/com/teamsolo/studyroom/ui/auth/AuthViewModel.kt
git commit -m "feat(auth): add AuthViewModel and map Firebase errors"
git add app/src/main/java/com/teamsolo/studyroom/ui
git commit -m "feat(auth): add login, register and home screens"
git add .
git commit -m "feat(auth): add navigation with auto-login and back-stack clearing"
```

**Test it (deliverable list)** – run on the emulator:
- [ ] Register `test1@student.polsl.pl` / `secret123` → lands on Home showing the email
- [ ] Register with `abc` email, `123` password, mismatched confirm → field errors, no network call
- [ ] Register the same email again → "An account with this email already exists"
- [ ] Log in with wrong password → "Incorrect email or password"
- [ ] Log out → Login screen; press Back → app closes (does not return to Home)
- [ ] Log in, close the app (swipe from recents), reopen → straight to Home
- [ ] Firebase Console → Authentication → Users shows the new user

**5.7 PR #2**
```bash
git push -u origin feature/auth
```
- [ ] Open PR into `main`, fill the template, write **`Closes #1`** (your auth issue number).
- [ ] Solo "review": open **Files changed**, leave at least one comment on a line (e.g. a note on error mapping) so the review step is visible.
- [ ] **Squash and merge** → issue #1 closes and moves to **Done** automatically.
```bash
git checkout main && git pull
```

## Phase 6 – Demo + retro (30 min)
Demo (3 min), in this order:
1. Problem statement + MVP (README)
2. Data model + Mermaid diagram (README renders it on GitHub)
3. Live: register → logout → login → kill app → reopen (auto-login)
4. Board, 2 merged PRs, the `protect-main` ruleset

Likely question: *"Where will you check that a user can only edit their own data?"* → **In Firebase Security Rules** (server side, Lab 2), e.g. `request.auth.uid == resource.data.ownerId`. Checks in the app alone can be bypassed.

Retro:
- [ ] New issue **Retro – Lab 1**, label `docs`, comment: went well / didn't go well / will change.
- [ ] Put 1–2 action items in the description (e.g. "Write issues before coding", "Commit smaller").

---

## End-of-lab checklist
- [ ] Roles, problem, MVP, data model, Mermaid diagram in README
- [ ] Board with 5 columns; Sprint 1 issues with criteria and assignee
- [ ] Ruleset `protect-main` active
- [ ] PR template in `.github/`
- [ ] 2 merged PRs (docs + auth)
- [ ] Firebase project on Spark, instructor added, `google-services.json` in `app/`, Email/Password enabled
- [ ] Register / login / logout / auto-login work, readable errors
- [ ] Retro – Lab 1 issue with action items

## If something breaks
| Problem | Fix |
|---|---|
| `google-services.json is missing` | File must be in `app/`, and its `package_name` must equal `applicationId` |
| `Unresolved reference: collectAsStateWithLifecycle` | You missed `lifecycle-runtime-compose` in the Gradle snippets |
| `Unresolved reference: await` | Add `kotlinx-coroutines-play-services` |
| `Unresolved reference: StudyRoomTheme` | Your theme is named after your project – use the name in `ui/theme/Theme.kt` |
| Login always fails | Email/Password not enabled in console (also: Firebase intentionally doesn't say whether email or password was wrong) |
| Push rejected: protected branch | Correct behaviour – push a branch and open a PR |
