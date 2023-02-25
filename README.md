# RestHooks
## Feedback: 
[Telegram](https://t.me/UnLegit)

Discord: UnLegit#6190

## Getting Started

#### Creating entity that will be parsed in JSON and from JSON

```java
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    final long id;
    final String name;
    double balance;
}
```

#### Creating service hook (you can use the entity as a return type, but I recommend using RestResponse, because you can use it to process the status returned by the server)

```java
@RequestPath("127.0.0.1:1000/user")
public interface UserServiceHook {

    @RequestMethod(HttpMethod.POST)
    RestResponse<User> register(@Param("name") String name);

    @RequestMethod(HttpMethod.GET)
    RestResponse<User> get(@Path String name);

    @RequestMethod(HttpMethod.PATCH)
    RestResponse<Void> save(@Body User user);
}
```

#### Creating HTTP request executor (you can create you own request executor and json codec implementations)

```java
RequestExecutor requestExecutor = new OkHttpRequestExecutor(new GsonCodec(new Gson()));
```

#### Implementing service hook

```java
UserServiceHook userServiceHook = requestExecutor.implementHook(UserServiceHook.class);
```