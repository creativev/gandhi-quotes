package me.creativei.gandhi;

import android.os.Parcel;
import android.os.Parcelable;

public class Quote implements Parcelable {
    public final int _id;
    public final String content;
    public final boolean favorite;

    public Quote(int _id, String quote, boolean favorite) {
        super();
        this._id = _id;
        this.content = quote;
        this.favorite = favorite;
    }

    public Quote(Parcel in) {
        _id = in.readInt();
        content = in.readString();
        favorite = in.readInt() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(content);
        dest.writeInt(favorite ? 1 : 0);
    }

    public static final Parcelable.Creator<Quote> CREATOR = new Parcelable.Creator<Quote>() {
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    public String image() {
        return "gandhi_art_" + (_id % 16);
    }
}
