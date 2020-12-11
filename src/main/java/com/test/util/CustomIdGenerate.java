package com.test.util;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

public class CustomIdGenerate implements IdentifierGenerator {


    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        Serializable id = sharedSessionContractImplementor.getEntityPersister(null, o).getClassMetadata().getIdentifier(o, sharedSessionContractImplementor);
        if (id == null) {
            id = UUID.randomUUID().toString().replace("-", "");
        }
        return id;
    }

    @Override
    public boolean supportsJdbcBatchInserts() {
        return true;
    }

}
