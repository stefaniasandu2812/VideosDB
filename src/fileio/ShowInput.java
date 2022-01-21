package fileio;

import java.util.ArrayList;

/**
 * General information about show (video), retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public abstract class ShowInput {
    /**
     * Show's title
     */
    private final String title;
    /**
     * The year the show was released
     */
    private final int year;
    /**
     * Show casting
     */
    private final ArrayList<String> cast;
    /**
     * Show genres
     */
    private final ArrayList<String> genres;

    /**
     * The order number of the show in the database
     */
    private int orderNumber;

    /**
     * Number of views of a show
     */
    private int noViews = 0;

    /**
     * How many times is a show in
     * the favorite list of an user
     */
    private int favorite = 0;

    public ShowInput(final String title, final int year,
                     final ArrayList<String> cast, final ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
    }

    public final String getTitle() {
        return title;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }

    public final int getOrderNumber() {
        return orderNumber;
    }

    public final void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * This method computes the average
     * to find the rating of the show
     * @return the rating of a show
     */
    public abstract double average();

    public final int getNoViews() {
        return noViews;
    }

    /**
     *
     * @param noViews number of views of a show
     */
    public final void setNoViews(int noViews) {
        this.noViews += noViews;
    }

    public final int getFavorite() {
        return favorite;
    }

    /**
     * Sets how many times is a show in a user's favorite list
     */
    public final void setFavorite() {
        this.favorite += 1;
    }
}
