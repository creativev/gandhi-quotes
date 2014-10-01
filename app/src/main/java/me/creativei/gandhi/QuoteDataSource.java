package me.creativei.gandhi;

public interface QuoteDataSource {
    public Quote getQuote(int offset, boolean favorite);

    public void setFavorite(int id, boolean favorite);

    int count(boolean favorite);
}
