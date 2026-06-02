interface ATMState{
    
    insertCard(ATM,Card);
    enterPin(ATM,Pin);
    withDrawCash(ATM,amount);
    ejectCard(ATM);
    
}
public class IdleState implements ATMState{
    public void insertCard(ATM atm,Card card){
        atm.setCard(card);
        atm.setState(new Card_insertedState());
        System.out.print("Card Inserted");
    }
}

public class Card_insertedState implements ATMState{
    public void enterPin(ATM atm, int pin) {

    Card card = atm.getCurrentCard();

    if(card.getPin() == pin) {

        atm.setState(new PinVerifiedState());

        System.out.println("PIN verified");
    }
    else {

        card.decrementRetryCount();

        if(card.getRetryCount() == 0) {

            atm.ejectCard();

            atm.setState(new IdleState());

            System.out.println("Card blocked");
        }
        else {

            System.out.println(
                "Invalid PIN. Attempts left: "
                + card.getRetryCount()
            );
        }
    }
}
}

public class PinVerifiedState{
    withDrawCash(ATM atm,double amount){
        if(amount<=atmBalance && amount<= atm.currentCard.getBalance()){
            //if denomination combination exist to make up for withdrawn amount
            if(canWithDraw(atm,amount)){
                //withdrawn
                //update denomination,CardBalance,atmBalance
                atm.setState(new IdleState());
            }
        }
        else{
            System.out.println("not enough balance");
        }
    }
}



class ATM{
    private ATMState curAtmState;
    private Card currentCard;
    private double atmBalance;
    private Map<Integer,Integer>denominationCount;
    //getter and setters
}
