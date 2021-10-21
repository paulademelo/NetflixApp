package alura.com.paulaflix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import alura.com.paulaflix.model.Category;
import alura.com.paulaflix.model.Movie;
import alura.com.paulaflix.util.CategoryTask;
import alura.com.paulaflix.util.ImageDownloaderTask;


public class MainActivity extends AppCompatActivity implements CategoryTask.CategoryLoader {

    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_main);

        //criando de categorias
        List<Category> categories = new ArrayList<>();

        // gerencia os itens dinamicos e atribui para o viewHolder
        mainAdapter = new MainAdapter(categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL,
                false));
        recyclerView.setAdapter(mainAdapter);

        // intanciando o json
       CategoryTask categoryTask = new CategoryTask(this);
       categoryTask.setCategoryLoader(this);
               categoryTask.execute("https://tiagoaguiar.co/api/netflix/home");
    }

    @Override
    public void onResult(List<Category> categories) {
        mainAdapter.setCategories(categories);
        //todos os dados podem ser populados
        mainAdapter.notifyDataSetChanged();
    }

    //  gerencia as listas
    public static class MovieHolder extends RecyclerView.ViewHolder {

        final ImageView imageViewCover;

        public MovieHolder(@NonNull View itemView) {
            // pega a referência do layout específico
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.image_view_header);
        }
    }

    private static class CategoryHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        RecyclerView recyclerViewMovie;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            recyclerViewMovie = itemView.findViewById(R.id.recycler_view_movie);
        }
    }

    private class MainAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private List<Category> categories;

        private MainAdapter(List<Category> categories) {
            this.categories = categories;
        }

        @NonNull
        @Override
        // aqui é declarado qual layout será usado
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CategoryHolder(getLayoutInflater().inflate(R.layout.category_item, parent, false));
        }

        // método que devolve a posição que queremos
        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            Category category = categories.get(position);
            holder.textViewTitle.setText(category.getName());
            holder.recyclerViewMovie.setAdapter(new MovieAdapter(category.getMovies()));
            holder.recyclerViewMovie.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, false));
        }

        // responsável por identificar a quantidade de itens da coleção
        @Override
        public int getItemCount() {
            return categories.size();
        }

        void setCategories(List<Category> categories) {
            this.categories.clear();
            this.categories.addAll(categories);
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        private final List<Movie> movies;

        private MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(getLayoutInflater().inflate(R.layout.movie_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie movie = movies.get(position);
            new ImageDownloaderTask(holder.imageViewCover).execute(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }
}