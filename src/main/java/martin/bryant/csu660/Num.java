package martin.bryant.csu660;
//
public class Num implements FLANG {
    String number;
    
    public Num(String number) {
        this.number = number;
    }
    
    public FLANG eval() {
        return this;
    }
    
    public FLANG subst(Id from, FLANG to) {
        return this;
    }
    
    public String toString() {
        return "(Num "+number+")";
    }
}
