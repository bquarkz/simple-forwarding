package com.niltonrc.simplef.core;


import com.niltonrc.simplef.exceptions.NotUniqueDataException;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractJPARepository< ENTITY extends DomainEntity< ID >, ID extends Serializable >
        implements IBasicRepository< ENTITY, ID >
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constants
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Special Fields And Injections
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Fields
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @PersistenceContext
    private EntityManager entityManager;

    private final IJpaDao< ENTITY, ID > dao;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected AbstractJPARepository( IJpaDao< ENTITY, ID > dao )
    {
        this.dao = dao;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Factories
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters And Setters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected EntityManager getEntityManager()
    {
        return entityManager;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public long countAll()
    {
        return dao.count();
    }

    @Override
    public long count( Predicate predicate )
    {
        return dao.count( predicate );
    }

    @Override
    public ENTITY save( ENTITY entity )
    {
        return dao.save( entity );
    }

    @Override
    public Optional< ENTITY > findById( ID id )
    {
        return dao.findById( id );
    }

    @Override
    public Stream< ENTITY > find( Predicate predicate )
    {
        return ( (List< ENTITY>) dao.findAll( predicate ) ).stream();
    }

    @Override
    public Stream< ENTITY > find( Predicate predicate, OrderSpecifier< ? >... orders )
    {
        return ( (List< ENTITY >) dao.findAll( predicate, orders ) ).stream();
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public Stream< ENTITY > find( Predicate predicate, Pageable pageable )
    {
        return dao.findAll( predicate, pageable ).stream();
    }

    @Override
    public Optional< ENTITY > findOne( Predicate predicate )
    {
        try
        {
            return dao.findOne( predicate );
        }
        catch( org.springframework.dao.IncorrectResultSizeDataAccessException ex )
        {
            throw new NotUniqueDataException( ex );
        }
    }

    @Override
    public Optional< ENTITY > findFirst( Predicate predicate )
    {
        return ( (List< ENTITY>) dao.findAll( predicate ) ).stream().findFirst();
    }

    @Override
    public Optional< ENTITY > findFirst( Predicate predicate, OrderSpecifier< ? >... orders )
    {
        return ( (List< ENTITY>) dao.findAll( predicate, orders ) ).stream().findFirst();
    }

    @Override
    public Stream< ENTITY > findAll()
    {
        return dao.findAll().stream();
    }

    @Override
    public < Q extends EntityPath< ENTITY > > JPAQuery< Q > query()
    {
        return new JPAQuery<>( getEntityManager() );
    }

    @Override
    public < Q extends EntityPath< ENTITY > > JPAUpdateClause update( Q q )
    {
        return new JPAUpdateClause( getEntityManager(), q );
    }

    @Override
    public < Q extends EntityPath< ENTITY > > JPADeleteClause delete( Q q )
    {
        return new JPADeleteClause( getEntityManager(), q );
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Inner Classes And Patterns
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
