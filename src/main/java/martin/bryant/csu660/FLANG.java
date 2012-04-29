package martin.bryant.csu660;

public interface FLANG {
    public FLANG eval() throws Exception;
    public FLANG  subst(Id from, FLANG to);
    public String toString();
}
