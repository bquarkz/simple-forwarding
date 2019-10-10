package com.niltonrc.simplef.controllers;

import com.niltonrc.simplef.configuration.WebMvcConfiguration;
import com.niltonrc.simplef.contracts.IEntryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith( SpringRunner.class )
@ContextConfiguration( classes = { WebMvcConfiguration.class } )
@WebMvcTest( ApiController.class )
public class ApiControllerTest
{
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IEntryService entryService;
    @MockBean
    private ApiController apiController;

    @Test
    public void test_anyRandomAddress() throws Exception
    {
        mockMvc.perform( get( "/api/anyRandomStuff" ) )
               .andExpect( status().isNotFound() );
    }

    @Test
    public void test_statisticsWithoutArguments() throws Exception
    {
        mockMvc.perform( get( "/api/statistics" ) )
               .andExpect( status().isBadRequest() );
    }

    @Test
    public void test_statisticsWithWrongArguments() throws Exception
    {
        mockMvc.perform( get( "/api/statistics?OLASDASDASD=asdasdasd" ) )
               .andExpect( status().isBadRequest() );
    }

    @Test
    public void test_statisticsWithRightArguments() throws Exception
    {
        mockMvc.perform( get( "/api/statistics?code=ALALALA&code=OOOSSSS" )
               .accept( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
               .andExpect( status().isOk() );
    }

    @Test
    public void test_statisticsAsPost() throws Exception
    {
        mockMvc.perform( post( "/api/statistics" ) )
               .andExpect( status().isMethodNotAllowed() );
    }

    @Test
    public void test_createWithoutContent() throws Exception
    {
        mockMvc.perform( post( "/api/create" )
               .contentType( MediaType.APPLICATION_JSON_UTF8_VALUE )
               .accept( MediaType.APPLICATION_JSON_UTF8_VALUE )
               .characterEncoding( "UTF-8" ) )
               .andExpect( status().isBadRequest() );
    }

    @Test
    public void test_createWithContent() throws Exception
    {
        mockMvc.perform( post( "/api/create" )
               .content( "LALALA" )
               .contentType( MediaType.APPLICATION_JSON_UTF8_VALUE )
               .accept( MediaType.APPLICATION_JSON_UTF8_VALUE )
               .characterEncoding( "UTF-8" ) )
               .andExpect( status().isOk() );
    }

    @Test
    public void test_createAsGet() throws Exception
    {
        mockMvc.perform( get( "/api/create" ) )
               .andExpect( status().isMethodNotAllowed() );
    }

}
