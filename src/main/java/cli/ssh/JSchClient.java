package cli.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@RequiredArgsConstructor
public class JSchClient {

    private final Session session;

    /**
     * Getting channel by type
     */
    public Channel getChannel(String type) throws JSchException {
        Channel channel = session.openChannel(type);
        return channel;
    }

    /**
     * Getting SftpChannel
     */
    public ChannelSftp getSftpChannel() throws JSchException {
        return (ChannelSftp) getChannel("sftp");
    }
}
