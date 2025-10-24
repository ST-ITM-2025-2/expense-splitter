# Contributing

Thanks for contributing to the OSS Expense Splitter!

This project is divided into **three phases**.



## Roster Setup (one-time) (Oct 17)
- One PR per group, opened by the **Team Captain**.
- **Group members must be contiguous rows** in the table.
- Use label **`roster`** on this PR so CI does not require tests.
- Do **not** change any Java files in the roster PR.

**Steps (suggested):**
1. Captain forks the repo and creates a branch: `git checkout -b roster/group-<n>`  
2. Captain edits the Contributors table to add **three contiguous rows** for the group.  
3. Captain pushes and opens a PR labeled **`roster`**.  
5. TA/Prof. verifies rows are contiguous and merges the PR.


## Phase 2 (Implementation) (Oct 24 midnight)
- Students are assigned into groups of 3 (Groups 1–9).
- Each group selects one Issue from the GitHub Issues tab.
- The instructor/TA labels the issue with:
  - `groupX-implementing` (e.g., `group1-implementing`)
  - `in progress`
- When implementation is finished, group updates PR label to:
  - `implementation finished`
- Deliverables:
  - Implementation code for the issue
  - Unit tests (at least 2 happy paths + 2 edge cases)
    - Happy path test is a well-defined test case using known input, which executes without exception and produces an expected output.
  - PR opened against `main`



## Phase 3 (Cross-Review & Refactor) (Nov 05 midnight)
- Review assignments are round-robin:
  - Group 1 reviews Group 2
  - Group 2 reviews Group 3
  - …
  - Group 9 reviews Group 1
- When reviewing, label the PR with:
  - `groupX-reviewing` (e.g., `group1-reviewing`)
  - `in progress`
- After review, change to:
  - `review finished`
- Each member of the reviewing group must **approve** the PR.
- Only after 3 approvals + CI green, the PR is merged (Squash & merge).

---

## Labels used
- `phase1`, `phase2`, `phase3`
- `groupX-implementing` (X=1…9)
- `groupX-reviewing` (X=1…9)
- `in progress`
- `implementation finished`
- `review finished`
- `needs tests`
- `changes requested`
- `ready to merge`

---

## General rules
- Branches: `feat/<issue-number>-short-title`
- Commits: short imperative, e.g., `Add CSV loader`
- No console I/O in unit tests
- Clear test names and assertions

