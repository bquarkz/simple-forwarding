package com.niltonrc.simplef.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niltonrc.simplef.contracts.IEntryService;
import com.niltonrc.simplef.messages.CreateForwardingRequest;
import com.niltonrc.simplef.messages.CreateForwardingResponse;
import com.niltonrc.simplef.messages.SwapRequest;
import com.niltonrc.simplef.messages.SwapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping( "/" )
public class ForwardingController
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
    @Autowired
    protected ForwardingController( IEntryService entryService )
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
    @RequestMapping(
            value = "/create",
            method = { RequestMethod.POST },
            consumes = "application/x-www-form-urlencoded",
            produces = "application/json" )
    @ResponseBody
    public String create( String address ) throws JsonProcessingException
    {
        final CreateForwardingResponse response = entryService.createForwarding( new CreateForwardingRequest( address ) );
        return mapper.writeValueAsString( response.getForwardingBundles() );
    }

    @RequestMapping( value = "/to/**", method = { RequestMethod.GET } )
    public void redirect( HttpServletRequest httpRequest, HttpServletResponse httpResponse ) throws IOException
    {
        final String uri = httpRequest.getRequestURI().replaceFirst( "/to", "" );
        final int index = uri.indexOf( "/", 1 );
        final String code = uri.substring( 1, index == -1 ? uri.length() : index );
        final String rawPath = uri.substring( 1 + code.length() );
        final String path = rawPath.equalsIgnoreCase( "/" ) || rawPath.isEmpty() ? "" : rawPath.substring( 1 );

        final SwapResponse response = entryService.swap( new SwapRequest( code, path ) );
        if( response.isOk() )
        {
            final String forwarding = response.getForwarding();
            if( rawPath.isEmpty() )
            {
                httpResponse.sendRedirect( forwarding );
            }
            else
            {
                httpResponse.sendRedirect( forwarding.endsWith( "/" )
                    ? forwarding + rawPath.substring( 1 )
                    : forwarding + rawPath );
            }
        }
        else
        {
            httpResponse.setStatus( 404 );
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Inner Classes And Patterns
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
