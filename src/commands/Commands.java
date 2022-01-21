package commands;

import java.util.List;
import entertainment.Season;
import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;

public class Commands {
    /**
     * the message to be returned
     */
    private String message;

    public final String getMessage() {
        return message;
    }

    /**
     * This method adds a show to the list of favorite
     * of a given user
     * @param usersList the list of users from database
     * @param action the action that needs to be done
     */
    public void favorite(final List<UserInputData> usersList, final ActionInputData action) {
        for (UserInputData userInputData : usersList) {
            if (userInputData.getUsername().equals(action.getUsername())) {
                if (userInputData.getHistory().get(action.getTitle()) != null) {
                        if (userInputData.getFavoriteMovies().contains(action.getTitle())) {
                            message = "error -> " + action.getTitle()
                                    + " is already in favourite list";
                         } else {
                            userInputData.addFavorite(action.getTitle());
                            message = "success -> " + action.getTitle()
                                    + " was added as favourite";
                        }
                        break;
                 } else {
                    message = "error -> " + action.getTitle() + " is not seen";
                }
            }
        }
    }

    /**
     * Rating a show
     * @param usersList the list of users from database
     * @param action the action that needs to be done
     * @param moviesList list of movies from database
     * @param serialsList list of serials from database
     */

    public void rating(final List<UserInputData> usersList, final ActionInputData action,
                       final List<MovieInputData> moviesList,
                       final List<SerialInputData> serialsList) {
        for (UserInputData user : usersList) {
            if (user.getUsername().equals(action.getUsername())) {
                if (user.getHistory().get(action.getTitle()) != null) {
                    for (MovieInputData movie : moviesList) {
                        if (movie.getTitle().equals(action.getTitle())) {
                            if (user.getRatedMovies().get(movie.getTitle()) != null) {
                                message = "error -> " + movie.getTitle()
                                        + " has been already rated";
                                break;
                             } else {
                                movie.getRatings().add(action.getGrade());
                                user.getRatedMovies().put(movie.getTitle(),
                                        action.getGrade());
                                message = "success -> " + action.getTitle()
                                        + " was rated with " + action.getGrade()
                                        + " by " + user.getUsername();
                            }
                        }
                    }

                    for (SerialInputData serial : serialsList) {
                        if (serial.getTitle().equals(action.getTitle())) {
                            Season season = serial.getSeasons()
                                    .get(action.getSeasonNumber() - 1);
                            if (user.getRatedSerials().get(serial.getTitle()) == null
                                    || !season.getRatedByUsers().contains(user.getUsername())) {
                                season.getRatings().add(action.getGrade());
                                season.getRatedByUsers().add(user.getUsername());
                                user.getRatedSerials().put(serial.getTitle(),
                                        action.getGrade());
                                message = "success -> " + action.getTitle()
                                        + " was rated with " + action.getGrade()
                                        + " by " + user.getUsername();
                             } else {
                                message = "error -> " + serial.getTitle()
                                        + " has been already rated";
                                break;
                            }
                        }
                    }
                 } else {
                    message = "error -> " + action.getTitle() + " is not seen";
                }
            }
        }
    }

    /**
     * Marking a show as viewed by adding it to the history
     * list of a given user
     * @param usersList the list of users from database
     * @param action the action that needs to be done
     */
    public void view(final List<UserInputData> usersList, final ActionInputData action) {
        for (UserInputData user : usersList) {
            if (user.getUsername().equals(action.getUsername())) {
                if (user.getHistory().get(action.getTitle()) != null) {
                    int noViews = user.getHistory().get(action.getTitle());
                    user.getHistory().put(action.getTitle(), noViews + 1);
                 } else {
                    user.getHistory().put(action.getTitle(), 1);
                }
                message = "success -> " + action.getTitle()
                        + " was viewed with total views of "
                        + user.getHistory().get(action.getTitle());
            }
        }
    }
}
