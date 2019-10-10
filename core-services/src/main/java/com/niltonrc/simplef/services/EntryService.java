package com.niltonrc.simplef.services;

import com.niltonrc.simplef.contracts.IEntryRepository;
import com.niltonrc.simplef.contracts.IEntryService;
import com.niltonrc.simplef.dtos.ForwardingBundle;
import com.niltonrc.simplef.dtos.PathDto;
import com.niltonrc.simplef.dtos.StatisticsDto;
import com.niltonrc.simplef.entities.EntryEntity;
import com.niltonrc.simplef.entities.QEntryEntity;
import com.niltonrc.simplef.messages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class EntryService
    implements IEntryService
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constants
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Special Fields And Injections
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private final IEntryRepository entryRepository;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Fields
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Autowired
    protected EntryService( IEntryRepository entryRepository )
    {
        this.entryRepository = entryRepository;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Factories
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters And Setters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Transactional( readOnly = true )
    @Override
    public StatisticsResponse getStatistics( StatisticsRequest request )
    {
        final List< StatisticsDto > statisticList = new ArrayList<>();
        final List< String > codes = request.getCodes();
        for( String code : codes )
        {
            final List< PathDto > paths = new ArrayList<>();
            final Map< String, Integer > entryStatistics = entryRepository
                    .findFirst( QEntryEntity.entryEntity.fakeAddress.eq( code ) )
                    .map( EntryEntity::getStatistics )
                    .orElse( new HashMap<>() );
            for( String path : entryStatistics.keySet() )
            {
                final Integer hits = entryStatistics.get( path );
                paths.add( new PathDto( path, hits ) );
            }
            final StatisticsDto statistics = new StatisticsDto();
            statistics.setCode( code );
            statistics.setPaths( paths );
            statistics.setAllHits( paths.stream().map( PathDto::getHits ).reduce( 0, Integer::sum ) );
            statisticList.add( statistics );
        }
        return new StatisticsResponse( statisticList );
    }

    @Transactional( readOnly = false )
    @Override
    public SwapResponse swap( SwapRequest request )
    {
        final Optional< EntryEntity > entry = entryRepository
                .findFirst( QEntryEntity.entryEntity.fakeAddress.eq( request.getCode() ) );
        if( entry.isPresent() )
        {
            EntryEntity entity = entry.get();
            entity.updatePath( request.getPath() );
            entity = entryRepository.save( entity );

            final String forwarding = entity.getRealAddress();
            return new SwapResponse( true, forwarding );
        }
        return new SwapResponse( false, null );
    }

    @Transactional( readOnly = false )
    @Override
    public CreateForwardingResponse createForwarding( CreateForwardingRequest request )
    {
        EntryEntity entity = EntryEntity.generateByRandom( request.getRealAddress() );
        entity = entryRepository.save( entity );
        return new CreateForwardingResponse( new ForwardingBundle( entity.getFakeAddress(), entity.getRealAddress() ) );
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Inner Classes And Patterns
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
