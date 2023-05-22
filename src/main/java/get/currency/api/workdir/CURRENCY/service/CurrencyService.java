package get.currency.api.workdir.CURRENCY.service;

import get.currency.api.workdir.CURRENCY.controller.model.CurrencyModel;
import get.currency.api.workdir.CURRENCY.controller.model.ExchangeRate;
import get.currency.api.workdir.CURRENCY.repository.CurrencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CurrencyService {

    private static final String API_URL = "https://api.privatbank.ua/p24api/pubinfo?exchange&coursid=5";
    private final CurrencyRepo currencyRepo;
    @Autowired
    public CurrencyService(CurrencyRepo currencyRepo) {
        this.currencyRepo = currencyRepo;
    }

    public void saveNewCurrency(CurrencyModel currencyModel){
        Optional.ofNullable(currencyModel)
                .ifPresentOrElse(currencyRepo::save,
                        () -> {
                            throw new IllegalArgumentException("Попытка сохранить пустую модель валюты");
                        });
    }

    public List<CurrencyModel> findAll(){
        return Optional.ofNullable(currencyRepo.findAll())
                .orElseGet(ArrayList::new);
    }

    public CurrencyModel findLast(){
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
        }

    }

}
