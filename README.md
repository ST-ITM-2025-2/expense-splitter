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
Name | GitHub ID | Group Name |
------|-----------|------------|
  Kim Hyewon     | @Kimhyewon0621 | githero |
 Kwon Ayeong     | @AyeongKwon | githero |
 Kim Mingyeong     | @mingyeonggg | githero |
 Oh Kyung Hun  | @ohkyounghun  | Doosan Bears |
 Kim Gun Woo   | @gunuzello    | Doosan Bears |
 Kwon Do Hun   | @kwon-dohun   | Doosan Bears |
 Kwon Sihyun | @bbirribbarribbo | P.P.G |
 Lee Jiseop  | @ljseop1030 | P.P.G |
 Na Hyanghee | @ihh25 | P.P.G |
 Minhee     | @MHJeong730 | BranchOps |
 Agnes      | @sengA7 | BranchOps |
 Sascha Huber     | @saschahuberzh | BranchOps |

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
