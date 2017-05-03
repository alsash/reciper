package com.alsash.reciper.ui.fragment;

import android.support.v4.app.Fragment;

public abstract class BaseRecipeListFragment extends Fragment {

/*    protected RecyclerView recyclerView;

    protected abstract void setupList(RecyclerView list);

    @Override
    public void showDetails(Recipe recipe) {
        RecipeBottomDialog bottomDialog = RecipeBottomDialog.newInstance(recipe.getId());
        bottomDialog.show(getActivity().getSupportFragmentManager(), bottomDialog.getTag());
    }

    @Override
    public void showRecipe(Recipe recipe) {
        // RecipeDetailActivity.start(getContext(), recipe.getId());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.view_list, container, false);
        bindViews(layout);
        setupList(recyclerView);
        return layout;
    }

    private void bindViews(View layout) {
        recyclerView = (RecyclerView) layout.findViewById(R.id.list);
    }*/
}
