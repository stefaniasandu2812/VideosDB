package queries;

import fileio.ActionInputData;
import sort.SortingClass;
import fileio.UserInputData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserQuery {
    private String message;

    public final String getMessage() {
        return message;
    }

    /**
     * The method for user query returns the most active users
     * @param usersList the list of users from database
     * @param action the action that needs to be done
     */
    public void getActiveUsers(final List<UserInputData> usersList,
                               final ActionInputData action) {
        Map<String, Double> usersActivity = new HashMap<>();

        for (UserInputData user : usersList) {
            if (user.getRatedMovies() != null
                    && user.getRatedMovies().size() != 0) {
                usersActivity.put(user.getUsername(),
                        (double) user.getRatedMovies().size());
            }

            if (user.getRatedSerials() != null
                    && user.getRatedSerials().size() != 0) {
                if (usersActivity.containsKey(user.getUsername())) {
                    double count = usersActivity.get(user.getUsername());
                    usersActivity.put(user.getUsername(),
                            count + user.getRatedSerials().size());
                 } else {
                    usersActivity.put(user.getUsername(),
                        (double) user.getRatedSerials().size());
                }
            }
        }

        message = SortingClass.sorting(action, usersActivity);
    }
}
