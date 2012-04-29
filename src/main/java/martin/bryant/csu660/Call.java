package martin.bryant.csu660;

public class Call implements FLANG {
    FLANG fun;
    FLANG arg;
    
    public Call(FLANG fun, FLANG arg) {
        this.fun = fun;
        this.arg = arg;
    }
    
    public FLANG eval() throws Exception {
        if (fun instanceof Fun) {
            Fun temp = (Fun) fun;
            temp = (Fun)temp.subst(temp.id,arg);
            System.out.println(">> Subst "+temp.id.toString()+" -> "+arg.toString()+"\n"+temp.toString());
            return temp.eval();
        }
        
        throw new Exception("'call' expects a function, got: "+fun.toString());
    }
    
    public FLANG subst(Id from, FLANG to) {
        return new Call(fun.subst(from,to),arg.subst(from,to));
    }
    
    public String toString() {
        return "(Call "+fun.toString()+" "+arg.toString()+")";
    }
}
