package martin.bryant.csu660;

public class Fun implements FLANG {
    Id id;
    FLANG body;
    
    public Fun(Id id, FLANG body) {
        this.id = id;
        this.body = body;
    }
    
    public FLANG eval() throws Exception {
        return body.eval();
    }
    
    public FLANG subst(Id from, FLANG to) {
        if(!(id.equals(from))) {  // Not same java object...
            return this;
        }
        return new Fun(id,body.subst(from, to));
    }
    
    public String toString() {
        return "(Fun "+id.toString()+" "+body.toString()+")";
    }
}
