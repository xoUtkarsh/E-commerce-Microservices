//package com.ecommerce.order.clients;
//
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestClient;
//import org.springframework.web.client.support.RestClientAdapter;
//import org.springframework.web.service.invoker.HttpServiceProxyFactory;
//
//@Configuration
//public class ProductServiceClientConfig {
//
//    @Bean
//    @LoadBalanced
//    public RestClient.Builder restClientBuilder(){
//        return RestClient.builder();
//    }
//
//    @Bean
//    public ProductServiceClient restClientInterface(RestClient.Builder restClientBuilder){
//        RestClient restClient=restClientBuilder.baseUrl("http://product-service").build();
//        RestClientAdapter adapter=RestClientAdapter.create(restClient);
//        HttpServiceProxyFactory factory= HttpServiceProxyFactory.builderFor(adapter).build();
//        ProductServiceClient productServiceClient=factory.createClient(ProductServiceClient.class);
//        return productServiceClient;
//    }
//}

package com.ecommerce.order.clients;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class ProductServiceClientConfig {


    @Bean
    public ProductServiceClient productServiceClientInterface(@LoadBalanced RestClient.Builder loadBalancedRestClientBuilder) {
        RestClient restClient = loadBalancedRestClientBuilder
                .baseUrl("http://product-service")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError,
                        ((request, response) -> Optional.empty()))
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ProductServiceClient.class);
    }
}
