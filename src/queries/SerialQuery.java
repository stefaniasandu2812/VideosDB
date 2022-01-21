package queries;

import fileio.ActionInputData;
import fileio.SerialInputData;
import sort.SortingClass;
import fileio.UserInputData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SerialQuery {
    private String message;

    public final String getMessage() {
        return message;
    }

    /**
     * The method computes the serials searched by rating
     * @param action the action that needs to be done
     * @param serialsList the list of serials from database
     */
    public void ratingSerialsQ(final ActionInputData action,
                               final List<SerialInputData> serialsList) {
        Map<String, Double> searchedSerials = new HashMap<>();

        List<SerialInputData> list2 = verifyFilters(action, serialsList);

        for (SerialInputData serial : list2) {
            if (serial.average() != 0) {
                searchedSerials.put(serial.getTitle(), serial.average());
            }
        }

        message = SortingClass.sorting(action, searchedSerials);
    }

    /**
     * The method returns the searched query for serials
     * that appear in the favorite list of users
     * @param action the action that needs to be done
     * @param serialsList the list of serials from database
     * @param usersList the list of users
     */
    public void favoriteSerialsQ(final ActionInputData action,
                                 final List<SerialInputData> serialsList,
                                 final List<UserInputData> usersList) {
        Map<String, Double> favSerials = new HashMap<>();

        List<SerialInputData> list2 = verifyFilters(action, serialsList);

        for (UserInputData user : usersList) {
            for (String fav : user.getFavoriteMovies()) {
                for (SerialInputData serial : list2) {
                    if (serial.getTitle().equals(fav)) {
                        if (favSerials.get(serial.getTitle()) != null) {
                            double count = favSerials.get(serial.getTitle());
                            favSerials.put(serial.getTitle(),
                                    count + user.getHistory().get(serial.getTitle()));
                         } else {
                            favSerials.put(serial.getTitle(), 1.0);
                        }
                    }
                }
            }
        }

        message = SortingClass.sorting(action, favSerials);
    }

    /**
     * Method to get the longest serials
     * @param actionInputData the action that needs to be done
     * @param serialsList the list of serials from database
     */
    public void longestSerialsQ(final ActionInputData actionInputData,
                                final List<SerialInputData> serialsList) {
        Map<String, Double> longestSerials = new HashMap<>();

        List<SerialInputData> list = verifyFilters(actionInputData, serialsList);

        for (SerialInputData serial : list) {
            longestSerials.put(serial.getTitle(), (double) serial.serialDuration());
        }

        message = SortingClass.sorting(actionInputData, longestSerials);
    }

    /**
     * Method to get the most viewed serials
     * @param actionInputData the action that needs to be done
     * @param serialList the list of serials from database
     * @param usersList the list of users
     */
    public void mostViewedSerialQ(final ActionInputData actionInputData,
                                  final List<SerialInputData> serialList,
                                  final List<UserInputData> usersList) {
        Map<String, Double> viewedSerials = new HashMap<>();

        List<SerialInputData> list = verifyFilters(actionInputData, serialList);

        for (SerialInputData serial : list) {
            for (UserInputData user : usersList) {
                if (user.getHistory().containsKey(serial.getTitle())) {
                    if (viewedSerials.containsKey(serial.getTitle())) {
                        double count = viewedSerials.get(serial.getTitle());
                        viewedSerials.put(serial.getTitle(),
                                count + user.getHistory().get(serial.getTitle()));
                     } else {
                        viewedSerials.put(serial.getTitle(),
                                (double) user.getHistory().get(serial.getTitle()));
                    }
                }
            }
        }

        message = SortingClass.sorting(actionInputData, viewedSerials);
    }

    /**
     * The method filters the serials if the the year or genre exists
     * @param actionInputData the action that needs to be done
     * @param serialsList the list of serials from database
     * @return a list of serials filtered by year and genre
     */
    public List<SerialInputData> verifyFilters(final ActionInputData actionInputData,
                                              final List<SerialInputData> serialsList) {
        List<SerialInputData> list1;
        List<SerialInputData> list2;
        if (actionInputData.getFilters().get(0).get(0) != null) {
            if (!actionInputData.getFilters().get(0).get(0).equals("null")) {
                list1 = serialsList.stream()
                        .filter(s -> s.getYear() == Integer
                                .parseInt(actionInputData.getFilters().get(0).get(0)))
                        .collect(Collectors.toList());

             } else {
                list1 = serialsList;
            }
         } else {
            list1 = serialsList;
        }

        if (actionInputData.getFilters().get(1).get(0) != null) {
            if (!actionInputData.getFilters().get(1).get(0).equals("null")) {
                list2 = list1.stream()
                        .filter(s -> s.getGenres()
                                .contains(actionInputData.getFilters().get(1).get(0)))
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
