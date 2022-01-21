package queries;

import fileio.ActionInputData;
import fileio.MovieInputData;
import sort.SortingClass;
import fileio.UserInputData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MovieQuery {
    private String message;

    public final String getMessage() {
        return message;
    }

    /**
     * The method gets the movies searched by rating
     * @param action the action that needs to be done
     * @param moviesList the list of movies from database
     */
    public void ratingMoviesQ(final ActionInputData action,
                              final List<MovieInputData> moviesList) {
        Map<String, Double> moviesSearched = new HashMap<>();

        List<MovieInputData> list2 = verifyFilters(action, moviesList);

        for (MovieInputData movie : list2) {
            if (movie.average() != 0) {
                moviesSearched.put(movie.getTitle(), movie.average());
            }
        }

        message = SortingClass.sorting(action, moviesSearched);
    }

    /**
     * The method returns the searched query for movies
     * that appear in the favorite list of users
     * @param action the action that needs to be done
     * @param moviesList the list of movies from database
     * @param usersList the list of users from database
     */
    public void favoriteMoviesQ(final ActionInputData action,
                                final List<MovieInputData> moviesList,
                                final List<UserInputData> usersList) {
        Map<String, Double> favMovies = new HashMap<>();

        List<MovieInputData> list2 = verifyFilters(action, moviesList);

        for (UserInputData user : usersList) {
            for (String fav : user.getFavoriteMovies()) {
                for (MovieInputData movie : list2) {
                    if (movie.getTitle().equals(fav)) {
                        if (favMovies.get(movie.getTitle()) != null) {
                            double count = favMovies.get(movie.getTitle());
                            favMovies.put(movie.getTitle(), count + 1);
                         } else {
                            favMovies.put(movie.getTitle(), 1.0);
                        }
                    }
                }
            }
        }

        message = SortingClass.sorting(action, favMovies);
    }

    /**
     * Method to get the longest movies
     * @param action the action that needs to be done
     * @param moviesList the list of movies from database
     */
    public void longestMovieQ(final ActionInputData action,
                              final List<MovieInputData> moviesList) {
        Map<String, Double> longestMovies = new HashMap<>();

        List<MovieInputData> list = verifyFilters(action, moviesList);

        for (MovieInputData movie : list) {
            longestMovies.put(movie.getTitle(), (double) movie.getDuration());
        }

        message = SortingClass.sorting(action, longestMovies);

    }

    /**
     * Method to get the most viewed movies
     * @param action the action that needs to be done
     * @param moviesList the list of movies from database
     * @param usersList the list of users from database
     */
    public void mostViewedMovieQ(final ActionInputData action,
                                 final List<MovieInputData> moviesList,
                                 final List<UserInputData> usersList) {
        Map<String, Double> viewedMovies = new HashMap<>();

        List<MovieInputData> list = verifyFilters(action, moviesList);


        for (MovieInputData movie : list) {
            for (UserInputData user : usersList) {
                if (user.getHistory().containsKey(movie.getTitle())) {
                    if (viewedMovies.containsKey(movie.getTitle())) {
                        double count = viewedMovies.get(movie.getTitle());
                        viewedMovies.put(movie.getTitle(),
                                count + user.getHistory().get(movie.getTitle()));
                     } else {
                        viewedMovies.put(movie.getTitle(),
                                (double) user.getHistory().get(movie.getTitle()));
                    }
                }
            }
        }

        message = SortingClass.sorting(action, viewedMovies);
    }

    /**
     * The method filters the movies if the the year or genre exists
     * @param action the action that needs to be done
     * @param moviesList the list of movies from database
     * @return a list of movies filtered by year and genre
     */
    public List<MovieInputData> verifyFilters(final ActionInputData action,
                                              final List<MovieInputData> moviesList) {
        List<MovieInputData> list1;
        List<MovieInputData> list2;
        if (action.getFilters().get(0).get(0) != null) {
            if (!action.getFilters().get(0).get(0).equals("null")) {
                list1 = moviesList.stream()
                        .filter(s -> s.getYear() == Integer
                                .parseInt(action.getFilters().get(0).get(0)))
                        .collect(Collectors.toList());

            } else {
                list1 = moviesList;
            }
        } else {
            list1 = moviesList;
        }

        if (action.getFilters().get(1).get(0) != null) {
            if (!action.getFilters().get(1).get(0).equals("null")) {
                list2 = list1.stream()
                        .filter(s -> s.getGenres().contains(action.
                                getFilters().get(1).get(0)))
                        .collect(Collectors.toList());
            } else {
                list2 = list1;
            }
        } else {
            list2 = list1;
        }

        return list2;
    }
}
