package get.currency.api.workdir.CURRENCY.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    private String ccy;
    private String sale;
    private String buy;
}
