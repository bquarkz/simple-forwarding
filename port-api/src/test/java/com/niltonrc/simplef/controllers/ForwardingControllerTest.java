package com.niltonrc.simplef.controllers;

import com.niltonrc.simplef.configuration.WebMvcConfiguration;
import com.niltonrc.simplef.contracts.IEntryService;
import com.niltonrc.simplef.messages.SwapResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith( SpringRunner.class )
@ContextConfiguration( classes = { WebMvcConfiguration.class } )
@WebMvcTest( ForwardingController.class )
public class ForwardingControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEntryService entryService;

    @Test
    public void test() throws Exception
    {
        Mockito.when( entryService.swap( any() ) ).thenReturn( new SwapResponse( true, "http://cool/" ) );
        mockMvc.perform( get( "/to/XYZ" ) ).andExpect( status().isOk() );
    }
}
