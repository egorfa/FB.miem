package com.hse_miem.fb_miem.fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hse_miem.fb_miem.AllProductsActivity;
import com.hse_miem.fb_miem.ItemDecorationRemove;
import com.hse_miem.fb_miem.MainActivity;
import com.hse_miem.fb_miem.R;
import com.hse_miem.fb_miem.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.hse_miem.fb_miem.MainActivity.ALL_PRODUCTS_CODE;

/**
 * Created by Egor on 18/05/16.
 */
public class ProductFragment extends BaseFragment{

    private static final int PENDING_REMOVAL_TIMEOUT = 2000;
    private ArrayList<Pair<Product, Integer>> mProductsChoosen;
    private MenuItem doneMenuItem;

    @Bind(R.id.recycler_products)   RecyclerView recyclerViewChoosen;
    @Bind(R.id.fab)                         FloatingActionButton fab;
    @Bind(R.id.img_empty)                   ImageView imgEmpty;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        ButterKnife.bind(this, view);
        mProductsChoosen = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initializeRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mProductsChoosen.size() == 0) {
            imgEmpty.setVisibility(View.VISIBLE);
            Snackbar.make(getActivity().findViewById(R.id.clayout), getString(R.string.empty_message), Snackbar.LENGTH_SHORT).show();
        } else {
            imgEmpty.setVisibility(View.GONE);
        }


        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AllProductsActivity.class);
            getActivity().startActivityForResult(intent, ALL_PRODUCTS_CODE);
        });

        recyclerViewChoosen.setAdapter(new ProductAdapter());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list_products_choose, menu);
        doneMenuItem = menu.findItem(R.id.action_done);
        doneMenuItem.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_done:
                ((MainActivity)getActivity()).goToMap(mProductsChoosen);
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void initializeRecyclerView() {
        recyclerViewChoosen.setHasFixedSize(true);
        recyclerViewChoosen.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerViewChoosen.setLayoutManager(mLayoutManager);

        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();
    }

    public void updateArray(Product productNew) {
        doneMenuItem.setVisible(true);
        int positionYes = -1;
        for(int i = 0; i < mProductsChoosen.size(); i++) {
            if (mProductsChoosen.get(i).first.getId() == (productNew.getId()))
                positionYes = i;
        }
        if (positionYes > -1) {
            mProductsChoosen.set(positionYes, Pair.create(mProductsChoosen.get(positionYes).first, mProductsChoosen.get(positionYes).second+1));
            recyclerViewChoosen.getAdapter().notifyItemChanged(positionYes);
            recyclerViewChoosen.requestLayout();
        } else {
            mProductsChoosen.add(Pair.create(productNew, 1));
            recyclerViewChoosen.getAdapter().notifyDataSetChanged();
            recyclerViewChoosen.requestLayout();
        }
    }

    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(getResources().getColor(R.color.white_back));
                xMark = ContextCompat.getDrawable(getActivity(), R.drawable.ic_clear_white_24dp);
                xMark.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                ProductAdapter productAdapter = (ProductAdapter)recyclerView.getAdapter();
                if (productAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                ProductAdapter adapter = (ProductAdapter)recyclerViewChoosen.getAdapter();
                adapter.pendingRemoval(swipedPosition);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerViewChoosen);
    }

    private void setUpAnimationDecoratorHelper() {
        recyclerViewChoosen.addItemDecoration(new ItemDecorationRemove());
    }


    private class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<Pair<Product, Integer>> itemsPendingRemoval;

        private Handler handler = new Handler(); // hanlder for running delayed runnables
        HashMap<Pair<Product, Integer>, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

        public ProductAdapter() {
            itemsPendingRemoval = new ArrayList<>();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (itemsPendingRemoval.contains(mProductsChoosen.get(position))) {
                View.OnClickListener listener = v -> {
                    Runnable pendingRemovalRunnable = pendingRunnables.get(mProductsChoosen.get(position));
                    pendingRunnables.remove(mProductsChoosen.get(position));
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(mProductsChoosen.get(position));
                    // this will rebind the row in "normal" state
                    notifyItemChanged(mProductsChoosen.indexOf(mProductsChoosen.get(position)));
                };
                ((ProductViewHolder) holder).deleteBind(mProductsChoosen.get(position), listener);
            } else
                ((ProductViewHolder) holder).normalBind(mProductsChoosen.get(position));
        }
        @Override
        public int getItemCount() {
            return mProductsChoosen.size();
        }

        public void pendingRemoval(int position) {
            final Pair item = mProductsChoosen.get(position);
            if (!itemsPendingRemoval.contains(item)) {
                itemsPendingRemoval.add(item);
                // this will redraw row in "undo" state
                notifyItemChanged(position);
                // let's create, store and post a runnable to remove the item
                Runnable pendingRemovalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        remove(mProductsChoosen.indexOf(item));
                    }
                };
                handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
                pendingRunnables.put(item, pendingRemovalRunnable);
            }
        }

        public void remove(int position) {
            Pair item = mProductsChoosen.get(position);
            if (itemsPendingRemoval.contains(item)) {
                itemsPendingRemoval.remove(item);
            }
            if (mProductsChoosen.contains(item)) {
                mProductsChoosen.remove(position);
                if (mProductsChoosen.size() == 0)
                    doneMenuItem.setVisible(false);
                notifyItemRemoved(position);
            }
        }

        public boolean isPendingRemoval(int position) {
            Pair item = mProductsChoosen.get(position);
            return itemsPendingRemoval.contains(item);
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.card_view)   CardView cardView;
        @Bind(R.id.text_name)   TextView name;
        @Bind(R.id.text_price)  TextView price;
        @Bind(R.id.text_num)    TextView numbers;
        @Bind(R.id.undo_button) Button undo;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void normalBind(Pair<Product, Integer> product) {
            itemView.setBackgroundColor(getResources().getColor(R.color.white_back));
            cardView.setVisibility(View.VISIBLE);
            undo.setVisibility(View.GONE);
            undo.setOnClickListener(null);


            name.setText(product.first.getName());
            String strNum = "x" + String.valueOf(product.second);
            numbers.setText(strNum);
            String strPrice = product.first.getPrice().toString() + "\u20BD";
            price.setText(strPrice);
        }

        public void deleteBind(Pair<Product, Integer> product, View.OnClickListener listener) {
            itemView.setBackgroundColor(getResources().getColor(R.color.white_back));
            cardView.setVisibility(View.INVISIBLE);
            undo.setVisibility(View.VISIBLE);
            undo.setOnClickListener(listener);

        }
//        viewHolder.itemView.setBackgroundColor(Color.RED);
//        viewHolder.titleTextView.setVisibility(View.GONE);
//        viewHolder.undoButton.setVisibility(View.VISIBLE);
//        viewHolder.undoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // user wants to undo the removal, let's cancel the pending task
//                Runnable pendingRemovalRunnable = pendingRunnables.get(item);
//                pendingRunnables.remove(item);
//                if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
//                itemsPendingRemoval.remove(item);
//                // this will rebind the row in "normal" state
//                notifyItemChanged(items.indexOf(item));
//            }
//        });
//    } else {
//        // we need to show the "normal" state
//        viewHolder.itemView.setBackgroundColor(Color.WHITE);
//        viewHolder.titleTextView.setVisibility(View.VISIBLE);
//        viewHolder.titleTextView.setText(item);
//        viewHolder.undoButton.setVisibility(View.GONE);
//        viewHolder.undoButton.setOnClickListener(null);
//    }
    }


}
