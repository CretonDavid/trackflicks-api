package com.studio.trackflicks.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
@SequenceGenerator(name = "tokens_seq", sequenceName = "tokens_seq", allocationSize = 10)
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "tokens_seq")
    public Long id;
    @Column(unique = true)
    public String token;
    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;
    public boolean revoked;
    public boolean expired;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    public User user;

}
