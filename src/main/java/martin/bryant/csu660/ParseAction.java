package martin.bryant.csu660;

import java.util.regex.Pattern;

public abstract class ParseAction {
    Pattern pattern;
    FLANGParser flangParser;
    
    public ParseAction(String regex) {
        pattern = Pattern.compile(regex);
        flangParser = FLANGParser.getInstance();
    }
    
    public Pattern getPattern() {
        return pattern;
    }
    
    public abstract FLANG action(String expr) throws Exception;
}
