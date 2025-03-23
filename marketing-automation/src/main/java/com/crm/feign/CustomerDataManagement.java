package com.crm.feign;
import org.springframework.cloud.openfeign.FeignClient;
@FeignClient("api-gateway")
public interface CustomerDataManagement {
}
