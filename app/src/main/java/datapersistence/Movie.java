package datapersistence;

import androidx.room.Entity;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "movie_table")
public class Movie implements Serializable {


    private Integer voteCount;
    @PrimaryKey
    @OnConflictStrategy
    private Integer id;

    private Boolean video;

    private float voteAverage;


    private String title;

    private Double popularity;

    private String posterPath;

    private String originalLanguage;

    private String originalTitle;

    private String backdropPath;

    private Boolean adult;

    private String overview;

    private String releaseDate;
    private final static long serialVersionUID = 4244349405580365208L;

    public Movie() {
    }

    public Movie(String posterPath, String movieTitle, String movieOverview, String movieReleaseDate, String movieBackdropPath, int movieID, float movieRating) {
        this.posterPath = posterPath;
        this.title = movieTitle;
        this.overview = movieOverview;
        this.releaseDate = movieReleaseDate;
        this.backdropPath = movieBackdropPath;
        this.id = movieID;
        this.voteAverage = movieRating;
    }


    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }


    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

}
