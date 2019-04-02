package android.example.popularmovies.Data;

import java.util.Date;

public class Movie {

    private String Title;
    private String ReleaseDate;
    private String Rating;
    private String Synopsis;
    private String ThumbnailPath;

    public Movie(){
    }

    public Movie(String title, String releasedate, String rating, String synopsis, String thumbnailpath){
        Title = title;
        ReleaseDate = releasedate;
        Rating = rating;
        Synopsis = synopsis;
        ThumbnailPath = thumbnailpath;
    }

    public String getTitle() {
        return Title;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public String getRating() {
        return Rating;
    }

    public String getSynopsis() {
        return Synopsis;
    }

    public String getThumbnailPath() {
        return ThumbnailPath;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public void setSynopsis(String synopsis) {
        Synopsis = synopsis;
    }

    public void setThumbnail(String  thumbnailpath) {
        ThumbnailPath = thumbnailpath;
    }
}
