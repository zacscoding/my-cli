package cli.command.ssh;

import cli.ssh.JSchClient;
import cli.ssh.JSchClientPool;
import cli.orm.entity.RemoteHostEntity;
import cli.orm.repository.RemoteHostRepository;
import com.jcraft.jsch.ChannelShell;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RequiredArgsConstructor
@ShellComponent
@ShellCommandGroup("SSH commands")
public class SshCommand {

    private final RemoteHostRepository repository;
    private final JSchClientPool pool;

    @ShellMethod(value = "ssh remote host", key = "ssh_host")
    public String connectSSH(@ShellOption({"--hostname"}) String hostName) throws Exception {
        Optional<RemoteHostEntity> optional = repository.findByHostName(hostName);

        if (!optional.isPresent()) {
            return "Invalid host name : " + hostName;
        }

        final CountDownLatch terminate = new CountDownLatch(1);

        Thread shellThread = new Thread(() -> {
            try {
                JSchClient client = pool.borrowObject(optional.get());
                ChannelShell shell = (ChannelShell) client.getChannel("shell");
                shell.setInputStream(System.in);
                shell.setOutputStream(System.out);
                shell.connect();

                while (!Thread.currentThread().isInterrupted()) {
                    if (shell.isClosed() || !shell.isConnected()) {
                        terminate.countDown();
                    }

                    TimeUnit.MILLISECONDS.sleep(100L);
                }
            } catch (Exception e) {
                logger.error("Exception occur while connecting ssh", e);
                terminate.countDown();
            }
        });

        shellThread.setDaemon(true);
        shellThread.start();

        terminate.await();
        return "";
    }
}
