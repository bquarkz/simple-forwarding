package com.niltonrc.simplef.interceptors;

import com.niltonrc.simplef.contracts.IEntryService;
import com.niltonrc.simplef.messages.SwapRequest;
import com.niltonrc.simplef.messages.SwapResponse;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ForwardingInterceptorTest
{
    @Test
    public void test_WhenItIsAnApiCalling() throws Exception
    {
        final IEntryService entryService = Mockito.mock( IEntryService.class );
        final HttpServletRequest httpRequest = Mockito.mock( HttpServletRequest.class );
        final HttpServletResponse httpResponse = Mockito.mock( HttpServletResponse.class );
        final ForwardingInterceptor interceptor = new ForwardingInterceptor( entryService );
        when( httpRequest.getRequestURI() ).thenReturn( "/api" );

        boolean result = interceptor.preHandle( httpRequest, httpResponse, null );
        assertTrue( result );
    }

    @Test
    public void test_WhenItIsNotAnApiCalling_AndSwapCouldHappen() throws Exception
    {
        final IEntryService entryService = Mockito.mock( IEntryService.class );
        final HttpServletRequest httpRequest = Mockito.mock( HttpServletRequest.class );
        final HttpServletResponse httpResponse = Mockito.mock( HttpServletResponse.class );
        final ForwardingInterceptor interceptor = new ForwardingInterceptor( entryService );
        when( httpRequest.getRequestURI() ).thenReturn( "/XXYYZZ" );
        when( entryService.swap( any( SwapRequest.class ) ) ).thenReturn( new SwapResponse( true, "/ZZYYXX" ) );

        boolean result = interceptor.preHandle( httpRequest, httpResponse, null );
        assertFalse( result );
        verify( entryService, times( 1 ) ).swap( any( SwapRequest.class ) );
        verify( httpResponse, times( 1 ) ).sendRedirect( contains( "/ZZYYXX" ) );
    }

    @Test
    public void test_WhenItIsNotAnApiCalling_AndSwapCouldHappen_WithMorePath() throws Exception
    {
        final IEntryService entryService = Mockito.mock( IEntryService.class );
        final HttpServletRequest httpRequest = Mockito.mock( HttpServletRequest.class );
        final HttpServletResponse httpResponse = Mockito.mock( HttpServletResponse.class );
        final ForwardingInterceptor interceptor = new ForwardingInterceptor( entryService );
        when( httpRequest.getRequestURI() ).thenReturn( "/XXYYZZ/MORE/PATH" );
        when( entryService.swap( any( SwapRequest.class ) ) ).thenReturn( new SwapResponse( true, "/ZZYYXX" ) );

        boolean result = interceptor.preHandle( httpRequest, httpResponse, null );
        assertFalse( result );
        verify( entryService, times( 1 ) ).swap( any( SwapRequest.class ) );
        verify( httpResponse, times( 1 ) ).sendRedirect( startsWith( "/ZZYYXX" ) );
        verify( httpResponse, times( 1 ) ).sendRedirect( endsWith( "/MORE/PATH" ) );
    }

    @Test
    public void test_WhenItIsNotAnApiCalling_AndSwapCouldNotHappen() throws Exception
    {
        final IEntryService entryService = Mockito.mock( IEntryService.class );
        final HttpServletRequest httpRequest = Mockito.mock( HttpServletRequest.class );
        final HttpServletResponse httpResponse = Mockito.mock( HttpServletResponse.class );
        final ForwardingInterceptor interceptor = new ForwardingInterceptor( entryService );
        when( httpRequest.getRequestURI() ).thenReturn( "/XXYYZZ" );
        when( entryService.swap( any( SwapRequest.class ) ) ).thenReturn( new SwapResponse( false, null ) );

        boolean result = interceptor.preHandle( httpRequest, httpResponse, null );
        assertFalse( result );
        verify( entryService, times( 1 ) ).swap( any( SwapRequest.class ) );
        verify( httpResponse, never() ).sendRedirect( anyString() );
        verify( httpResponse, times( 1 ) ).setStatus( eq( 404 ) );
    }

}
