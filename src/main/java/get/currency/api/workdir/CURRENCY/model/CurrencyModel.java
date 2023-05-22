package get.currency.api.workdir.CURRENCY.model;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "currency_history")
public class CurrencyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column
    String date;

    @Column(name = "time_create")
    String timeCreate;

    @Column(name = "usd_sale")
    String usdSale;

    @Column(name = "usd_buy")
    String usdBuy;

    @Column(name = "eur_sale")
    String eurSale;

    @Column(name = "eur_buy")
    String eurBuy;

    public CurrencyModel() {
        this.date = String.valueOf(LocalDate.now());
        this.timeCreate = String.valueOf(LocalTime.now());
    }

    public CurrencyModel(String usdSale, String usdBuy, String eurSale, String eurBuy) {
        this.date = String.valueOf(LocalDate.now());
        this.timeCreate = String.valueOf(LocalTime.now());
        this.usdSale = usdSale;
        this.usdBuy = usdBuy;
        this.eurSale = eurSale;
        this.eurBuy = eurBuy;
    }

}
