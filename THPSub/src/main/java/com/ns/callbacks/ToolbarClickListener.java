package com.ns.callbacks;


import com.ns.model.ToolbarCallModel;



public interface ToolbarClickListener {

    void onBackClickListener();
    void onSearchClickListener(ToolbarCallModel toolbarCallModel);
    void onShareClickListener(ToolbarCallModel toolbarCallModel);
    void onCreateBookmarkClickListener(ToolbarCallModel toolbarCallModel);
    void onRemoveBookmarkClickListener(ToolbarCallModel toolbarCallModel);
    void onFontSizeClickListener(ToolbarCallModel toolbarCallModel);
    void onCommentClickListener(ToolbarCallModel toolbarCallModel);
    void onTTSPlayClickListener(ToolbarCallModel toolbarCallModel);
    void onTTSStopClickListener(ToolbarCallModel toolbarCallModel);
    void onFavClickListener(ToolbarCallModel toolbarCallModel);
    void onLikeClickListener(ToolbarCallModel toolbarCallModel);
}
