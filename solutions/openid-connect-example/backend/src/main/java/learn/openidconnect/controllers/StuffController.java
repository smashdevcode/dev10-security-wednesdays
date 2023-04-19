package learn.openidconnect.controllers;

import learn.openidconnect.models.AppUser;
import learn.openidconnect.models.Stuff;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/stuff")
public class StuffController {
    private static final List<Stuff> stuff = new ArrayList<>();

    @GetMapping
    public ResponseEntity<?> get(@AuthenticationPrincipal AppUser principal) {
        if (principal.getAppUserId() > 0) {
            List<Stuff> filteredStuff = stuff.stream()
                    .filter(s -> s.getAppUserId() == principal.getAppUserId())
                    .toList();
            return new ResponseEntity<>(filteredStuff, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping
    public ResponseEntity<?> post(@AuthenticationPrincipal AppUser principal, @RequestBody List<String> values) {
        if (principal.getAppUserId() > 0) {
            for (String value : values) {
                stuff.add(new Stuff(principal.getAppUserId(), value));
            }
            List<Stuff> filteredStuff = stuff.stream()
                    .filter(s -> s.getAppUserId() == principal.getAppUserId())
                    .toList();
            return new ResponseEntity<>(filteredStuff, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
