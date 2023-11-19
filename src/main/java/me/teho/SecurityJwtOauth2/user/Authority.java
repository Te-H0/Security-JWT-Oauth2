package me.teho.SecurityJwtOauth2.user;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "authority")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    @Column(name = "Authority_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "authority_name", length = 50)
    private String authorityName;
}
