package com.AgateToBDEQT.AgateToBDEQT.test3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomerCreditItemProcessor implements ItemProcessor<CustomerCredit, CustomerCredit> {

    private static final Logger log = LoggerFactory.getLogger(CustomerCreditItemProcessor.class);

    @Override
    public CustomerCredit process(final CustomerCredit customerCredit) throws Exception {
        final String name = customerCredit.getName().toUpperCase();
        final int credit = customerCredit.getCredit();

        final CustomerCredit transformCC = new CustomerCredit(customerCredit.getId(),name, credit);

        log.info("Converting (" + customerCredit + ") into (" + transformCC + ")");

        return transformCC;
    }

}