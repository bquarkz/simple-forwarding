package com.niltonrc.simplef.services;

import com.niltonrc.simplef.contracts.IEntryRepository;
import com.niltonrc.simplef.entities.EntryEntity;
import com.niltonrc.simplef.messages.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EntryServiceTest
{
    @Mock
    private IEntryRepository entryRepository;
    @InjectMocks
    private EntryService entryService;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks( this );
    }

    @Test
    public void test_CreateForwarding()
    {
        final EntryEntity entity = EntryEntity.generateByRandom( "test" );
        when( entryRepository.save( any( EntryEntity.class ) ) ).thenReturn( entity );
        CreateForwardingResponse response = entryService.createForwarding( new CreateForwardingRequest( "real address" ) );
        assertNotNull( response );
        assertEquals( entity.getFakeAddress(), response.getForwardingBundles().getFakeAddress() );
        verify( entryRepository, times( 1 ) ).save( any( EntryEntity.class ) );
    }

    @Test
    public void test_Swap()
    {
        final EntryEntity entity = EntryEntity.generateByRandom( "test" );
        when( entryRepository.findFirst( any() ) ).thenReturn( Optional.of( entity ) );
        when( entryRepository.save( any( EntryEntity.class ) ) ).thenReturn( entity );
        SwapResponse response = entryService.swap( new SwapRequest( "code", "path" ) );
        assertNotNull( response );
        assertEquals( entity.getRealAddress(), response.getForwarding() );
    }

    @Test
    public void test_Statistics()
    {
        final EntryEntity entity = EntryEntity.generateByRandom( "test" );
        when( entryRepository.findFirst( any() ) ).thenReturn( Optional.of( entity ) );
        entity.getStatistics().put( "A", 5 );
        entity.getStatistics().put( "B", 10 );
        entity.getStatistics().put( "C", 15 );
        final StatisticsResponse response = entryService
                .getStatistics( new StatisticsRequest( Stream
                        .of( entity.getFakeAddress() )
                        .collect( Collectors.toList() ) ) );
        assertNotNull( response );
        assertEquals( 1, response.getStatistics().size() );

        assertEquals( entity.getFakeAddress(), response.getStatistics().get( 0 ).getCode() );
        assertEquals( 30, (int)response.getStatistics().get( 0 ).getAllHits() );

        assertEquals( "A", response.getStatistics().get( 0 ).getPaths().get( 0 ).getPath() );
        assertEquals( 5, (int)response.getStatistics().get( 0 ).getPaths().get( 0 ).getHits() );

        assertEquals( "B", response.getStatistics().get( 0 ).getPaths().get( 1 ).getPath() );
        assertEquals( 10, (int)response.getStatistics().get( 0 ).getPaths().get( 1 ).getHits() );

        assertEquals( "C", response.getStatistics().get( 0 ).getPaths().get( 2 ).getPath() );
        assertEquals( 15, (int)response.getStatistics().get( 0 ).getPaths().get( 2 ).getHits() );
    }
}
