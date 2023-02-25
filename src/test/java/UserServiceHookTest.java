import com.google.gson.Gson;
import lombok.SneakyThrows;
import ru.unlegit.resthooks.HttpStatus;
import ru.unlegit.resthooks.RestResponse;
import ru.unlegit.resthooks.executor.OkHttpRequestExecutor;
import ru.unlegit.resthooks.executor.RequestExecutor;
import ru.unlegit.resthooks.json.GsonCodec;

public class UserServiceHookTest {

    @SneakyThrows
    public static void main(String[] args) {
        RequestExecutor requestExecutor = new OkHttpRequestExecutor(new GsonCodec(new Gson()));
        UserServiceHook userServiceHook = requestExecutor.implementHook(UserServiceHook.class);

        testRegister(userServiceHook);
        testGet(userServiceHook);
        testSave(userServiceHook);
    }

    private static void testRegister(UserServiceHook serviceHook) {
        RestResponse<User> response = serviceHook.register("UnLegit");
        HttpStatus status = response.getStatus();

        if (status == HttpStatus.OK) {
            System.out.println("Registered user: " + response.getBody());
        } else {
            System.err.println("Unable to register user, unexpected status " + status);
        }
    }

    private static void testGet(UserServiceHook serviceHook) {
        RestResponse<User> response = serviceHook.get("UnLegit");
        HttpStatus status = response.getStatus();

        if (status == HttpStatus.OK) {
            System.out.println("Found user " + response.getBody());
        } else if (status == HttpStatus.NOT_FOUND) {
            System.err.println("User not found");
        } else {
            System.err.println("Unable to get user, unexpected response status " + status);
        }
    }

    private static void testSave(UserServiceHook serviceHook) {
        RestResponse<User> getResponse = serviceHook.get("UnLegit");
        HttpStatus getStatus = getResponse.getStatus();

        if (getStatus == HttpStatus.OK) {
            System.out.println("Found user " + getResponse.getBody());
        } else if (getStatus == HttpStatus.NOT_FOUND) {
            System.err.println("User not found");
        } else {
            System.err.println("Unable to get user, unexpected response status " + getStatus);
        }

        User user = getResponse.getBody();

        user.setBalance(100);

        RestResponse<Void> response = serviceHook.save(user);

        if (response.getStatus() == HttpStatus.OK) {
            System.out.println("User successfully saved");
        } else {
            System.err.println("Unable to save user, unexpected response status " + response.getStatus());
        }
    }
}