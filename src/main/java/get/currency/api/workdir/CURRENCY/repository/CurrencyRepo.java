package get.currency.api.workdir.CURRENCY.repository;

import get.currency.api.workdir.CURRENCY.controller.model.CurrencyModel;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CurrencyRepo extends JpaRepository<CurrencyModel, Long> {

    CurrencyModel findFirstByOrderByIdDesc();


}
