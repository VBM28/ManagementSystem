package login;

public enum Option {
    Admin , User;

    private Option(){}

    public String value(){
        return name();
    }

    public static Option fromvalue(String v){
        return valueOf(v);
    }
}
