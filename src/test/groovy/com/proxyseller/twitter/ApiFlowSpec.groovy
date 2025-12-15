package com.proxyseller.twitter

import com.proxyseller.twitter.api.dto.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiFlowSpec extends Specification {

    @Autowired
    TestRestTemplate rest



    def "full user flow: register, login, post, like, comment, follow, feed"() {

        when: "register first user"
        def userAlice = new RegisterRequest(username: "alice", password: "password", displayName: "Alice")
        def regAliceResponse = rest.postForEntity("/api/auth/register", userAlice, AuthResponse)

        then:
        regAliceResponse.statusCode == HttpStatus.OK
        regAliceResponse.body.token
        regAliceResponse.body.userId
        String aliceId = regAliceResponse.body.userId

        println(regAliceResponse.body)

        when: "register second user"
        def userBob = new RegisterRequest(username: "bob", password: "password", displayName: "Bob")
        def regBobResponse = rest.postForEntity("/api/auth/register", userBob, AuthResponse)

        then:
        regBobResponse.statusCode == HttpStatus.OK
        String bobId = regBobResponse.body.userId
        String bobToken = regBobResponse.body.token

        println(regBobResponse.body)

        and: "alice creates a post"
        String aliceToken = regAliceResponse.body.token
        def headers = new HttpHeaders()
        headers.set("X-Auth-Token", aliceToken)
        headers.setContentType(MediaType.APPLICATION_JSON)

        def postReq = new CreatePostRequest(content: "Hello from Alice")
        def createPostResp = rest.exchange("/api/posts",
                HttpMethod.POST,
                new HttpEntity<>(postReq, headers),
                PostResponse)

        then:
        createPostResp.statusCode == HttpStatus.OK
        createPostResp.body.id
        String postId = createPostResp.body.id

        println(createPostResp.body)

        when: "bob follows alice"
        def bobHeaders = new HttpHeaders()
        bobHeaders.set("X-Auth-Token", bobToken)

        def followResp = rest.exchange(
                "/api/users/{id}/follow",
                HttpMethod.POST,
                new HttpEntity<>(null, bobHeaders),
                Map,
                aliceId
        )

        then:
        followResp.statusCode.is2xxSuccessful()

        println(followResp.body)

        when: "alice likes her own post"
        def likeResp = rest.exchange(
                "/api/posts/{id}/like",
                HttpMethod.POST,
                new HttpEntity<>(headers),
                PostResponse,
                postId
        )

        then:
        likeResp.statusCode == HttpStatus.OK
        likeResp.body.likesCount == 1


        println(likeResp.body)
    }
}
