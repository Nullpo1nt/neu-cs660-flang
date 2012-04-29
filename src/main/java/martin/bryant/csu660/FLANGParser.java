package martin.bryant.csu660;

import java.util.ArrayList;
import java.util.regex.*;

public class FLANGParser {
    private static FLANGParser _instance = null;
    private ArrayList patterns = new ArrayList();
    
    private FLANGParser() {
        _instance = this;
        
        // Add: { + <FLANG> <FLANG> }
        patterns.add(new ParseAction("\\s*\\{\\s*\\+\\s+.+\\s+.+\\s*\\}") {
            public FLANG action(String expr) throws Exception {
                ArrayList subsets = getSubsets(expr);
                
                if(subsets.size() > 3)
                    throw new Exception("Bad 'add' syntax, too many arguments: "+expr);
                                
                return new Add(flangParser.parse((String)subsets.get(1)),
                        flangParser.parse((String)subsets.get(2)));
            }
        });
        // Mul: { * <FLANG> <FLANG> }
        patterns.add(new ParseAction("\\{\\s*\\*\\s+.+\\s+.+\\s*\\}") {
            public FLANG action(String expr) throws Exception {
                ArrayList subsets = getSubsets(expr);
                
                if(subsets.size() > 3)
                    throw new Exception("Bad 'mul' syntax, too many arguments: "+expr);
                                
                return new Mul(flangParser.parse((String)subsets.get(1)),
                        flangParser.parse((String)subsets.get(2)));
            }
        });
        // Sub: { - <FLANG> <FLANG> }
        patterns.add(new ParseAction("\\{\\s*-\\s+.+\\s+.+\\s*\\}") {
            public FLANG action(String expr) throws Exception {
                ArrayList subsets = getSubsets(expr);
                
                if(subsets.size() > 3)
                    throw new Exception("Bad 'sub' syntax, too many arguments: "+expr);
                                
                return new Sub(flangParser.parse((String)subsets.get(1)),
                        flangParser.parse((String)subsets.get(2)));
            }
        });
        // Div: { / <FLANG> <FLANG> }
        patterns.add(new ParseAction("\\{\\s*/\\s+.+\\s+.+\\s*\\}") {
            public FLANG action(String expr) throws Exception {
                ArrayList subsets = getSubsets(expr);
                
                if(subsets.size() > 3)
                    throw new Exception("Bad 'div' syntax, too many arguments: "+expr);
                                
                return new Div(flangParser.parse((String)subsets.get(1)),
                        flangParser.parse((String)subsets.get(2)));
            }
        });
        // With: { with { <id> <FLANG> } <FLANG> }
        patterns.add(new ParseAction("\\{\\s*with\\s+\\{\\s*.+\\s+.+\\s*\\}\\s*.+\\s*\\}") {
            public FLANG action(String expr) throws Exception {
                ArrayList al  = getSubsets(expr);
                ArrayList als = getSubsets((String)al.get(1));

                if(al.size() > 3)
                    throw new Exception("Bad 'with' syntax, too many arguments: "+expr);
                if(als.size() > 2)
                    throw new Exception("Bad 'with' syntax, too many arguments: "
                            +(String)al.get(1));

                FLANG id = flangParser.parse(((String)als.get(0)).trim());
                if(!(id instanceof Id))
                    throw new Exception("Bad 'with' syntax, '"+id.toString()+"' is not an Id.");
                
                return new With((Id)flangParser.parse((String)als.get(0)),
                        flangParser.parse((String)als.get(1)),
                        flangParser.parse((String)al.get(2)));
            }
        });
        // Fun: { fun { <id> } <FLANG> }
        patterns.add(new ParseAction("\\{\\s*fun\\s+\\{\\s*.+\\s*\\}\\s*.+\\s*\\}") {
            public FLANG action(String expr) throws Exception {
                ArrayList al = getSubsets(expr);
                if(al.size() > 3)
                    throw new Exception("Bad 'fun' syntax, too many arguments: "+expr);

                FLANG id = flangParser.parse(((String)al.get(1)).replaceAll("\\{|\\}|\\s+",""));
                if(!(id instanceof Id))
                    throw new Exception("Bad 'fun' syntax, '"+id.toString()+"' is not an Id.");

                return new Fun((Id)id, flangParser.parse((String)al.get(2)));
            } 
        });
        // Call: { call <FLANG> <FLANG> }
        patterns.add(new ParseAction("\\{\\s*call\\s+.+\\s+.+\\s*\\}") {
            public FLANG action(String expr) throws Exception {
                ArrayList al = getSubsets(expr);
                
                if(al.size() > 3)
                    throw new Exception("Bad 'call' syntax, too many aruments: "+expr);
                
                return new Call(flangParser.parse((String)al.get(1)),
                        flangParser.parse((String)al.get(2)));
            }
        });
        // Num : <num>
        patterns.add(new ParseAction("\\d+") {
            public FLANG action(String expr) throws Exception {
                return new Num(expr);
            }
        });
        // Id: <id>
        patterns.add(new ParseAction("[\\S+&&[^{}]]+") {
            public FLANG action(String expr) throws Exception {
                return new Id(expr.replaceAll("\\{|\\}|\\s+",""));
            }
        });
    }
    
    public static FLANGParser getInstance() {
        if(_instance == null) {
            return new FLANGParser();
        }
        
        return _instance;
    }

    
    public FLANG parse(String expr) throws Exception {
        // Remove indentation, extra spaces, ect to help regex matcher...
        String input = expr.replaceAll("\\s+"," ").trim();
        
        for(int x = 0, y = patterns.size(); x < y; x++) {
            ParseAction p = (ParseAction) patterns.get(x);
            if(p != null) {
                Matcher m = p.getPattern().matcher(input);
                if(m.matches()) {

                    System.out.println("Matched: \""
                            +input+"\" with \""
                            +p.getPattern().pattern()+"\".");

                    return p.action(expr);
                }
            }    
        }

        throw new Exception("Unknown syntax in \'"+expr+"'");
    }
    
    // Nevermind this mess, just returns a list of items in an expression
    public ArrayList getSubsets(String expr) {
        ArrayList subsets = new ArrayList();
        int subfunc = 0;
        String s = "";
        
        // Assume expr == "{.*}" and remove the first "{" and last "}" then spilt
        String subExpr[] = expr.trim().substring(1,expr.length()-1).split("\\s+");
      
        for(int x = 0; x < subExpr.length; x++) {
//            System.out.println(subExpr.length+" "+subfunc+" "+((String)subExpr[x]).length()+" >> "+subExpr[x]);
            if(((String)subExpr[x]).length() > 0) {
                if(subExpr[x].charAt(0) == '{') {
                    if(subfunc == 0)
                        s = "";
                    for(int z = 0; z < subExpr[x].length(); z++) {
                        if(subExpr[x].charAt(z) == '{') {
                            subfunc += 1;
                        }
                    }
                }
                if(subfunc > 0) {
                    s += subExpr[x]+" ";
                    for(int z = subExpr[x].length()-1; z >= 0; z--) {
                        if(subExpr[x].charAt(z) == '}') {
                            subfunc -= 1;
                        }
                    }
                } else {
                    s = subExpr[x];
                }              
                if(subfunc == 0 || subExpr.length == x+1) {
                    subsets.add(s.trim());
                }
            }
        }
        
        for(int x = 0; x < subsets.size(); x++)
           System.out.println("subsets["+x+"] = "+(String)subsets.get(x));
        return subsets;
    }
}
