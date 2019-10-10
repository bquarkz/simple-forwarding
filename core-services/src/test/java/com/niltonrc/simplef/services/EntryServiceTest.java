package com.niltonrc.simplef.services;

import com.niltonrc.simplef.contracts.IEntryRepository;
import com.niltonrc.simplef.entities.EntryEntity;
import com.niltonrc.simplef.messages.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

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
        Mockito.when( entryRepository.save( any( EntryEntity.class ) ) ).thenReturn( entity );
        CreateForwardingResponse response = entryService.createForwarding( new CreateForwardingRequest( "real address" ) );
        Assert.assertNotNull( response );
        Assert.assertEquals( entity.getFakeAddress(), response.getForwardingBundles().getFakeAddress() );
        Mockito.verify( entryRepository.save( any( EntryEntity.class ) ), Mockito.times( 1 ) );
    }

    @Test
    public void test_Swap()
    {
        final EntryEntity entity = EntryEntity.generateByRandom( "test" );
        Mockito.when( entryRepository.findFirst( any() ) ).thenReturn( Optional.of( entity ) );
        Mockito.when( entryRepository.save( any( EntryEntity.class ) ) ).thenReturn( entity );
        SwapResponse response = entryService.swap( new SwapRequest( "code", "path" ) );
        Assert.assertNotNull( response );
        Assert.assertEquals( entity.getRealAddress(), response.getForwarding() );
    }

    @Test
    public void test_Statistics()
    {
        final EntryEntity entity = EntryEntity.generateByRandom( "test" );
        Mockito.when( entryRepository.findFirst( any() ) ).thenReturn( Optional.of( entity ) );
        entity.getStatistics().put( "A", 5 );
        entity.getStatistics().put( "B", 10 );
        entity.getStatistics().put( "C", 15 );
        StatisticsResponse response = entryService.getStatistics( new StatisticsRequest( Stream
                .of( entity.getFakeAddress() )
                .collect( Collectors.toList() ) ) );
        Assert.assertNotNull( response );

        Assert.assertEquals( 1, response.getStatistics().size() );

        Assert.assertEquals( entity.getFakeAddress(), response.getStatistics().get( 0 ).getCode() );
        Assert.assertEquals( 30, (int)response.getStatistics().get( 0 ).getAllHits() );

        Assert.assertEquals( "A", response.getStatistics().get( 0 ).getPaths().get( 0 ).getPath() );
        Assert.assertEquals( 5, (int)response.getStatistics().get( 0 ).getPaths().get( 0 ).getHits() );

        Assert.assertEquals( "B", response.getStatistics().get( 0 ).getPaths().get( 1 ).getPath() );
        Assert.assertEquals( 10, (int)response.getStatistics().get( 0 ).getPaths().get( 1 ).getHits() );

        Assert.assertEquals( "C", response.getStatistics().get( 0 ).getPaths().get( 2 ).getPath() );
        Assert.assertEquals( 15, (int)response.getStatistics().get( 0 ).getPaths().get( 2 ).getHits() );
    }
}
