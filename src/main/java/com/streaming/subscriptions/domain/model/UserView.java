package com.streaming.subscriptions.domain.model;

import java.time.Instant;

/**
 * Modelo de leitura do usuário, que representa como a API devolve os dados de um usuário.
 * É um record, ou seja, um tipo imutável e simples para leitura/respose.
 *
 * Mesmo não sendo uma unidade persistida, representa um contrato de leitura estável. Então é interessante estar no domínio.
 * Além disso, evita acoplamento. Se retornasse UserEntity, estaria acoplando o contrato HTTP a detalhes do banco.
 *
 * Como ele é construído:
 * Ele é montado nos adapters de persistência (CreateUserAdapter e UserQueryAdapter); na criação do usuário o adapter retorna UserView.
 */
public record UserView(
        Long id,
        String fullName,
        Instant createdAt,
        Long subscriptionId,
        String subscriptionStatus
) {}