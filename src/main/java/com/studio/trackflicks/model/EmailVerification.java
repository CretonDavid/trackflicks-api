package com.studio.trackflicks.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "email_verification")
@SequenceGenerator(name = "email_verification_seq", sequenceName = "email_verification_seq", allocationSize = 10)
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "verify_seq")
    private Long id;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "expire_date")
    private LocalDateTime expireDate;
    @Column(name = "verified_date")
    private LocalDateTime verifiedDate;
    private String code;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "users_id")
    private User user;

}
