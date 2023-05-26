package get.currency.api.workdir.CURRENCY.controller;

import get.currency.api.workdir.CURRENCY.model.CurrencyModel;
import get.currency.api.workdir.CURRENCY.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiv2")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/gethistory")
    @PreAuthorize("hasAuthority('developers:read')")
    public ResponseEntity<List<CurrencyModel>> findAll() {
        List<CurrencyModel> currency = currencyService.getAll();
        return ResponseEntity.ok(currency);
    }

    @PostMapping("/addnew")
    @PreAuthorize("hasAuthority('developers:write')")
    public ResponseEntity<CurrencyModel> saveNewCurrency(
            @RequestParam("usdSale") String usdSale,
            @RequestParam("usdBuy") String usdBuy,
            @RequestParam("eurSale") String eurSale,
            @RequestParam("eurBuy") String eurBuy)
    {
        CurrencyModel currencyModel = new CurrencyModel(usdSale, usdBuy, eurSale, eurBuy);
        currencyService.saveNewCurrency(currencyModel);
        return ResponseEntity.ok(currencyModel);
    }

    @GetMapping("/getlast")
    @PreAuthorize("hasAuthority('developers:write')")
    public CurrencyModel getLast(){
        return currencyService.getLast();
    }

//    @Scheduled(cron = "0 */2 * ? * *")
//    @Scheduled(cron = "0 0 */5 ? * *")
    @Scheduled(cron = "0 0 */6 ? * *")
    void saveNewDateFromApi(){
        currencyService.fetchAndSaveCurrencyData();
    }

}
