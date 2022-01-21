package fileio;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Information about an actor, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class ActorInputData {
    /**
     * actor name
     */
    private String name;
    /**
     * description of the actor's career
     */
    private String careerDescription;
    /**
     * videos starring actor
     */
    private ArrayList<String> filmography;
    /**
     * awards won by the actor
     */
    private Map<ActorsAwards, Integer> awards;

    /**
     * ratings of an actor based on movies and serials rating
     */
    private final Map<String, Double> actorsAverage = new HashMap<>();

    public ActorInputData(final String name, final String careerDescription,
                          final ArrayList<String> filmography,
                          final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public void setCareerDescription(final String careerDescription) {
        this.careerDescription = careerDescription;
    }

    @Override
    public String toString() {
        return "ActorInputData{"
                + "name='" + name + '\''
                + ", careerDescription='"
                + careerDescription + '\''
                + ", filmography=" + filmography + '}';
    }

    public Map<String, Double> getActorsAverage() {
        return actorsAverage;
    }

    /**
     * The method computes the rating of an actor
     * by summing the ratings of the movies and series
     * they played in
     * @return rating of an actor
     */
    public Double average() {
        Double sum = 0.0;
        for (Map.Entry<String, Double> show : actorsAverage.entrySet()) {
            sum += show.getValue();
        }

        return sum / actorsAverage.size();
    }

    /**
     * @return the total number of awards won by an actor
     */
    public Integer totalAwards() {
        Integer sum = 0;

        for (Integer noAwards : awards.values()) {
            sum += noAwards;
        }

        return sum;
    }
}
