package tv.marytv.video.exception;

public class ItemHasChildrenException extends IllegalStateException {
    private final String title;
    private final int childCount;

    public ItemHasChildrenException(String title, int childCount) {
        super(String.format("The item '%s' has %d child%s associated with it. If you want to delete this item, please delete all of its children first.",
                title, childCount, childCount == 1 ? "" : "ren"));
        this.title = title;
        this.childCount = childCount;
    }

    public String getTitle() {
        return title;
    }

    public int getChildCount() {
        return childCount;
    }
}