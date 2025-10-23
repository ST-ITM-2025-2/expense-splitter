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

---

### Tests
| Test | Description | Expected Result |
|------|--------------|----------------|
| `simpleTest()` | One debtor and one creditor | One payment (Alice → Bob: 30) |
| `tinyAmountTest()` | Very small numbers | No payment created |

Run:
```bash
mvn test
