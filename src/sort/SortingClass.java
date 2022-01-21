package sort;

import common.Constants;
import fileio.ActionInputData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SortingClass {

    private SortingClass() { }

    /**
     * This method sorts a map first by value
     * and then by key for alphabetical order,
     * using a customized comparator
     * @param map list of map from database
     * @param sortType the type of sorting
     * @return a sorted map
     */
    private static Map<String, Double> sort(final Map<String, Double> map,
                                            final String sortType) {
        Map<String, Double> sortedMap = new LinkedHashMap<>();

        ArrayList<Map.Entry<String, Double>> list = new ArrayList<>(map.entrySet());

        Comparator<Map.Entry<String, Double>> myComparator =
                (o1, o2) -> {
                    Double val1 = o1.getValue();
                    Double val2 = o2.getValue();

                    int var = val1.compareTo(val2);

                    if (var == 0) {
                        String key1 = o1.getKey();
                        String key2 = o2.getKey();

                        if (sortType.equals(Constants.DESCENDING)) {
                            return key2.compareTo(key1);
                        }

                        return key1.compareTo(key2);
                    }

                    if (sortType.equals(Constants.DESCENDING)) {
                        return val2.compareTo(val1);
                    }
                    return val1.compareTo(val2);
        };
        list.sort(myComparator);

            for (Map.Entry<String, Double> el : list) {
                sortedMap.put(el.getKey(), el.getValue());
            }

        return sortedMap;
    }

    /**
     * Returning the first N elements for a query
     * @param action the action that needs to be done
     * @param map the sorted map
     * @return the result of a query
     */
    public static String sorting(final ActionInputData action,
                                 final Map<String, Double> map) {
        Map<String, Double> sortedShows;
        sortedShows = SortingClass.sort(map, action.getSortType());

        List<String> searchedQuery = new ArrayList<>();

        /* in case the results are less than N we return all of them */
        int count = 0;

        if (action.getNumber() > sortedShows.size()) {
            count = action.getNumber() - map.size();
        }

        Iterator<String> itr = sortedShows.keySet().iterator();
        while (count < action.getNumber() && itr.hasNext()) {
            searchedQuery.add(itr.next());
            ++count;
        }

        return "Query result: " + searchedQuery;
    }
}
