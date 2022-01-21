package main;

import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import fileio.ActionInputData;
import fileio.ActorInputData;
import queries.ActorQuery;
import commands.Commands;
import fileio.Input;
import fileio.InputLoader;
import fileio.MovieInputData;
import queries.MovieQuery;
import recommendations.Recommendations;
import fileio.SerialInputData;
import queries.SerialQuery;
import fileio.UserInputData;
import queries.UserQuery;
import fileio.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        List<ActorInputData> actorsList = input.getActors();
        List<UserInputData> usersList = input.getUsers();
        List<ActionInputData> actionsList = input.getCommands();
        List<MovieInputData> moviesList = input.getMovies();
        List<SerialInputData> serialsList = input.getSerials();

        for (ActionInputData actionInputData : actionsList) {
            if (actionInputData.getActionType().equals(Constants.COMMAND)) {
                Commands command = new Commands();
                if (actionInputData.getType() != null) {
                    if (actionInputData.getType().equals("favorite")) {
                        command.favorite(usersList, actionInputData);
                        JSONObject jsonObject = fileWriter.writeFile(
                                actionInputData.getActionId(), "", command.getMessage());
                        arrayResult.add(jsonObject);
                    }

                    if (actionInputData.getType().equals(Constants.RATING)) {
                        command.rating(usersList, actionInputData, moviesList, serialsList);
                        JSONObject jsonObject = fileWriter.writeFile(
                                actionInputData.getActionId(), "", command.getMessage());
                        arrayResult.add(jsonObject);
                    }

                    if (actionInputData.getType().equals(Constants.VIEW)) {
                        command.view(usersList, actionInputData);
                        JSONObject jsonObject = fileWriter.writeFile(
                                actionInputData.getActionId(), "", command.getMessage());
                        arrayResult.add(jsonObject);
                    }
                }
            }

            if (actionInputData.getActionType().equals(Constants.QUERY)) {
                if (actionInputData.getObjectType().equals(Constants.ACTORS)) {
                    ActorQuery query = new ActorQuery();
                    if (actionInputData.getCriteria().equals(Constants.AVERAGE)) {
                        query.averageQ(actionInputData, actorsList, moviesList, serialsList);
                        JSONObject jsonObject = fileWriter.writeFile(
                                actionInputData.getActionId(), "", query.getMessage());
                        arrayResult.add(jsonObject);
                    }

                    if (actionInputData.getCriteria().equals(Constants.AWARDS)) {
                        query.awardsQ(actionInputData, actorsList);
                        JSONObject jsonObject = fileWriter.writeFile(
                                actionInputData.getActionId(), "", query.getMessage());
                        arrayResult.add(jsonObject);
                    }

                    if (actionInputData.getCriteria().equals(Constants.FILTER_DESCRIPTIONS)) {
                        query.filterQ(actionInputData, actorsList);
                        JSONObject jsonObject = fileWriter.writeFile(
                                actionInputData.getActionId(), "", query.getMessage());
                        arrayResult.add(jsonObject);
                    }
                }
                if (actionInputData.getObjectType().equals(Constants.MOVIES)) {
                    MovieQuery query = new MovieQuery();
                    if (actionInputData.getCriteria().equals(Constants.RATINGS)) {
                            query.ratingMoviesQ(actionInputData, moviesList);
                            JSONObject jsonObject = fileWriter.writeFile(
                                    actionInputData.getActionId(), "", query.getMessage());
                            arrayResult.add(jsonObject);

                    }

                    if (actionInputData.getCriteria().equals(Constants.FAVORITE)) {
                        query.favoriteMoviesQ(actionInputData, moviesList, usersList);
                        JSONObject jsonObject = fileWriter.writeFile(
                                actionInputData.getActionId(), "", query.getMessage());
                        arrayResult.add(jsonObject);
                    }

                    if (actionInputData.getCriteria().equals(Constants.LONGEST)) {
                        query.longestMovieQ(actionInputData, moviesList);
                        JSONObject jsonObject = fileWriter.writeFile(
                                actionInputData.getActionId(), "", query.getMessage());
                        arrayResult.add(jsonObject);
                    }

                    if (actionInputData.getCriteria().equals(Constants.MOST_VIEWED)) {
                        query.mostViewedMovieQ(actionInputData, moviesList, usersList);
                        JSONObject jsonObject = fileWriter.writeFile(
                                actionInputData.getActionId(), "", query.getMessage());
                        arrayResult.add(jsonObject);
                    }
                }

                if (actionInputData.getObjectType().equals(Constants.SHOWS)) {
                    SerialQuery query = new SerialQuery();
                    if (actionInputData.getCriteria().equals(Constants.RATINGS)) {
                        query.ratingSerialsQ(actionInputData, serialsList);
                        JSONObject jsonObject = fileWriter.writeFile(
                                actionInputData.getActionId(), "", query.getMessage());
                        arrayResult.add(jsonObject);

                    }

                    if (actionInputData.getCriteria().equals(Constants.FAVORITE)) {
                        query.favoriteSerialsQ(actionInputData, serialsList, usersList);
                        JSONObject jsonObject = fileWriter.writeFile(
                                actionInputData.getActionId(), "", query.getMessage());
                        arrayResult.add(jsonObject);

                    }

                    if (actionInputData.getCriteria().equals(Constants.LONGEST)) {
                        query.longestSerialsQ(actionInputData, serialsList);
                        JSONObject jsonObject = fileWriter.writeFile(
                                actionInputData.getActionId(), "", query.getMessage());
                        arrayResult.add(jsonObject);
                    }

                    if (actionInputData.getCriteria().equals(Constants.MOST_VIEWED)) {
                        query.mostViewedSerialQ(actionInputData, serialsList, usersList);
                        JSONObject jsonObject = fileWriter.writeFile(
                                actionInputData.getActionId(), "", query.getMessage());
                        arrayResult.add(jsonObject);
                    }
                }

                if (actionInputData.getObjectType().equals(Constants.USERS)) {
                    UserQuery query = new UserQuery();
                    query.getActiveUsers(usersList, actionInputData);
                    JSONObject jsonObject = fileWriter.writeFile(
                            actionInputData.getActionId(), "", query.getMessage());
                    arrayResult.add(jsonObject);
                }
            }

            if (actionInputData.getActionType().equals(Constants.RECOMMENDATION)) {
                Recommendations recommendations = new Recommendations();
                if (actionInputData.getType().equals(Constants.STANDARD)) {
                    recommendations.standardRecommendation(usersList, actionInputData,
                            moviesList, serialsList);
                    JSONObject jsonObject = fileWriter.writeFile(
                            actionInputData.getActionId(), "", recommendations.getMessage());
                    arrayResult.add(jsonObject);
                }

                if (actionInputData.getType().equals(Constants.BEST_UNSEEN)) {
                    recommendations.bestUnseenRecommendation(usersList, actionInputData,
                            moviesList, serialsList);
                    JSONObject jsonObject = fileWriter.writeFile(
                            actionInputData.getActionId(), "", recommendations.getMessage());
                    arrayResult.add(jsonObject);
                }

                if (actionInputData.getType().equals(Constants.POPULAR)) {
                    recommendations.popularRecommendation(usersList, actionInputData,
                            moviesList, serialsList);
                    JSONObject jsonObject = fileWriter.writeFile(
                            actionInputData.getActionId(), "", recommendations.getMessage());
                    arrayResult.add(jsonObject);
                }

                if (actionInputData.getType().equals(Constants.FAVORITE)) {
                    recommendations.favoriteRecommendation(usersList, actionInputData,
                            moviesList, serialsList);
                    JSONObject jsonObject = fileWriter.writeFile(
                            actionInputData.getActionId(), "", recommendations.getMessage());
                    arrayResult.add(jsonObject);
                }

                if (actionInputData.getType().equals(Constants.SEARCH)) {
                    recommendations.searchRecommendation(usersList, actionInputData,
                            moviesList, serialsList);
                    JSONObject jsonObject = fileWriter.writeFile(
                            actionInputData.getActionId(), "", recommendations.getMessage());
                    arrayResult.add(jsonObject);
                }
            }
        }

        fileWriter.closeJSON(arrayResult);
    }
}
