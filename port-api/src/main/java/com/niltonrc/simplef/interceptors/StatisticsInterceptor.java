package com.niltonrc.simplef.interceptors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niltonrc.simplef.contracts.IEntryService;
import com.niltonrc.simplef.messages.StatisticsRequest;
import com.niltonrc.simplef.messages.StatisticsResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsInterceptor extends HandlerInterceptorAdapter
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
    private final IEntryService entryService;
    private final ObjectMapper mapper;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public StatisticsInterceptor( IEntryService entryService )
    {
        this.entryService = entryService;
        this.mapper = new ObjectMapper();
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

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler ) throws Exception
    {
        if( request.getMethod().equalsIgnoreCase( "get" ) &&
            request.getRequestURI().startsWith( "/statistics" ) )
        {
            final List< String > codes = querySpliter( request.getQueryString() ).get( "code" );
            final String json = buildResponse( codes );
            response.setContentType( "application/json" );
            response.setCharacterEncoding( "UTF-8" );
            final PrintWriter output = response.getWriter();
            output.print( json );
            output.flush();
            return false;
        }
        else
        {
            return super.preHandle( request, response, handler );
        }
    }

    private String buildResponse( List< String > codes ) throws JsonProcessingException
    {
        final StatisticsResponse response = entryService.getStatistics( new StatisticsRequest( codes ) );
        return mapper.writeValueAsString( response.getStatistics() );
    }

    private Map< String, List< String > > querySpliter( String query )
    {
        final Map< String, List< String > > map = new HashMap<>();
        final String[] pairs = query.split( "&" );
        for( final String pair : pairs )
        {
            final String[] parts = pair.split( "=" );
            final String key = parts[ 0 ];
            final String value = parts[ 1 ];
            if( !map.containsKey( key ) )
            {
                map.put( key, new ArrayList<>() );
            }
            map.get( key ).add( value );
        }
        return map;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Inner Classes And Patterns
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
