package patterns.p12_ChainOfResponsibility;

abstract class MoneyHandler{
    protected MoneyHandler moneyHandler;

    public MoneyHandler() {
        this.moneyHandler = null;
    }


    public void setNextHandler(MoneyHandler moneyHandler) {
        this.moneyHandler = moneyHandler;
    }

    public abstract void dispense(int amount);
}


class ThousandRuppesshandler extends MoneyHandler{
    private int mNumNotes;

    public ThousandRuppesshandler(int notes) {
        this.mNumNotes = notes;
    }

    @Override
    public void dispense(int amount) {
        // 4000 
        int notesNeeded = amount / 1000;

        if(notesNeeded > mNumNotes) {
            notesNeeded = mNumNotes;
            mNumNotes = 0;
        } else{
            mNumNotes = mNumNotes - notesNeeded;
        }

        if(notesNeeded > 0) 
            System.out.println("Dispensing : " + notesNeeded + " x ₹1000");

        int remainingAmount = amount - (1000 * notesNeeded);

        if(remainingAmount > 0) {
            if(moneyHandler!=null) {
                moneyHandler.dispense(remainingAmount);
            }else{
                System.out.println("insuffiecient funds in ATM");
            }
        }

    }
    
}


class FiveHundredRuppesshandler extends MoneyHandler{
    private int mNumNotes;

    public FiveHundredRuppesshandler(int notes) {
        this.mNumNotes = notes;
    }

    @Override
    public void dispense(int amount) {
        // 4000 
        int notesNeeded = amount / 500;

        if(notesNeeded > mNumNotes) {
            notesNeeded = mNumNotes;
            mNumNotes = 0;
        } else{
            mNumNotes = mNumNotes - notesNeeded;
        }

        if(notesNeeded > 0) 
            System.out.println("Dispensing : " + notesNeeded + " x ₹500");

        int remainingAmount = amount - (500 * notesNeeded);

        if(remainingAmount > 0) {
            if(moneyHandler!=null) {
                moneyHandler.dispense(remainingAmount);
            }else{
                System.out.println("insuffiecient funds in ATM");
            }
        }

    }
    
}


class HundredRuppesshandler extends MoneyHandler{
    private int mNumNotes;

    public HundredRuppesshandler(int notes) {
        this.mNumNotes = notes;
    }

    @Override
    public void dispense(int amount) {
        // 4000 
        int notesNeeded = amount / 100;

        if(notesNeeded > mNumNotes) {
            notesNeeded = mNumNotes;
            mNumNotes = 0;
        } else{
            mNumNotes = mNumNotes - notesNeeded;
        }

        if(notesNeeded > 0) 
            System.out.println("Dispensing : " + notesNeeded + " x ₹100");

        int remainingAmount = amount - (100 * notesNeeded);

        if(remainingAmount > 0) {
            if(moneyHandler!=null) {
                moneyHandler.dispense(remainingAmount);
            }else{
                System.out.println("insuffiecient funds in ATM");
            }
        }

    }
    
}

class FiftyRuppesshandler extends MoneyHandler{
    private int mNumNotes;

    public FiftyRuppesshandler(int notes) {
        this.mNumNotes = notes;
    }

    @Override
    public void dispense(int amount) {
        // 4000 
        int notesNeeded = amount / 50;

        if(notesNeeded > mNumNotes) {
            notesNeeded = mNumNotes;
            mNumNotes = 0;
        } else{
            mNumNotes = mNumNotes - notesNeeded;
        }

        if(notesNeeded > 0) 
            System.out.println("Dispensing : " + notesNeeded + " x ₹50");

        int remainingAmount = amount - (50 * notesNeeded);

        if(remainingAmount > 0) {
            if(moneyHandler!=null) {
                moneyHandler.dispense(remainingAmount);
            }else{
                System.out.println("insuffiecient funds in ATM");
            }
        }

    }
    
}


public class ChainOfResponsibility {
    public static void main(String[] args) {
        MoneyHandler thousand = new ThousandRuppesshandler(3);
        MoneyHandler fivehundred = new FiveHundredRuppesshandler(10);
        MoneyHandler hundred = new HundredRuppesshandler(10);
        MoneyHandler fifty = new FiftyRuppesshandler(20);

        thousand.setNextHandler(fivehundred);
        fivehundred.setNextHandler(hundred);
        hundred.setNextHandler(fifty);

        int withdraw = 4000;

        System.out.println("Withdrawing money from ATM");
        thousand.dispense(withdraw);

    }
}
