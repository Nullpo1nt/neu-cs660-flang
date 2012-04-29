package martin.bryant.csu660;

public class Div extends Arith {
    public Div(FLANG left, FLANG right) {
        super(left,right);
    }
    
    public FLANG eval() throws Exception {
        super.parseNumbers();
        Integer result = new Integer(l/r);
        System.out.println(">> "+l+"/"+r+"="+result.toString());
        return new Num(result.toString());
    }
    
    public FLANG subst(Id from, FLANG to) {
        return new Add(left.subst(from,to),right.subst(from,to));
    }
    
    public String toString() {
        return "(Div "+left.toString()+" "+right.toString()+")";
    }
}
