package udacity.example.com.abfitness.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.example.com.abfitness.R;
import udacity.example.com.abfitness.utils.ColorUtils;
import udacity.example.com.abfitness.interfaces.OnAdapterClickHandler;

public class BaseListAdapter extends RecyclerView.Adapter<BaseListAdapter.BaseListAdapterViewHolder> {

    private static final String TAG = BaseListAdapter.class.getSimpleName();
    private final OnAdapterClickHandler mClickHandler;
    private ArrayList<String> mBaseList;
    private  int viewHolderCount = 0;

    public BaseListAdapter(OnAdapterClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public BaseListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.base_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        BaseListAdapterViewHolder viewHolder = new BaseListAdapterViewHolder(view);

        int backgroundColorForViewHolder =
                ColorUtils.getViewHolderBackgroundColorFromInstance(context, viewHolderCount);

        viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);
        viewHolderCount++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseListAdapterViewHolder holder, int i) {
        String item = mBaseList.get(i);
        holder.baseItem.setText(item);
    }

    @Override
    public int getItemCount() {
        if (mBaseList == null) {
            return 0;
        }
        return mBaseList.size();
    }

    public void setBaseList(ArrayList<String> baseList) {
        mBaseList = baseList;
        notifyDataSetChanged();
    }

    public ArrayList<String> getBaseList() {
        return mBaseList;
    }

    class BaseListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.base_item_name_tv)
        TextView baseItem;

        public BaseListAdapterViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition, baseItem.getText().toString());
        }
    }
}

