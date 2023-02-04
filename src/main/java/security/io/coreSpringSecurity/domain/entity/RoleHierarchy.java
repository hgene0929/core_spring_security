package security.io.coreSpringSecurity.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString(exclude = {"parentName", "roleHierarchy"})
public class RoleHierarchy implements Serializable {

    @Id @GeneratedValue
    public Long id;

    private String chileName;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_name", referencedColumnName = "chiled_name")
    private RoleHierarchy parentName;

    @OneToMany(mappedBy = "parentName", cascade = {CascadeType.ALL})
    private Set<RoleHierarchy> roleHierarchy = new HashSet<>();
}
