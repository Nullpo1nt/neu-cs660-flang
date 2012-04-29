package martin.bryant.csu660;

public class Add extends Arith {
    public Add(FLANG left, FLANG right) {
        super(left,right);
    }
    
    public FLANG eval() throws Exception {
        super.parseNumbers();
        Integer result = new Integer(l+r);
        System.out.println(">> "+l+"+"+r+"="+result.toString());
        return new Num(result.toString());
    }
    
    public FLANG subst(Id from, FLANG to) {      
        return new Add(left.subst(from,to),right.subst(from,to));
    }
    
    public String toString() {
        return "(Add "+left.toString()+" "+right.toString()+")";
    }
}
