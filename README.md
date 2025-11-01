# OSS Expense Splitter (CLI Project)

This repository is used for the Open Source Software class assignment. Students practice Git, GitHub Issues & Pull Requests, code review, Maven, and writing unit tests.

---

## Phases

### Phase 1 (Group Formation)
- Form groups of 3 students, no more no less. There should be 9 groups of 3 students each.
- Choose a group name and a group captain.
- Team captain Forks and clones the repo
- Team captain add the information of the grupo memebers to the  Contributors table via PR.
- Open a PR with label `roster`

### Phase 2 (Code Review 1)
- Groups are formed (Groups 1–9)
- Each group is assigned one Issue (features of the Expense Splitter)
- Issue is labeled `groupX-implementing` and `in progress`
- Deliverables: implementation + unit tests
- When finished, label PR `implementation finished`

### Phase 3 (Cross-Review)
- Review is round-robin: Group 1 → Group 2 → … → Group 9 → Group 1
- Reviewing group labels PR `groupX-reviewing` and `in progress`
- After review, change to `review finished`
- All 3 reviewers must approve before PR can be merged
- PRs are merged with **Squash & merge**

---


## Contributors (Roster Setup)

See the [Contributing Guidelines](/CONTRIBUTING.md).

**Rules for adding your group:**
1) Exactly **one PR per group** (opened by the **Team Captain**).  
2) Columns are: **Name**, **GitHub ID**, **Group Name**.  
3) Label the PR **`roster`** so CI does not require tests for this PR.

### Table
Name | GitHub ID | Group Name | Assigned Group Number |
------|-----------|------------|------------|
 Lim Donghyun    | @ehdgus4173 | Kim Byungchan  | 1|
 Kim Junseop     | @Junseop1228 | Kim Byungchan | 1|
 Byungchan Kim   | @byungchan3077 | Kim Byungchan | 1|
 Choi Ju Young | @ichbinju0 | Kka-Mong-Ju | 2|
 Park So Yeon  | @so-yeon-333 | Kka-Mong-Ju | 2|
 Kim Gyeong Yoon | @Gyeongyoon | Kka-Mong-Ju | 2|
 Kim Geon     | @GeonKim0422 | Team F1 | 3|
 Kim Sinwoo     | @bigmansinwoo-coder | Team F1 | 3|
 Kim Junu     | @MelonChicken | Team F1 | 3|
  ParkMinjoon     | @ERE252 | OpenMinded | 4|
 HongJunsoo     | @Jeffrey-Hong1005 |  OpenMinded | 4|
 LeeSeongbin     | @yeonlimee2 |  OpenMinded | 4|
  Kim Hyewon     | @Kimhyewon0621 | githero | 5|
 Kwon Ayeong     | @AyeongKwon | githero | 5|
 Kim Mingyeong     | @mingyeonggg | githero | 5|
 Oh Kyung Hun  | @ohkyounghun  | Doosan Bears | 6|
 Kim Gun Woo   | @gunuzello    | Doosan Bears | 6|
 Kwon Do Hun   | @kwon-dohun   | Doosan Bears | 6|
 Kwon Sihyun | @bbirribbarribbo | P.P.G | 7|
 Lee Jiseop  | @ljseop1030 | P.P.G | 7|
 Na Hyanghee | @ihh25 | P.P.G | 7|
 Minhee     | @MHJeong730 | BranchOps | 8|
 Agnes      | @sengA7 | BranchOps | 8|
 Sascha Huber     | @saschahuberzh | BranchOps | 8|


---

## Labels
- `roster` (for the group PR that only edits the Contributors table)
- `phase2`, `phase3`
- `groupX-implementing`, `groupX-reviewing` (X=1…9)
- `in progress`, `implementation finished`, `review finished`
- `needs tests`, `changes requested`, `ready to merge`

---

## License
MIT — see `LICENSE`.
