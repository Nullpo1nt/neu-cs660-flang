package martin.bryant.csu660;

public class Mul extends Arith {
    public Mul(FLANG left, FLANG right) {
        super(left,right);
    }
    
    public FLANG eval() throws Exception {
        super.parseNumbers();
        Integer result = new Integer(l*r);
        System.out.println(">> "+l+"*"+r+"="+result.toString());
        return new Num(result.toString());
    }
    
    public FLANG subst(Id from, FLANG to) {
        return new Mul(left.subst(from,to),right.subst(from,to));
    }
    
    public String toString() {
        return "(Mul "+left.toString()+" "+right.toString()+")";
    }
}
