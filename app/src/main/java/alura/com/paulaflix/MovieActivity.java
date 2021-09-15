package alura.com.paulaflix;


import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import alura.com.paulaflix.model.Movie;

public class MovieActivity extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtDesc;
    private TextView txtCast;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        txtTitle = findViewById(R.id.text_view_title);
        txtDesc = findViewById(R.id.text_view_desc);
        txtCast = findViewById(R.id.text_view_cast);
        recyclerView = findViewById(R.id.recycler_view_similar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            getSupportActionBar().setTitle(null);
        }

        // pegando a referencia do drawable
        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.shadows);

        // verificando o null pointer exception
        if (drawable != null) {
            Drawable movieCover = ContextCompat.getDrawable(this, R.drawable.movie_4);
            drawable.setDrawableByLayerId(R.id.cover_drawable, movieCover);

            // acessa o image view e troca a imagem atribuida
            ((ImageView) findViewById(R.id.image_view_cover)).setImageDrawable(drawable);
        }

        txtTitle.setText("Batman Begins");
        txtDesc.setText(R.string.batman_desc);
        txtCast.setText(getString(R.string.cast, "Christian Bale" + ", Morgan Freeman" + ", Liam Neeson" + ", Katie Holmes" + ", Michael Caine" + ", Ken Watanabe" + ", Gary Oldman"));

        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Movie movie = new Movie();
            movies.add(movie);
        }

        recyclerView.setAdapter(new MovieAdapter(movies));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

    }

    //  gerencia as listas
    private static class MovieHolder extends RecyclerView.ViewHolder {

        final ImageView imageViewCover;

        public MovieHolder(@NonNull View itemView) {
            // pega a referência do layout específico
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.image_view_header);
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MainActivity.MovieHolder> {

        private final List<Movie> movies;

        private MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        @NonNull
        @Override
        public MainActivity.MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MainActivity.MovieHolder(getLayoutInflater().inflate(R.layout.movie_item_similar,
                    parent,
                    false));
        }

        @Override
        public void onBindViewHolder(@NonNull MainActivity.MovieHolder movieHolder, int position) {
            Movie movie = movies.get(position);

        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }
}