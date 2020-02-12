package org.nipu.po.order.clients;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * TODO: This is autogenerated Java-Doc.
 *
 * @author Nikita_Puzankov
 */
@FeignClient(
        name = "pc",
        fallback = ProductSpecificationRepository.ProductSpecificationRepositoryFallback.class)
public interface ProductSpecificationRepository {

    @GetMapping("/catalog/{specificationId}")
    Object existsById(@PathVariable("specificationId") String specificationId);

    @Component
    @Slf4j
    class ProductSpecificationRepositoryFallback implements ProductSpecificationRepository{

        @Override
        public Object existsById(String specificationId) {
            log.debug("running hystrix fallback");
            return null;
        }
    }
}