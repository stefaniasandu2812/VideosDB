package recommendations;

import common.Constants;
import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.ShowInput;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Recommendations {
    private String message;

    public final String getMessage() {
        return message;
    }

    /**
     * Recommending the first unseen show for an user
     * @param usersList list of users from database
     * @param action the action that needs to be done
     * @param moviesList list of movies from database
     * @param serialsList list of serials from database
     */
    public void standardRecommendation(final List<UserInputData> usersList,
                                       final ActionInputData action,
                                       final List<MovieInputData> moviesList,
                                       final List<SerialInputData> serialsList) {
        String recommendedVideo = null;

        for (UserInputData user : usersList) {
            if (user.getUsername().equals(action.getUsername())) {
                for (MovieInputData movie : moviesList) {
                    if (!user.getHistory().containsKey(movie.getTitle())) {
                        recommendedVideo = movie.getTitle();
                        break;
                    }
                }

                if (recommendedVideo == null) {
                    for (SerialInputData serial : serialsList) {
                        if (!user.getHistory().containsKey(serial.getTitle())) {
                            recommendedVideo = serial.getTitle();
                            break;
                        }
                    }
                }
            }
        }

        if (recommendedVideo != null) {
            message = "StandardRecommendation result: " + recommendedVideo;
        } else {
            message = "StandardRecommendation cannot be applied!";
        }
    }

    /**
     * Recommending the first unseen show with the greatest rating
     * @param usersList list of users from database
     * @param action the action that needs to be done
     * @param moviesList list of movies from database
     * @param serialsList list of serials from database
     */
    public void bestUnseenRecommendation(final List<UserInputData> usersList,
                                         final ActionInputData action,
                                         final List<MovieInputData> moviesList,
                                         final List<SerialInputData> serialsList) {
        List<ShowInput> movies = new ArrayList<>();

        for (UserInputData user : usersList) {
            if (user.getUsername().equals(action.getUsername())) {
                int i = 0;
                for (MovieInputData movie : moviesList) {
                    if (!user.getHistory().containsKey(movie.getTitle())) {
                        movie.setOrderNumber(i);
                        movies.add(movie);
                    }
                    ++i;
                }
                for (SerialInputData serial : serialsList) {
                    if (!user.getHistory().containsKey(serial.getTitle())) {
                        serial.setOrderNumber(i);
                        movies.add(serial);
                    }
                    ++i;
                }

                movies.sort(Comparator.comparing(ShowInput::average).reversed().
                        thenComparing(ShowInput::getOrderNumber));
            }

        }

        if (movies.size() != 0) {
            message = "BestRatedUnseenRecommendation result: " + movies.get(0).getTitle();
         } else {
            message = "BestRatedUnseenRecommendation cannot be applied!";
        }
    }

    /**
     * Recommending the most popular unseen show based on
     * the number of views
     * @param usersList list of users from database
     * @param action the action that needs to be done
     * @param moviesList list of movies from database
     * @param serialsList list of serials from database
     */
    public void popularRecommendation(final List<UserInputData> usersList,
                                     final ActionInputData action,
                                     final List<MovieInputData> moviesList,
                                     final List<SerialInputData> serialsList) {
        String recommendedShow = null;
        Map<String, Integer> genreMap = new HashMap<>();
        List<String> showsWatchedByUser = new ArrayList<>();
        int ok = 0;

        for (UserInputData user : usersList) {
            if (user.getUsername().equals(action.getUsername())) {
                if (!user.getSubscriptionType().equals(Constants.PREMIUM)) {
                    ok = 1;
                    break;
                }
            }
            for (MovieInputData movie : moviesList) {
                if (user.getHistory().containsKey(movie.getTitle())) {
                    movie.setNoViews(user.getHistory().get(movie.getTitle()));
                    for (String genre : movie.getGenres()) {
                        if (genreMap.containsKey(genre)) {
                            genreMap.put(genre, genreMap.get(genre) + movie.getNoViews());
                         } else {
                            genreMap.put(genre, movie.getNoViews());
                        }
                    }
                }
            }

            for (SerialInputData serial : serialsList) {
                if (user.getHistory().containsKey(serial.getTitle())) {
                    serial.setNoViews(user.getHistory().get(serial.getTitle()));
                    for (String genre : serial.getGenres()) {
                        if (genreMap.containsKey(genre)) {
                            int count = genreMap.get(genre);
                            genreMap.put(genre, count + serial.getNoViews());
                         } else {
                            genreMap.put(genre, serial.getNoViews());
                        }
                    }
                }
            }
        }

        if (ok == 0) {
            Map<String, Integer> sortedGenres = genreMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            for (UserInputData user : usersList) {
                if (user.getUsername().equals(action.getUsername())) {
                    showsWatchedByUser.addAll(user.getHistory().keySet());
                }
            }

            for (String genre : sortedGenres.keySet()) {
                for (MovieInputData movie : moviesList) {
                    if (!showsWatchedByUser.contains(movie.getTitle())) {
                        if (movie.getGenres().contains(genre)) {
                            recommendedShow = movie.getTitle();
                            break;
                        }
                    }
                }

                if (recommendedShow == null) {
                    for (SerialInputData serial : serialsList) {
                        if (!showsWatchedByUser.contains(serial.getTitle())) {
                            if (serial.getGenres().contains(genre)) {
                                recommendedShow = serial.getTitle();
                                break;
                            }
                        }
                    }
                }

                if (recommendedShow != null) {
                    break;
                }
            }
            if (recommendedShow != null) {
                message = "PopularRecommendation result: " + recommendedShow;
             } else {
                message = "PopularRecommendation cannot be applied!";
            }
         } else {
            message = "PopularRecommendation cannot be applied!";
        }
    }

    /**
     * Recommending the most favorite unseen show
     * @param usersList list of users from database
     * @param action the action that needs to be done
     * @param moviesList list of movies from database
     * @param serialsList list of serials from database
     */
    public void favoriteRecommendation(final List<UserInputData> usersList,
                                       final ActionInputData action,
                                       final List<MovieInputData> moviesList,
                                       final List<SerialInputData> serialsList) {
        List<ShowInput> favShows = new ArrayList<>();
        String recommendation = null;
        int ok = 0;

        for (UserInputData user : usersList) {
            if (user.getUsername().equals(action.getUsername())) {
                if (!user.getSubscriptionType().equals(Constants.PREMIUM)) {
                    ok = 1;
                    break;
                }
            }
            int i = 0;
            for (MovieInputData movie : moviesList) {
                if (user.getFavoriteMovies().contains(movie.getTitle())) {
                    if (favShows.contains(movie)) {
                        movie.setFavorite();
                     } else {
                        movie.setFavorite();
                        movie.setOrderNumber(i);
                        favShows.add(movie);
                    }
                }
                ++i;
            }

            for (SerialInputData serial : serialsList) {
                if (user.getFavoriteMovies().contains(serial.getTitle())) {
                    if (favShows.contains(serial)) {
                        serial.setFavorite();
                     } else {
                        serial.setOrderNumber(i);
                        serial.setFavorite();
                        favShows.add(serial);
                    }
                }
                ++i;
            }
        }

        favShows.sort(Comparator.comparing(ShowInput::getFavorite).reversed()
                .thenComparing(ShowInput::getOrderNumber));

        for (UserInputData user : usersList) {
            if (user.getUsername().equals(action.getUsername())) {
                for (ShowInput show : favShows) {
                    if (!user.getHistory().containsKey(show.getTitle())) {
                        recommendation = show.getTitle();
                        break;
                    }
                }
            }
        }

        if (ok == 0 && recommendation != null) {
            message = "FavoriteRecommendation result: " + recommendation;
         } else {
            message = "FavoriteRecommendation cannot be applied!";
        }
    }

    /**
     * Recommending an unseen show based on searched genre
     * @param usersList list of users from database
     * @param action the action that needs to be done
     * @param moviesList list of movies from database
     * @param serialsList list of serials from database
     */
    public void searchRecommendation(final List<UserInputData> usersList,
                                     final ActionInputData action,
                                     final List<MovieInputData> moviesList,
                                     final List<SerialInputData> serialsList) {
        List<ShowInput> shows = new ArrayList<>();

            for (MovieInputData movie : moviesList) {
                if (movie.getGenres().contains(action.getGenre())) {
                    shows.add(movie);
                }
            }

            for (SerialInputData serial : serialsList) {
                if (serial.getGenres().contains(action.getGenre())) {
                        shows.add(serial);
                    }
            }

        shows.sort(Comparator.comparing(ShowInput::average)
                .thenComparing(ShowInput::getTitle));

        List<String> recommendations = new ArrayList<>();
        for (UserInputData user : usersList) {
            if (user.getUsername().equals(action.getUsername())) {
                for (ShowInput show : shows) {
                    if (!user.getHistory().containsKey(show.getTitle())) {
                        recommendations.add(show.getTitle());
                    }
                }
            }
        }

        if (recommendations.size() != 0) {
            message = "SearchRecommendation result: " + recommendations;
         } else {
            message = "SearchRecommendation cannot be applied!";
        }
    }
}
