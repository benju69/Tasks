# 🗺️ Feature Plan & Suggestions

This document tracks planned improvements and new feature ideas for **Simple Tasks**.  
Items are grouped by priority and implementation complexity.

---

## ✅ Already Implemented

- Create, edit, delete tasks
- LOW / MEDIUM / HIGH priority levels
- Filter tasks by All / Active / Completed
- Toggle task completion
- Dark mode (persisted via DataStore)
- Clean Architecture (domain → data → feature)
- Unit tests for ViewModels, repositories, use-cases and mappers

---

## 🚀 Planned Features

### High Priority

- [ ] **Due dates & reminders**  
  Allow users to set an optional due date on each task and receive a local notification when the deadline approaches.

- [ ] **Task reordering**  
  Drag-and-drop reordering of tasks in the list so users can organise by their own preference, not just priority.

- [ ] **Undo delete**  
  Show a `Snackbar` with an "Undo" action after a task is deleted, preventing accidental data loss.

- [ ] **Search / quick filter**  
  A search bar at the top of the task list that live-filters tasks by title or description.

---

### Medium Priority

- [ ] **Categories / Tags**  
  Let users assign custom labels (e.g. Work, Personal, Shopping) to group tasks, with a filter chip per tag.

- [ ] **Sub-tasks (checklists)**  
  Support nested checklist items inside a task so complex tasks can be broken into smaller steps.

- [ ] **Task notes / rich description**  
  Expand the description field to support multiline text and basic Markdown rendering (bold, lists).

- [ ] **Archive completed tasks**  
  Instead of permanently deleting completed tasks, move them to an archive section that can be reviewed or bulk-deleted later.

- [ ] **Home-screen widget**  
  An Android App Widget showing today's pending tasks directly on the home screen.

- [ ] **Sorting options**  
  Allow tasks to be sorted by due date, priority, creation date, or alphabetically.

---

### Lower Priority / Nice-to-Have

- [ ] **Cloud sync / backup**  
  Optional sign-in (e.g. Google account) to back up and sync tasks across devices using Firebase Firestore or a REST API.

- [ ] **Recurring tasks**  
  Mark a task as repeating (daily, weekly, monthly) so it is automatically recreated after completion.

- [ ] **Statistics / productivity dashboard**  
  A summary screen showing tasks completed today, this week, completion rate, and a streak counter.

- [ ] **Export / import**  
  Export tasks as a JSON or CSV file, and import tasks from such a file, for backup and portability.

- [ ] **Collaboration / sharing**  
  Share a task list with another user, or send a task via a deep link / share intent.

- [ ] **Themes & colour accents**  
  In addition to dark/light mode, let users pick an accent colour or choose from a set of curated themes (Dynamic Color on Android 12+).

- [ ] **Accessibility improvements**  
  Ensure full TalkBack support, minimum 48 dp touch targets, and sufficient colour contrast across all components.

---

## 🛠️ Technical Improvements

- [ ] **UI / instrumented tests** — Add Compose UI tests using `ComposeTestRule` for the key screens.
- [ ] **CI pipeline** — GitHub Actions workflow to run `./gradlew test` and `./gradlew lint` on every pull request.
- [ ] **Lint baseline** — Configure `lint.xml` and fix or suppress all existing warnings.
- [ ] **ProGuard / R8 rules** — Add and verify minification rules for a release build.
- [ ] **Version catalog** — Migrate dependency declarations to a `libs.versions.toml` version catalog.
- [ ] **Kotlin Multiplatform (KMP)** — Extract the `domain` module as a pure KMP module so the business logic can be shared with a future iOS or web client.

---

## 💡 UX / Design Suggestions

- Show an **empty-state illustration** when there are no tasks, with a call-to-action to add the first one.
- Add a **floating action button (FAB) animation** — the FAB could transform into the editor screen (shared-element transition).
- Consider a **bottom sheet** for quick task creation (title + priority) so users don't have to leave the list screen.
- Improve **swipe gestures** — swipe right to complete, swipe left to delete (with undo).
- Add a **"Today" section** that surfaces tasks due today at the top of the list regardless of the active filter.
- Offer **haptic feedback** when a task is toggled complete.
- Display a **completion animation** (confetti or checkmark lottie) when all tasks in the current filter are done.

---

*Last updated: March 2026*
