package get.currency.api.workdir.CURRENCY.controller;

import get.currency.api.workdir.CURRENCY.model.CurrencyModel;
import get.currency.api.workdir.CURRENCY.service.CurrencyService;
import get.currency.api.workdir.PINGER.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@RestController
@RequestMapping("/api/v2/currency")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final TelegramBot telegramBot;

    @Autowired
    public CurrencyController(CurrencyService currencyService, TelegramBot telegramBot) {
        this.currencyService = currencyService;
        this.telegramBot = telegramBot;
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
@Scheduled(cron = "0 0 */2 ? * *")
    void saveNewDateFromApi(){
        telegramBot.sendTelegramMessage("Обновление информации в базе курса валют, следующее обновление через 2 часа");
        currencyService.fetchAndSaveCurrencyData();
    }

}
