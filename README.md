SIMPLE FORWARDING
=================
a simple way to do forwarding

-----

We could split Simple Forwarding into:

> 1. ForwardingInterceptor: A filter to do forwarding for any request which doesn't fit with "api".    
> 
> 1. ApiController: Handle statistics and create apis
>   1. statistics: `/api/statistics` : GET - from curl:
>`curl -X GET http://localhost:8080/api/statistics?code=SOME_CODE`
> it will return a json which contains hits for every path and a sum 
>of all hits.
>
>   1. create: `/api/create` : POST - from curl: 
>`curl -d "https://www.amazon.com/" -H "Content-Type: application/json;charset=UTF-8" -X POST http://localhost:8080/api/create`:
> it will generate a json with a random fakeAddress (code).
>
> 1. one address are already created on database: ABCDEFGHIJ => https://www.neueda.com/

Tech-Stack
==========
* Java 8 at least
* QueryDSL -- not necessary but...
* Gradle 4.7 - should be this version otherwise some problems could happen to compile QueryDSL helper classes
* We will use spring boot, spring-data-jpa and spring-web
* Flyway to handle database migrations
* JUnit and Mockito for tests
* We are using H2 an in memory database (emulating PostgreSQL)

Build and Run
=============
To build project please run `gradle bootjar`.

A simple `gradle bootrun` will be enough to run and if you want to see logs on screen please do: 
`gradle bootrun -Pprofiles=stdout`, otherwise just on files into logs.

Thoughts
========
* we have to improve services assertions they are really plain
* we could provide some security for api using a simple Basic Authentication
* we could improve comments and some tips, javadocs and etc
* we could improve the way that we handle exceptions, it will make app more robust. I added something there but it is
 not good at all (i didn't have time to plan the exceptions as well), but we could wrap it on facades (services) and 
 throw it if needed.
 
Job Diary
=========
1. start project with git init and gradle init
1. creation of `.gitignore` (i just copy from another project)
1. build directories: `mkdir {core-domain,core-service,dbc-api,dbc-persistence,port-api,port-persistence}/src/{main,test}/{java,resources}`
1. spend some time planning how to achieve the forwarding
1. start to code
1. at some stage i changed my mind and put the forwarding as a filter
1. wrote some tests around web and others