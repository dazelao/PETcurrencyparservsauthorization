package get.currency.api.workdir.CURRENCY.service;

import get.currency.api.workdir.CURRENCY.model.CurrencyModel;
import get.currency.api.workdir.CURRENCY.model.ExchangeRate;
import get.currency.api.workdir.CURRENCY.repository.CurrencyRepo;
import get.currency.api.workdir.PINGER.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CurrencyService {

    private static final String API_URL = "https://api.privatbank.ua/p24api/pubinfo?exchange&coursid=5";
    private final CurrencyRepo currencyRepo;
    private final TelegramBot telegramBot;
    @Autowired
    public CurrencyService(CurrencyRepo currencyRepo, TelegramBot telegramBot) {
        this.currencyRepo = currencyRepo;
        this.telegramBot = telegramBot;
    }

    public void saveNewCurrency(CurrencyModel currencyModel){
        Optional.ofNullable(currencyModel)
                .ifPresentOrElse(currencyRepo::save,
                        () -> {
                            throw new IllegalArgumentException("Попытка сохранить пустую модель валюты");
                        });
    }

    public List<CurrencyModel> getAll(){
        return Optional.ofNullable(currencyRepo.findAll())
                .orElseGet(ArrayList::new);
    }

    public CurrencyModel getLast(){
        return Optional.ofNullable(currencyRepo.findFirstByOrderByIdDesc())
                .orElseThrow(() -> new NoSuchElementException("Не найдена последняя модель валюты"));
    }

    public void fetchAndSaveCurrencyData() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ExchangeRate[]> response = restTemplate.getForEntity(API_URL, ExchangeRate[].class);
        ExchangeRate[] exchangeRates = response.getBody();

        if (exchangeRates != null && exchangeRates.length > 0) {
            String usdSale = null;
            String usdBuy = null;
            String eurSale = null;
            String eurBuy = null;

            for (ExchangeRate rate : exchangeRates) {
                if ("USD".equals(rate.getCcy())) {
                    usdSale = rate.getSale();
                    usdBuy = rate.getBuy();
                } else if ("EUR".equals(rate.getCcy())) {
                    eurSale = rate.getSale();
                    eurBuy = rate.getBuy();
                }
            }
            CurrencyModel currencyModel = new CurrencyModel(usdSale, usdBuy, eurSale, eurBuy);
            currencyRepo.save(currencyModel);
            telegramBot.sendTelegramMessage("Запись валюты" + LocalDateTime.now());
        }

    }

}
