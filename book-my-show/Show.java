class Show {
    private String showId;
    private String movieName;
    private String language;
    private String genre;
    private int durationInMinutes;
    private ShowStatus status;

    public Show(String showId, String movieName, String language, String genre, int duration) {
        this.showId = showId;
        this.movieName = movieName;
        this.language = language;
        this.genre = genre;
        this.durationInMinutes = duration;
        this.status = ShowStatus.COMING_SOON;
    }

    public String getShowId() { return showId; }
    public String getMovieName() { return movieName; }
    public String getLanguage() { return language; }
    public String getGenre() { return genre; }
    public int getDurationInMinutes() { return durationInMinutes; }
    public ShowStatus getStatus() { return status; }
    public void setStatus(ShowStatus status) { this.status = status; }

    @Override
    public String toString() {
        return movieName + " (" + language + ", " + genre + ")";
    }
}
