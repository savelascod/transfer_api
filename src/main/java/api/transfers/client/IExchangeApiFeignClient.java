package api.transfers.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import api.transfers.dto.response.ExchangeApiResponseDto;

@FeignClient(name = "exchange-api", url = "${yellowpepper.excahnge.api.url}")
public interface IExchangeApiFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "/latest")
    ExchangeApiResponseDto getExchanges(@RequestParam String base);
}
