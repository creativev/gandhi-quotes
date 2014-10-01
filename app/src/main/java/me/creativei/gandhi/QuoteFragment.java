package me.creativei.gandhi;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class QuoteFragment extends Fragment {

    public static final String QUOTE = "QUOTE";
    public static final String TOTAL_ROWS = "TOTAL_ROWS";
    private Quote quote;
    private int totalRows;
    private Typeface font;

    public QuoteFragment() {
    }

    public static QuoteFragment newInstance(Quote quote, int totalRows) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(QUOTE, quote);
        bundle.putInt(TOTAL_ROWS, totalRows);
        QuoteFragment fragment = new QuoteFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quote = getArguments().getParcelable(QUOTE);
        totalRows = getArguments().getInt(TOTAL_ROWS);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        font = ((MainActivity) activity).getQuoteFont();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (quote == null) {
            return inflater.inflate(R.layout.blank_quote, container, false);
        }

        View view = inflater.inflate(R.layout.fragment_quote, container, false);
        ((ImageView) view.findViewById(R.id.imageAuthor)).setImageResource(Utils.getResource(quote.image(), R.drawable.class));
        TextView quote = (TextView) view.findViewById(R.id.textQuote);
        quote.setTypeface(font);
        quote.setText(this.quote.content);
        ((TextView) view.findViewById(R.id.textCounter)).setText(String.format("%d of %d", this.quote._id, totalRows));
        return view;
    }
}
