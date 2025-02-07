## Things You Should Probably Know (Before Judging This Project)

This section is your survival guide to understanding the... *unique* design choices made in this project.  Proceed with caution (and maybe a sense of humor).

### Response Messages: The `ResponseMessage` Saga

Inside the `dto` package, you'll find the `ResponseMessage` object.  This is how the frontend gets feedback.

```java
public class ResponseMessage {
    private String message;

    /**
     * A numeric code indicating the outcome of the operation.
     * <p>Possible values:</p>
     * <ul>
     *     <li><b>1:</b> Success</li>
     *     <li><b>-1:</b> Failure</li>
     * </ul>
     */
    private int code;
}
```

Basically, it's a simple container with a `code` (1 for "Yay!", -1 for "Oops!") and a `message` (for the user, so try to be nice).

To display these messages in your Thymeleaf templates, you need to add it to the model with the name `"message"`.  Don't forget!

Check out `common/message.html` for the magic:

```html
<div  th:fragment="message" th:if="${message}">
    <div th:classappend="${message.code > 0} ? 'alert alert-success' : 'alert alert-danger'"
         class="alert"
         role="alert">
        <span th:text="${message.message}"></span>
    </div>
</div>
```

This handy Thymeleaf fragment handles the display, changing the color based on whether the message is good news or bad news. You know, like a mood ring for your application.

### Custom Spring Security:  A Slightly... *Different* Approach

Here's where things get interesting (read: potentially questionable).  Let's talk about the `UserDetailsService`:

```java
@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        String[] roles = user.getRole().split(",");

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Stream.of(roles)
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getEmail();
            }
        };

    }
```

Yeah, I'm storing roles in a single comma-separated column in the database.  I know, I know, relational database purists are screaming.  But hey, it works (for now), and this is *my* personal project, so I get to make questionable decisions!  The code above then parses these roles.

### Getting the Current User:  It's All About the Email

Due to my aforementioned... *creative* security implementation, when you try to get the current user's name via `Principal`, you'll get their email address.

```java
@PostMapping
public void func(Principal principal){
    principal.getName(); // Returns the user's email!
}
```

So, be prepared to use `findByEmail` everywhere.  You've been warned.

### Likes:  A Composite Key Adventure

The `Like` entity uses a composite primary key because each user can only like/dislike a post once.  Here's how it's structured:

```java
public class Like {

    @EmbeddedId
    private LikeId id;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    /*
    * This attribute false if it is dislike
    * true if it is like
     */
    private Boolean isLike;
}
```

And the `LikeId` looks like this:

```java
public class LikeId implements Serializable {
    @Column(name = "post_id")
    private UUID postId;

    @Column(name = "user_id")
    private UUID userId;
}
```

So basically, `LikeId` contains the `postId` and `userId` of the `Like` relationship. The `isLike` is a boolean field that keeps whether user liked or disliked.

Hope that clears things up! Good luck trying to understand my code! ;)

