# Issues to create on GitHub

Copy each title + body into **Issues → New issue**. Add the labels shown, assign yourself, add to the project.

---

## Sprint 1

### 1. As a student, I want to register and log in with email and password
Labels: `user-story`, `android`, `firebase`

**User story**

As a student, I want to register and log in with my email and password,
so that I can access my own reservations.

**Acceptance criteria**
- [ ] A new account can be created and the user lands on Home
- [ ] Invalid email, short password (< 6) and mismatched passwords show field errors without calling Firebase
- [ ] Registering an existing email shows a readable message
- [ ] Wrong credentials show a readable error message
- [ ] A logged-in user skips the login screen after restarting the app
- [ ] Logout returns to Login; Back does not return to Home

---

### 2. As a student, I want to see a list of study rooms
Labels: `user-story`, `android`, `firebase`, `ui`

**User story**

As a student, I want to see a list of study rooms with their building and capacity,
so that I can pick a room that suits my group.

**Acceptance criteria**
- [ ] The Home screen shows rooms loaded from the `rooms` collection in Firestore
- [ ] Each item shows name, building, capacity and a projector icon if available
- [ ] An empty list shows "No rooms yet" instead of a blank screen
- [ ] A loading indicator is shown while data loads

---

### 3. As a student, I want to see room details and today's bookings
Labels: `user-story`, `android`, `ui`

**User story**

As a student, I want to open a room and see when it is already booked today,
so that I can choose a free time slot.

**Acceptance criteria**
- [ ] Tapping a room opens its detail screen
- [ ] The screen lists today's active reservations (start–end)
- [ ] Back returns to the room list

---

### 4. As a student, I want to book a room and cancel my booking
Labels: `user-story`, `android`, `firebase`

**User story**

As a student, I want to book a room for a time slot and cancel it if my plans change,
so that the room is reserved for me and freed for others when I don't need it.

**Acceptance criteria**
- [ ] I can pick start and end time and save a reservation (`ownerId` = my uid)
- [ ] End before start shows an error and nothing is saved
- [ ] I can cancel only my own reservation (status → `cancelled`)
- [ ] The new booking appears in the room's list immediately

---

### 5. Set up repository: README, PR template, branch ruleset
Labels: `docs`

**Acceptance criteria**
- [ ] README has problem statement, team & roles, MVP, data model, Mermaid diagram, How to run
- [ ] `.github/pull_request_template.md` exists
- [ ] Ruleset `protect-main` is active (PR required, no force push, no deletion)

---

## Backlog

### 6. As a student, I want to see all my reservations
Labels: `user-story`, `android`, `ui`

**User story**

As a student, I want to see a list of all my upcoming reservations,
so that I don't forget where and when I booked a room.

**Acceptance criteria**
- [ ] The screen shows only reservations where `ownerId` = my uid
- [ ] Sorted by start time, cancelled ones hidden
- [ ] Tapping one opens the room detail

---

### 7. As a student, I want overlapping bookings to be rejected on the server
Labels: `user-story`, `firebase`

**User story**

As a student, I want the system to reject a booking that overlaps an existing one,
so that two groups never arrive at the same room at the same time.

**Acceptance criteria**
- [ ] A Cloud Function checks for overlap before a reservation becomes `active`
- [ ] An overlapping booking is rejected with a readable message in the app
- [ ] Non-overlapping bookings still succeed

---

## After the demo

### Retro – Lab 1
Labels: `docs`

Add one comment with: **Went well** / **Did not go well** / **We will change**.
Write 1–2 action items in this issue's description.
