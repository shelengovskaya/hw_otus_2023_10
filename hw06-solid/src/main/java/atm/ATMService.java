package atm;

import java.util.List;
import java.util.Map;

public class ATMService implements ATMServiceInterface {
    private final Storage storage;

    public ATMService() {
        this.storage = new Storage();
        initStorage();
    }

    private void initStorage() {
        storage.acceptBill(Banknote.THOUSAND);
        storage.acceptBill(Banknote.HUNDRED);
        storage.acceptBill(Banknote.FIFTY);
        storage.acceptBill(Banknote.TWENTY);
        storage.acceptBill(Banknote.TEN);
    }

    @Override
    public int getBalance() {
        return storage.getBalance();
    }

    @Override
    public void depositCash(List<Banknote> banknotes) {
        banknotes.forEach(storage::acceptBill);
    }

    @Override
    public Map<Banknote, Integer> withdrawCash(int sum) {
        if (sum <= 0) {
            throw new RuntimeException("Invalid sum for withdrawing cash.");
        }
        return storage.getBills(sum);
    }
}
