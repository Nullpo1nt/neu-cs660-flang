package martin.bryant.csu660;

public class With implements FLANG {
    Id id;
    FLANG m;
    FLANG body;
    
    public With(Id id, FLANG m, FLANG body) {
        this.id = id;
        this.m = m;
        this.body = body;
    }
    
    public FLANG eval() throws Exception {
        body = body.subst(id,m);
        System.out.println(">> Subst "+id+" -> "+m.toString()+"\n"+body.toString());
        return body.eval();
    }
    
    public FLANG subst(Id from, FLANG to) {
        return new With(id,
                m.subst(from,to),
                (id.id.equals(from.id))? body : body.subst(from,to));
    }
    
    public String toString() {
        return "(With "
            +id+" "
            +m+" "
            +body+")";
    }
}
