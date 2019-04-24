package cli.command.host;

import cli.orm.entity.RemoteHostEntity;
import cli.orm.repository.RemoteHostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * Remote host CRUD command
 *
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RequiredArgsConstructor
@ShellComponent
@ShellCommandGroup("Host commands")
public class HostCommand {

    private final RemoteHostRepository hostRepository;

    @ShellMethod(value = "register remote host", key = "host_register")
    public String register(@ShellOption(value = {"--hostname"}, defaultValue = "") String hostName,
        @ShellOption(value = {"--address"}, defaultValue = "") String address,
        @ShellOption(value = {"--port"}, defaultValue = "22") Integer sshPort,
        @ShellOption(value = {"--username"}, defaultValue = "") String username,
        @ShellOption(value = {"--password"}, defaultValue = "") String password,
        @ShellOption(value = {"--perm"}, defaultValue = "") String permFile) {

        RemoteHostEntity entity = RemoteHostEntity.builder()
            .hostName(hostName)
            .address(address)
            .sshPort(sshPort)
            .username(username)
            .password(password)
            .permFile(permFile)
            .build();

        RemoteHostEntity updated = hostRepository.save(entity);
        return String.format(
            "Success to save host. [id : %d, name : %s]", updated.getId(), updated.getHostName()
        );
    }

    @ShellMethod(value = "display remote hosts", key = "host_gets")
    public String findAll() {
        List<RemoteHostEntity> hosts = hostRepository.findAll();
        StringBuilder builder = new StringBuilder("// --------------------- Display hosts ---------------------\n");

        int idx = 1;

        for (RemoteHostEntity host : hosts) {
            builder.append(idx++).append("::").append(host.toSimpleString()).append("\n");
        }

        builder.append("----------------------------------------------------------");
        return builder.toString();
    }
}
