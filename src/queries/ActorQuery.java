package queries;

import actor.ActorsAwards;
import common.Constants;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import sort.SortingClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActorQuery {
    private String message;

    public final String getMessage() {
        return message;
    }

    /**
     * Getting the searched actors sorted by ratings
     * @param action the action that needs to be done
     * @param actorsList the list of actors from database
     * @param moviesList the list of movies from database
     * @param serialsList the list of serials from database
     */
    public void averageQ(final ActionInputData action,
                         final List<ActorInputData> actorsList,
                         final List<MovieInputData> moviesList,
                         final List<SerialInputData> serialsList) {
        Map<String, Double> actors = new HashMap<>();

        for (ActorInputData actor : actorsList) {
            if (actor.getFilmography().size() != 0) {
                for (String film : actor.getFilmography()) {
                    for (MovieInputData movie : moviesList) {
                        if (film.equals(movie.getTitle())
                                && movie.average() != 0) {
                            actor.getActorsAverage().put(movie.getTitle(), movie.average());
                        }
                    }
                }

                for (String show : actor.getFilmography()) {
                    for (SerialInputData serial : serialsList) {
                        if (show.equals(serial.getTitle())
                                && serial.average() != 0) {
                            actor.getActorsAverage().put(serial.getTitle(), serial.average());
                        }
                    }
                }
            }
        }


        for (ActorInputData actor : actorsList) {
            if (actor.getActorsAverage().size() != 0 && actor.average() != 0) {
                actors.put(actor.getName(), actor.average());
            }
        }

        message = SortingClass.sorting(action, actors);
    }

    /**
     * The method provides the actors searched by awards
     * @param action the action that needs to be done
     * @param actorsList the list of actors from database
     */
    public void awardsQ(final ActionInputData action, final List<ActorInputData> actorsList) {
        List<String> awards = action.getFilters()
                .get(action.getFilters().size() - 1);
        HashMap<String, Double> searchedActors = new HashMap<>();

        for (ActorInputData actorInputData : actorsList) {
            /* var used to check if the actor received the award */
            int ok = 0;
            for (String award : awards) {
                    if (actorInputData.getAwards().containsKey(ActorsAwards.valueOf(award))) {
                        ok = 1;
                    } else {
                        ok = 0;
                        break;
                    }
            }

            if (ok == 1) {
                if (actorInputData.totalAwards() != 0) {
                    searchedActors.put(actorInputData.getName(),
                            actorInputData.totalAwards().doubleValue());
                }
            }
        }

        message = SortingClass.sorting(action, searchedActors);
    }

    /**
     * Filters the actors by words appearing in their description
     * @param action the action that needs to be done
     * @param actorsList the list of actors from database
     */
    public void filterQ(final ActionInputData action,
                        final List<ActorInputData> actorsList) {
        List<String> words = action.getFilters().get(2);
        List<String> searchedActors = new ArrayList<>();

        for (ActorInputData actor : actorsList) {
            int ok = 0;
            for (String word : words) {
                /* case insensitive */
                String updatedDescription =
                        actor.getCareerDescription().replaceAll("[^a-zA-Z ]", " ");
                if (updatedDescription.toLowerCase().contains(" " + word + " ")) {
                    ok = 1;
                } else {
                    ok = 0;
                    break;
                }
            }

            if (ok == 1) {
                searchedActors.add(actor.getName());
            }
        }

        if (action.getSortType().equals(Constants.DESCENDING)) {
            searchedActors.sort(Collections.reverseOrder());
        } else {
            Collections.sort(searchedActors);
        }

        message = "Query result: " + searchedActors;
    }

}
