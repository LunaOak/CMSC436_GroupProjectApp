package Jobi.app.src.main.java.edu.umd.cs.jobi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.umd.cs.agileandroid.R;
import edu.umd.cs.jobi.model.Story;
import edu.umd.cs.jobi.service.StoryService;

public class BacklogFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    private static final int REQUEST_CODE_CREATE_STORY = 0;

    private StoryService storyService;

    private RecyclerView storyRecyclerView;
    private StoryAdapter adapter;

    public static BacklogFragment newInstance() {
        BacklogFragment fragment = new BacklogFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        storyService = DependencyFactory.getStoryService(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backlog, container, false);

        storyRecyclerView = (RecyclerView)view.findViewById(R.id.story_recycler_view);
        storyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CREATE_STORY) {
            if (data == null) {
                return;
            }

            Story storyCreated = StoryActivity.getStoryCreated(data);
            storyService.addStoryToBacklog(storyCreated);
            updateUI();
        }
    }

    private void updateUI() {
        Log.d(TAG, "updating UI all stories");

        List<Story> stories = storyService.getAllStories();

        if (adapter == null) {
            adapter = new StoryAdapter(stories);
            storyRecyclerView.setAdapter(adapter);
        } else {
            adapter.setStories(stories);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_backlog, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_create_story:
                Intent createStoryIntent = new Intent(getActivity(), StoryActivity.class);
                startActivityForResult(createStoryIntent, REQUEST_CODE_CREATE_STORY);
                return true;
            case R.id.menu_item_active_sprint:
                Intent activeSprintIntent = new Intent(getActivity(), SprintActivity.class);
                startActivity(activeSprintIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class StoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView summaryTextView;
        private TextView criteria;
        private TextView priorityTextView;
        private TextView pointsTextView;

        private Story story;

        public StoryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            summaryTextView = (TextView)itemView.findViewById(R.id.list_item_story_summary);
            criteria = (TextView)itemView.findViewById(R.id.list_item_story_criteria);
            priorityTextView = (TextView)itemView.findViewById(R.id.list_item_story_priority);
            pointsTextView = (TextView)itemView.findViewById(R.id.list_item_story_points);
        }

        public void bindStory(Story story) {
            this.story = story;

            summaryTextView.setText(story.getSummary());
            criteria.setText(story.getAcceptanceCriteria());
            priorityTextView.setText(story.getPriority().toString());
            pointsTextView.setText("" + story.getStoryPoints());
        }

        @Override
        public void onClick(View view) {
            Intent intent = StoryActivity.newIntent(getActivity(), story.getId());

            startActivityForResult(intent, REQUEST_CODE_CREATE_STORY);
        }
    }

    private class StoryAdapter extends RecyclerView.Adapter<StoryHolder> {
        private List<Story> stories;

        public StoryAdapter(List<Story> stories) {
            this.stories = stories;
        }

        public void setStories(List<Story> stories) {
            this.stories = stories;
        }

        @Override
        public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_story, parent, false);
            return new StoryHolder(view);
        }

        @Override
        public void onBindViewHolder(StoryHolder holder, int position) {
            Story story = stories.get(position);
            holder.bindStory(story);
        }

        @Override
        public int getItemCount() {
            return stories.size();
        }
    }
}
