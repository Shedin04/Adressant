package com.shedin.adressant.sender.headers.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Lazy
public class ServiceHeadersHandlerFactory {
    private Map<String, ServiceStrategy> strategies;

    @Autowired
    public ServiceHeadersHandlerFactory(Set<ServiceStrategy> strategySet) {
        createStrategy(strategySet);
    }

    public ServiceStrategy getStrategy(String serviceName) {
        return strategies.get(serviceName);
    }

    private void createStrategy(Set<ServiceStrategy> strategySet) {
        strategies = new HashMap<>();
        strategySet.forEach(strategy -> strategies.put(strategy.getServiceName().getValue(), strategy));
    }
}
