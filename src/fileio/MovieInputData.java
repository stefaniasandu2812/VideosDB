package fileio;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a movie, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class MovieInputData extends ShowInput {
    /**
     * Duration in minutes of a season
     */
    private final int duration;

    /**
     * All the ratings received by a movie
     */
    private final List<Double> ratings = new ArrayList<>();

    public MovieInputData(final String title, final ArrayList<String> cast,
                          final ArrayList<String> genres, final int year,
                          final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    @Override
    public String toString() {
        return "MovieInputData{" + "title= "
                + super.getTitle() + "year= "
                + super.getYear() + "duration= "
                + duration + "cast {"
                + super.getCast() + " }\n"
                + "genres {" + super.getGenres() + " }\n ";
    }

    /**
     * The method implements the abstract one
     * from the ShowInput class for movies
     * @return the rating of a movie
     */
    public double average() {
        double sum = 0.0;

        if (getRatings().size() == 0) {
            return 0.0;
        }

        for (int i = 0; i < getRatings().size(); ++i) {
            sum += getRatings().get(i);
        }

        return sum / getRatings().size();
    }

}
