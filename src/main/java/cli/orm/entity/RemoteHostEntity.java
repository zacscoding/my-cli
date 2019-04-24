package cli.orm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

/**
 * Remote host entity
 *
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "id")
@Entity(name = "RemoteHost")
@Table(name = "REMOTE_HOST")
public class RemoteHostEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "HOST_NAME", unique = true)
    private String hostName;

    // host address
    @Column(name = "ADDRESS")
    private String address;

    // ssh port
    @Column(name = "SSH_PORT")
    private Integer sshPort;

    // ssh username
    @Column(name = "USERNAME")
    private String username;

    // ssh password
    @Column(name = "PASSWORD")
    private String password;

    // ssh perm file path
    @Column(name = "PERM_FILE")
    private String permFile;

    public String toSimpleString() {
        return new StringBuilder()
            .append(username)
            .append("@")
            .append(address)
            .append(" -p ").append(sshPort)
            .append(" has perm file : ").append(StringUtils.hasText(permFile))
            .toString();
    }
}
