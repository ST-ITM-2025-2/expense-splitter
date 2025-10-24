# Settlement Feature (Issue #5)

This feature helps settle expenses in a group.  
It checks who owes money and who should get paid.  
Then it makes simple payments so everyone ends up even.

We tried to keep the code short and easy.  
To save each person’s balance, we used a `Map<>`.  
It was simple to match names with amounts, and sorting the list made the logic work better.

The algorithm is simple.  
The person who owes the most pays the person who should get the most.  
This keeps going until everyone is even.  
Small amounts (less than 0.005) are ignored.


---

### Example
| Person | Balance |
|---------|----------|
| Alice   | -30 |
| Bob     | +10 |
| Charlie | +20 |

**Result**
Alice → Charlie: 20
Alice → Bob: 10
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
 Kim Sinwoo     | @yourgithubid | Team F1 | 3|
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

### Tests
| Test | Description | Expected Result |
|------|--------------|----------------|
| `simpleTest()` | One debtor and one creditor | One payment (Alice → Bob: 30) |
| `tinyAmountTest()` | Very small numbers | No payment created |

Run:
```bash
mvn test
