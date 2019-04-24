package cli.ssh;

import cli.orm.entity.RemoteHostEntity;
import cli.ssh.JSchClient;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import java.io.File;
import java.io.FileInputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.util.StringUtils;

/**
 * JSch factory
 *
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class JSchClientFactory extends BaseKeyedPooledObjectFactory<RemoteHostEntity, JSchClient> {

    @Override
    public JSchClient create(RemoteHostEntity remoteHost) throws Exception {
        JSch jSch = new JSch();
        boolean existKeyFile = StringUtils.hasText(remoteHost.getPermFile());
        if (existKeyFile) {
            byte[] privateKey = IOUtils.toByteArray(new FileInputStream(new File(remoteHost.getPermFile())));
            jSch.addIdentity(
                remoteHost.getAddress(), privateKey, null, null
            );
        }

        Session session = jSch.getSession(
            remoteHost.getUsername(),
            remoteHost.getAddress(),
            remoteHost.getSshPort()
        );

        session.setConfig("StrictHostKeyChecking", "no");
        session.setUserInfo(createUserInfo(remoteHost));

        if (!existKeyFile) {
            session.setPassword(remoteHost.getPassword());
        }

        session.setDaemonThread(true);

        return new JSchClient(session);
    }

    @Override
    public PooledObject<JSchClient> wrap(JSchClient value) {
        return new DefaultPooledObject<>(value);
    }

    @Override
    public void activateObject(RemoteHostEntity key, PooledObject<JSchClient> pool) throws Exception {
        JSchClient client = pool.getObject();
        Session sock = null;
        if (client != null && (sock = client.getSession()) != null) {
            sock.connect();
        }
    }

    @Override
    public void destroyObject(RemoteHostEntity key, PooledObject<JSchClient> pool) throws Exception {
        JSchClient session = pool.getObject();
        Session sock = null;

        if (session != null && (sock = session.getSession()) != null) {
            sock.disconnect();
        }
    }

    private UserInfo createUserInfo(RemoteHostEntity remoteHostEntity) {
        return new UserInfo() {
            @Override
            public String getPassphrase() {
                return null;
            }

            @Override
            public String getPassword() {
                return remoteHostEntity.getPassword();
            }

            @Override
            public boolean promptPassword(String message) {
                return false;
            }

            @Override
            public boolean promptPassphrase(String message) {
                return false;
            }

            @Override
            public boolean promptYesNo(String message) {
                return false;
            }

            @Override
            public void showMessage(String message) {

            }
        };
    }
}
