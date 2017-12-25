package org.apache.skywalking.apm.testcase.resttemplate.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.skywalking.apm.testcase.resttemplate.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class TestRestController {

    private static final Map<Integer, User> users = new ConcurrentHashMap<>();

    @RequestMapping(value = "/rest/{id}", method = RequestMethod.GET, produces = "application/json")
    private ResponseEntity<User> getUser(@PathVariable("id") int id) {
        User currentUser = users.get(id);
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(currentUser);
    }

    @RequestMapping(value = "/rest/", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
        users.put(user.getId(), user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/rest/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody User user) {
        User currentUser = users.get(user.getId());
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }
        currentUser.setUserName(user.getUserName());
        return ResponseEntity.ok(currentUser);
    }

    @RequestMapping(value = "/rest/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(@PathVariable("id") int id) {
        User currentUser = users.get(id);
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }
        users.remove(id);
        return ResponseEntity.noContent().build();
    }
}
