package martin.bryant.csu660;

public class Id implements FLANG {
    String id;
    
    public Id(String id) {
        this.id = id;
    }
    
    public FLANG eval() throws Exception {
        throw new Exception("Free identifier: "+this.toString());
    }
    
    public FLANG subst(Id from, FLANG to) {
        return (id.equals(from.id)) ? to : this;
    }
    
    public String toString() {
        return "(Id '"+id+")";
    }
}
