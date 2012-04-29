package martin.bryant.csu660;

public abstract class Arith implements FLANG {
    FLANG left  = null;
    FLANG right = null;
    
    protected int l = 0,
                  r = 0;
    
    public Arith(FLANG left, FLANG right) {
        this.left = left;
        this.right = right;
    }
    
    public void parseNumbers() throws Exception {
        FLANG tempL = left.eval();
        FLANG tempR = right.eval();
        
        if (tempL instanceof Num) {
            Num numL = (Num) tempL;
            l = Integer.parseInt(numL.number);
        } else {
            new Exception("Did not evaluate to a number: "+tempL.toString());
        }
        
        if (tempR instanceof Num) {
            Num numR = (Num) tempR;
            r = Integer.parseInt(numR.number);
        } else {
            new Exception("Did not evaluate to a number: "+tempR.toString());
        }
    }
}
