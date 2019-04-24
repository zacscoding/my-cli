package cli.ssh;

import cli.orm.entity.RemoteHostEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class JSchClientPool extends GenericKeyedObjectPool<RemoteHostEntity, JSchClient> {

    public JSchClientPool(KeyedPooledObjectFactory<RemoteHostEntity, JSchClient> factory,
        GenericKeyedObjectPoolConfig<JSchClient> config) {
        super(factory, config);
    }
}
