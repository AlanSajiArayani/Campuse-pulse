# CampusPulse — Git Workflow Guide

This guide explains the exact steps each team member should follow to work on their
feature independently without breaking each other's code.

---

## The Golden Rules

1. **Never commit directly to `main`.** Always work on your own branch.
2. **Pull before you push.** Always get the latest changes before pushing yours.
3. **One feature = one branch.** Finish and merge one feature before starting the next.
4. **Coordinate before touching shared files.** Message the team if you need to change
   `User.java`, `AppConstants.java`, or another member's file.

---

## Step 1 · Clone the Repository (First Time Only)

```bash
git clone https://github.com/AlanSajiArayani/Campuse-pulse.git
cd Campuse-pulse
```

---

## Step 2 · Create Your Feature Branch

Always branch off of the latest `main`:

```bash
git checkout main
git pull origin main
git checkout -b your-branch-name
```

### Recommended Branch Names

| Member | Feature | Branch Name |
|---|---|---|
| Alan Saji | Authentication | `alan/authentication` |
| Alan Saji | Admin panel | `alan/admin-panel` |
| Alan Saji | Final integration | `alan/integration` |
| Ananya Raju | Model setup | `ananya/models` |
| Ananya Raju | Login screen UI | `ananya/login-ui` |
| Ananya Raju | Dashboard UI | `ananya/dashboard-ui` |
| Ardra K Gireesh | Resource catalog | `ardra/resource-catalog` |
| Ardra K Gireesh | Borrow workflow | `ardra/borrow-workflow` |
| Ashish K Biju | Volunteer management | `ashish/volunteer-management` |
| Ashish K Biju | Emergency requests | `ashish/emergency-requests` |

**Format:** `yourname/short-description` — lowercase, hyphens, no spaces.

---

## Step 3 · Make Changes and Commit

After editing your files:

```bash
# See what files you changed
git status

# Stage the files you want to commit
git add src/model/Resource.java
git add src/controller/MainController.java

# Or stage ALL changed files at once
git add .

# Commit with a clear message
git commit -m "ardra: implement addResource and searchResources"
```

### Good Commit Message Examples

```
alan: add login() stub in MainController
ananya: add showLoginScreen to MainView
ardra: implement searchResources with keyword filter
ashish: add createRequest with OPEN status
```

**Format:** `yourname: short description of what you did`

---

## Step 4 · Push Your Branch to GitHub

```bash
git push origin your-branch-name
```

Example:
```bash
git push origin ardra/resource-catalog
```

If this is the first time pushing this branch, git will print a message — that is normal.

---

## Step 5 · Open a Pull Request (PR)

1. Go to `https://github.com/AlanSajiArayani/Campuse-pulse`.
2. Click the **"Compare & pull request"** button that appears after your push.
3. Set **base branch** to `main` and **compare branch** to your feature branch.
4. Write a short title and description of what you did.
5. Click **"Create pull request"**.

### Who Should Review PRs?

| PR touches | Ask for review from |
|---|---|
| `User.java`, `AppConstants.java` | Ananya Raju (model coordinator) |
| `Resource.java`, Section 2 of controller | Ardra K Gireesh |
| `Volunteer.java`, `Request.java`, Section 3 | Ashish K Biju |
| `Admin.java`, Sections 1 & 4 of controller | Alan Saji |
| `MainView.java` | Ananya Raju |

---

## Step 6 · Keep Your Branch Up to Date

While you work on your branch, others will merge their changes into `main`. Update
your branch regularly so you do not fall behind:

```bash
git checkout main
git pull origin main
git checkout your-branch-name
git rebase main
```

If conflicts appear during rebase, see the section below.

---

## Handling Conflicts

A conflict happens when two people change the same lines in the same file.

### How to Resolve a Conflict

1. Git will tell you which file has a conflict. Open it in your editor.
2. You will see markers like this:

```
<<<<<<< HEAD
    // your version of the code
=======
    // the other person's version
>>>>>>> main
```

3. Decide which version to keep (or combine both), then delete the markers.
4. Stage the resolved file and continue:

```bash
git add src/model/User.java
git rebase --continue
```

### Preventing Conflicts on Shared Files

The most common source of conflicts is `MainController.java` because all four members
add methods to it. To avoid this:

- Each member only edits **their own numbered section** in the file.
- Do NOT reformat or reorganise sections you do not own.
- If you need a new method in someone else's section, ask them to add the stub.

---

## Non-Fast-Forward Push Error

If you see this when pushing:

```
! [rejected] main -> main (non-fast-forward)
```

It means the remote has new commits you do not have. Fix it with:

```bash
git pull --rebase origin main
git push origin main
```

This replays your commits on top of the remote's latest commits. It keeps the history
clean and linear.

---

## Quick Reference Card

```bash
# Start a new feature
git checkout main && git pull origin main
git checkout -b yourname/feature-name

# Save work in progress
git add .
git commit -m "yourname: describe your change"

# Share your branch with the team
git push origin yourname/feature-name

# Update your branch with latest main
git checkout main && git pull origin main
git checkout yourname/feature-name
git rebase main

# Fix a non-fast-forward push error
git pull --rebase origin main && git push origin main
```

