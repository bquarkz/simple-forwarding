package com.niltonrc.simplef.entities;

import com.niltonrc.simplef.core.DomainEntity;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table( schema = "dbo", name = "ENTRY" )
public class EntryEntity extends DomainEntity< Integer >
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
    @Id
    @Column( name = "entry_id" )
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer entryId;

    @Column( name = "fake_address" )
    private String fakeAddress;

    @Column( name = "real_address" )
    private String realAddress;

    @ElementCollection
    @JoinTable( schema = "dbo", name = "STATISTICS", joinColumns = @JoinColumn( name = "statistics_entry_id" ) )
    @MapKeyColumn( name = "path" )
    @Column( name = "hits" )
    private Map< String, Integer > statistics = new HashMap<>();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected EntryEntity()
    {
    }

    public EntryEntity( Integer entryId )
    {
        this.entryId = entryId;
    }

    EntryEntity(
            Integer entryId,
            String fakeAddress,
            String realAddress )
    {
        this.entryId = entryId;
        this.fakeAddress = fakeAddress;
        this.realAddress = realAddress;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Factories
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static EntryEntity generateByRandom( final String realAddress )
    {
        final String fakeAddress = RandomStringUtils.randomAlphanumeric( 10 );
        return new EntryEntity( null, fakeAddress, realAddress );
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters And Setters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Integer getEntryId()
    {
        return entryId;
    }

    public void setEntryId( Integer entryId )
    {
        this.entryId = entryId;
    }

    public String getFakeAddress()
    {
        return fakeAddress;
    }

    public void setFakeAddress( String fakeAddress )
    {
        this.fakeAddress = fakeAddress;
    }

    public String getRealAddress()
    {
        return realAddress;
    }

    public void setRealAddress( String realAddress )
    {
        this.realAddress = realAddress;
    }

    public Map< String, Integer > getStatistics()
    {
        return statistics;
    }

    public void setStatistics( Map< String, Integer > statistics )
    {
        this.statistics = statistics;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Integer getId()
    {
        return getEntryId();
    }

    public void updatePath( String path )
    {
        final Integer hits = getStatistics().get( path );
        if( hits == null )
        {
            getStatistics().put( path, 1 );
        }
        else
        {
            getStatistics().put( path, hits + 1 );
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Inner Classes And Patterns
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
