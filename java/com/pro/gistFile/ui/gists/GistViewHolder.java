package com.jldubz.gistaviewer.ui.gists;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jldubz.gistaviewer.R;
import com.jldubz.gistaviewer.model.GitHubUser;
import com.jldubz.gistaviewer.model.gists.Gist;
import com.jldubz.gistaviewer.model.gists.GistFile;

import java.text.DateFormat;
import java.util.Map;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Displays a GitHub Gist in a RecyclerView that handles clicks
 *
 * @author Jon-Luke West
 */
class GistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mFileNameText;
    private TextView mUsernameText;
    private ImageView mAvatarImage;
    private TextView mUpdatedText;

    private IGistViewHolderListener mListener;

    GistViewHolder(@NonNull View itemView, IGistViewHolderListener listener) {
        super(itemView);

        mListener = listener;

        mFileNameText = itemView.findViewById(R.id.text_gist_filename);
        mUsernameText = itemView.findViewById(R.id.text_comment_created);
        mAvatarImage = itemView.findViewById(R.id.image_gist_user_avatar);
        mUpdatedText = itemView.findViewById(R.id.text_gist_updated);
        itemView.setOnClickListener(this);
    }

    /***
     * Configure the view according to the Gist info provided
     * @param gist the Gist info to display in this view
     */
    void configureView(Gist gist)
    {
        //Set the filename to the first file in the Gist
        Map<String, GistFile> gistFiles = gist.getFiles();

        if (gistFiles != null && gistFiles.size() > 0)
        {
            GistFile firstFile = gistFiles.values().iterator().next();
            mFileNameText.setText(firstFile.getFilename());
        }
        else
            {
            mFileNameText.setText("ERROR - NO FILES");
        }

        //Set the updated time


        String updatedAtTime = DateFormat.getDateInstance().format(gist.getUpdated_at());
        mUpdatedText.setText(updatedAtTime);
        GitHubUser owner = gist.getOwner();

        if (owner != null)
        {
            //Set the author avatar image
            RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_avatar_placeholder);

            Glide.with(mAvatarImage)
                    .load(owner.getAvatar_url())
                    .apply(options)
                    .into(mAvatarImage);

            //Set the author login name
            mUsernameText.setText(owner.getLogin());
        }
        else

            {
            mUsernameText.setText("ERROR - NO USER");
        }
    }

    @Override
    public void onClick(View v) {
        if (mListener == null) {
            return;
        }

        //Tell the listener that the gist was clicked
        mListener.onGistClicked(getAdapterPosition());
    }

    interface IGistViewHolderListener {

        /***
         * Called when a Gist is clicked
         * @param position the position of the Gist item that was clicked
         */
        void onGistClicked(int position);
    }
}
