package com.niltonrc.simplef.entities;

import org.junit.Assert;
import org.junit.Test;

public class EntryEntityTest
{
    @Test
    public void test_GenerateByRandom()
    {
        final String realAddress = "REAL_ADDRESS";
        final EntryEntity entity = EntryEntity.generateByRandom( realAddress );
        Assert.assertNotNull( entity );
        Assert.assertEquals( 0, entity.getStatistics().size() );
        Assert.assertEquals( realAddress, entity.getRealAddress() );
        Assert.assertEquals( 10, entity.getFakeAddress().length() );
    }

    @Test
    public void test_UpdatePath()
    {
        final String realAddress = "REAL_ADDRESS";
        final EntryEntity entity = EntryEntity.generateByRandom( realAddress );
        entity.getStatistics().put( "A", 101 );
        entity.updatePath( "A" );
        Assert.assertEquals( 102, (int)entity.getStatistics().get( "A" ) );
    }
}
