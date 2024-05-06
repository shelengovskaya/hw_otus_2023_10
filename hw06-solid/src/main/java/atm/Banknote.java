package atm;

public enum Banknote {
    THOUSAND(1000),
    HUNDRED(100),
    FIFTY(50),
    TWENTY(20),
    TEN(10);

    private final int banknote;

    Banknote(int banknote) {
        this.banknote = banknote;
    }

    public int getBanknote() {
        return this.banknote;
    }
}
