package com.niltonrc.simplef.core;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Stream;

public interface IBasicRepository< ENTITY extends DomainEntity< ID >, ID extends Serializable >
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static fields
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Default Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Contracts
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    long countAll();
    long count( Predicate predicate );
    ENTITY save( ENTITY entity );
    Optional< ENTITY > findById( ID id );
    Stream< ENTITY > find( Predicate predicate );
    Stream< ENTITY > find(
            Predicate predicate,
            OrderSpecifier< ? >... orders );
    Stream< ENTITY > find(
            Predicate predicate,
            Pageable pageable );
    Optional< ENTITY > findOne( Predicate predicate );
    Optional< ENTITY > findFirst( Predicate predicate );
    Optional< ENTITY > findFirst(
            Predicate predicate,
            OrderSpecifier< ? >... orders );
    Stream< ENTITY > findAll();
    < Q extends EntityPath< ENTITY > > JPAQuery< Q > query();
    < Q extends EntityPath< ENTITY > > JPAUpdateClause update( Q q );
    < Q extends EntityPath< ENTITY > > JPADeleteClause delete( Q q );
}
