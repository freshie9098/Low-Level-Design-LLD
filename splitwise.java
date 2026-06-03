User

Group

Expense

Split

SplitStrategy
 ├── EqualSplitStrategy
 ├── ExactSplitStrategy
 └── PercentageSplitStrategy

ExpenseManager

BalanceSheet
User: userId
Expense:
Group:
Split:user,amount
SplitStrategy:
ExpenseManager:addExpense(),updateBalance(),showbalance()
BalanceSheet:map<user,map<user,amount>>balanceSheet;
who owes whom how much

Expense:
paidBy user,amount,list<user>membersOfExpense,SplitStrategy


balanceSheet:
map<user,map<user,amount>>balanceSheet;
addExpense()
public Expense addExpense(
        User paidBy,
        double amount,
        List<User> participants,
        SplitStrategy strategy) {

    List<Split> splits =
            strategy.calculateSplit(
                    paidBy,
                    amount,
                    participants);

    Expense expense =
            new Expense(
                    paidBy,
                    amount,
                    splits,
                    strategy);

    updateBalanceSheet(expense);

    return expense;
}
updateBalanceSheet()
public void updateBalanceSheet(Expense expense) {

    User paidBy = expense.getPaidBy();

    List<Split> splits = expense.getSplits();

    for (Split split : splits) {

        User user = split.getUser();

        if (user.equals(paidBy)) {
            continue;
        }

        addDebt(
                user,           // debtor
                paidBy,         // creditor
                split.getAmount()
        );
    }
}
Core Logic: addDebt()

This is where netting happens.

private void addDebt(
        User debtor,
        User creditor,
        double amount) {

    balanceSheet.putIfAbsent(
            debtor,
            new HashMap<>());

    balanceSheet.putIfAbsent(
            creditor,
            new HashMap<>());

    double reverseDebt =
            balanceSheet
                    .get(creditor)
                    .getOrDefault(debtor, 0.0);

    if (reverseDebt == 0) {

        double existingDebt =
                balanceSheet
                        .get(debtor)
                        .getOrDefault(creditor, 0.0);

        balanceSheet
                .get(debtor)
                .put(
                    creditor,
                    existingDebt + amount
                );
    }
    else {

        if (reverseDebt > amount) {

            balanceSheet
                    .get(creditor)
                    .put(
                        debtor,
                        reverseDebt - amount
                    );
        }
        else if (reverseDebt < amount) {

            balanceSheet
                    .get(creditor)
                    .remove(debtor);

            balanceSheet
                    .get(debtor)
                    .put(
                        creditor,
                        amount - reverseDebt
                    );
        }
        else {

            balanceSheet
                    .get(creditor)
                    .remove(debtor);
        }
    }
}
ExpenseManager:
public Expense addExpense(paidBy,amount,list<user>participants,SplitStrategy) {

	return SplitStrategy.calculateSplit(paidBy,amount,list<user>participants,SplitStrategy);
	
	//updateBalanceSheet
}

public class EqualSplitStrategy {
	public Split calculateSplit(paidBy,amount,list<user>participants,SplitStrategy) {
		int n = participants.size();
		double sharePerHead = amount/n;
		list<split>splits;
		for(user user:participants) {
			if(user==paidBy) {
				splits.add(user,sharePerHead-amount);
			}
			else {
				splits.add(user,sharePerHead);
			}
		}
		return splits;
	}

}
