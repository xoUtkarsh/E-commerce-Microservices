package com.ecommerce.gateway;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;

@Component
public class LoggingFilter implements GlobalFilter {
}
