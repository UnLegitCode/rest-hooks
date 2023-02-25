import ru.unlegit.resthooks.HttpMethod;
import ru.unlegit.resthooks.RestResponse;
import ru.unlegit.resthooks.annotation.*;

@RequestPath("127.0.0.1:1000/user")
public interface UserServiceHook {

    @RequestMethod(HttpMethod.POST)
    RestResponse<User> register(@Param("name") String name);

    @RequestMethod(HttpMethod.GET)
    RestResponse<User> get(@Path String name);

    @RequestMethod(HttpMethod.PATCH)
    RestResponse<Void> save(@Body User user);
}